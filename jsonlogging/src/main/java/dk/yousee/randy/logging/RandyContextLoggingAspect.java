/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.yousee.randy.logging;

import com.google.gson.JsonElement;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import javax.ws.rs.core.UriInfo;
import org.apache.log4j.MDC;
import org.apache.log4j.NDC;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

/**
 * Logging aspect wraps top-level ReST service methods and inspects input
 * arguments to find any subscriber, mac or ip address to push into the logging
 * context. Also logs on exit the http return code and clears the logging
 * context again.
 *
 * @author Jacob Lorensen, TDC, December 2013.
 */
@Aspect
@Component
public class RandyContextLoggingAspect {
    private static final Logger log = Logger.getLogger(RandyContextLoggingAspect.class);
    @Autowired
    private List<ContextLoggingSearchItem> searchItems;

    @Around("execution(javax.ws.rs.core.Response *(..))")
    public Object pushLoggingContext(ProceedingJoinPoint pjp) throws Throwable {
        Object result;
        // Take the proceedingpoint apart
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        Method method = signature.getMethod();
        String methodName = method.getName();
        String methodClassName = method.getDeclaringClass().getCanonicalName();
        String[] formalArgs = signature.getParameterNames();
        Annotation[][] argAnnotations = method.getParameterAnnotations();
        Object[] actualArgs = pjp.getArgs();

        // Try and find a uriInfo in the called method's environment
        UriInfo uriInfo;
        if ((uriInfo = getUriInfoArg(actualArgs)) != null || (uriInfo = getUriInfoField(pjp.getTarget())) != null)
            MDC.put("path", uriInfo.getAbsolutePath().getRawPath());
        // Log the method name and class
        MDC.put("restmethod", methodName);
        MDC.put("restclass", methodClassName);
        // Log the method path pattern
        File pathPattern = new File(
                new File(getPathAnnotation(method.getDeclaringClass().getAnnotations())),
                getPathAnnotation(method.getAnnotations()));
        MDC.put("urlpattern", pathPattern);

        // Find and log interesting arguments
        String payload = null;
        JsonObject payloadJo = null;
        for (ContextLoggingSearchItem si : searchItems) {
            System.out.println("YAY, doing search item: " + si.getKey() + "@" + si.getSearchKeys() + " with " + si.getFormat().getClass().getName());
            // Check arguments 
            for (int argc = 0; argc < argAnnotations.length; argc++) {
                if (actualArgs[argc] == null)
                    continue;
                Annotation[] annotations = argAnnotations[argc];
                // String arg receiving entity body does not have annotations - check and parse payload
                if (annotations.length == 0 && payload == null) {
                    payload = getPayload(actualArgs[argc]);
                    if (payload != null) {
                        try {
                            JsonElement body = new JsonParser().parse(payload);
                            if (body.isJsonObject())
                                payloadJo = body.getAsJsonObject();
                        } catch (Exception e) {
                            // well, json parsing didn't work so just ignore it silently
                        }
                    }
                    // we've caught the http entity body, continue with next argument
                    continue;
                }
                // Precedence: path/query-param then payload then name of formal arguments
                analyzeFormalArg(si, actualArgs[argc], formalArgs[argc]);
                analyzePayload(si, payloadJo);
                analyzeArgAnnotations(si, actualArgs[argc], annotations);
            }
        }
        // proceed calling and analyze result
        try {
            result = pjp.proceed();
            if (result instanceof Response) {
                Response r = (Response) result;
                MDC.put("httpstatus", r.getStatus());
                if (r.getStatus() >= 300) {
                    if (payload != null)
                        MDC.put("input", payload);
                    if (r.getEntity() != null)
                        MDC.put("output", r.getEntity());
                }
            }
            log.info("(return)");
            return result;
        } catch (Throwable exception) {
            MDC.put("uncaughtexceptionmsg", exception.toString());
            if (payload != null)
                MDC.put("input", payload);
            log.warn("uncaught exception", exception);
            throw exception;
        } finally {
            NDC.remove();
            MDC.clear();
        }
    }

    private String getPayload(Object arg) {
        if (!(arg instanceof java.lang.String))
            return null;
        return (String) arg;
    }

    private void analyzePayload(ContextLoggingSearchItem si, JsonObject body) {
        if (body == null)
            return;
        for (String s : si.getSearchKeys()) {
            // can't just bo.get(s) because we want loose, non-case-sensitive match        
            for (Map.Entry<String, JsonElement> e : body.entrySet()) {
                if (s.equalsIgnoreCase(e.getKey())) {
                    MDC.put(si.getKey(), si.getFormat().format(e.getValue().getAsString()));
                    break;
                }
            }
        }
    }

    private void analyzeArgAnnotations(ContextLoggingSearchItem si, Object arg, Annotation[] annotations) {
        if (arg == null)
            return;
        // else check annotations for anything interesting on this argument
        for (Annotation annotation : annotations) {
            Class<? extends Annotation> annotationType = annotation.annotationType();
            String value = null;
            if (annotationType.equals(javax.ws.rs.PathParam.class)) {
                javax.ws.rs.PathParam pathParam = (javax.ws.rs.PathParam) annotation;
                value = pathParam.value();
            } else if (annotationType.equals(javax.ws.rs.QueryParam.class)) {
                javax.ws.rs.QueryParam queryParam = (javax.ws.rs.QueryParam) annotation;
                value = queryParam.value();
            }
            if (value == null)
                continue;
            for (String s : si.getSearchKeys()) {
                if (s.equalsIgnoreCase(value)) {
                    MDC.put(si.getKey(), si.getFormat().format(arg.toString()));
                    continue;
                }
            }
        }
    }

    private void analyzeFormalArg(ContextLoggingSearchItem si, Object arg, String paramName) {
        if (paramName == null || arg == null)
            return;
        for (String s : si.getSearchKeys()) {
            if (s.equalsIgnoreCase(paramName))
                MDC.put(si.getKey(), si.getFormat().format(arg.toString()));
        }
    }

    private UriInfo getUriInfoField(Object t) {
        // Find a member of type UriInfo
        Field[] fields = t.getClass().getFields();
        for (Field f : fields) {
            if (f.getType().equals(UriInfo.class)) {
                try {
                    return (UriInfo) f.get(t);
                } catch (IllegalArgumentException ex) {
                    java.util.logging.Logger.getLogger(RandyContextLoggingAspect.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IllegalAccessException ex) {
                    java.util.logging.Logger.getLogger(RandyContextLoggingAspect.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ClassCastException ex) {
                    java.util.logging.Logger.getLogger(RandyContextLoggingAspect.class.getName()).log(Level.SEVERE, null, ex);
                }
                return null;
            }
        }
        return null;
    }

    private UriInfo getUriInfoArg(Object[] args) {
        for (Object arg : args) {
            if (arg instanceof UriInfo) {
                try {
                    return (UriInfo) arg;
                } catch (IllegalArgumentException ex) {
                    java.util.logging.Logger.getLogger(RandyContextLoggingAspect.class.getName()).log(Level.SEVERE, null, ex);
                } catch (ClassCastException ex) {
                    java.util.logging.Logger.getLogger(RandyContextLoggingAspect.class.getName()).log(Level.SEVERE, null, ex);
                }
                return null;
            }
        }
        return null;
    }

    private String getPathAnnotation(Annotation[] annotations) {
        for (Annotation annotation : annotations) {
            try {
                Class<? extends Annotation> annotationType = annotation.annotationType();
                if (annotationType.equals(javax.ws.rs.Path.class)) {
                    javax.ws.rs.Path path = (javax.ws.rs.Path) annotation;
                    return path.value();
                } else {
                    System.out.println("Not a Path annotation: " + annotationType);
                }
            } catch (ClassCastException ex) {
                // not possible
            }
        }
        return null;
    }

    public List<ContextLoggingSearchItem> getSearchItems() {
        return searchItems;
    }

    public void setSearchItems(List<ContextLoggingSearchItem> searchItems) {
        this.searchItems = searchItems;
    }
}

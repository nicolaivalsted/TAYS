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
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dk.yousee.randy.macaddress.MACaddress;
import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.logging.Level;
import javax.ws.rs.core.UriInfo;
import org.apache.log4j.MDC;
import org.apache.log4j.NDC;
import org.apache.log4j.Priority;
import org.aspectj.lang.reflect.MethodSignature;

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

    @Around("execution(javax.ws.rs.core.Response *(..))")
    public Object pushLoggingContext(ProceedingJoinPoint pjp) throws Throwable {
        AnalysisState aState = new AnalysisState();
        UriInfo uriInfo;
        Object result;
        String methodClassName = this.getClass().getCanonicalName();
        Logger methodLogger = Logger.getLogger(methodClassName);
        try {
            MethodSignature signature = (MethodSignature) pjp.getSignature();
            Method method = signature.getMethod();
            String[] parameterNames = signature.getParameterNames();
            Annotation[][] parameterAnnotations = method.getParameterAnnotations();
            Object[] args = pjp.getArgs();
            // Try and find a uriInfo in the called method's environment
            uriInfo = peekUriInfoArg(args);
            if (uriInfo == null)
                uriInfo = peekUriInfoField(pjp.getTarget());
            if (uriInfo != null) {
                MDC.put("path", uriInfo.getAbsolutePath().getRawPath());
            }
            // Log the method name and class
            String methodName = method.getName();
            methodClassName = method.getDeclaringClass().getCanonicalName();
            MDC.put("restmethod", methodName);
            MDC.put("restclass", methodClassName);
            // Log the method path pattern
            File pathPattern = new File(
                    new File(peekPathAnnotation(method.getDeclaringClass().getAnnotations())),
                    peekPathAnnotation(method.getAnnotations()));
            MDC.put("urlpattern", pathPattern);
            // Log interesting parameters from arguments
            analyzeArguments(aState, parameterAnnotations, parameterNames, args);
            result = pjp.proceed();
            if (result instanceof Response) {
                Response r = (Response) result;
                MDC.put("httpstatus", r.getStatus());
                if (r.getStatus() >= 300) {
                    if (aState.payload != null)
                        MDC.put("input", aState.payload);
                    if (r.getEntity() != null)
                        MDC.put("output", r.getEntity());
                }
            }
            methodLogger.info("(return)");
            return result;
        } catch (Throwable exception) {
            MDC.put("uncaughtexceptionmsg", exception.toString());
            if (aState.payload != null)
                MDC.put("input", aState.payload);
            methodLogger.warn("uncaught exception", exception);
            throw exception;
        } finally {
            NDC.remove();
            MDC.clear();
        }
    }

    private UriInfo peekUriInfoField(Object t) {
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

    private UriInfo peekUriInfoArg(Object[] args) {
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

    private String peekPathAnnotation(Annotation[] annotations) {
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

    private void analyzeArguments(AnalysisState a, Annotation[][] argAnnotations, String[] parameterNames, Object[] args) {
        // Check annotations for anything that looks like a subscriber, mac or ip; use the first found.
        for (int argc = 0; argc < argAnnotations.length; argc++) {
            if (args[argc] == null)
                continue;
            Annotation[] annotations = argAnnotations[argc];
            analyzePayload(a, args[argc], annotations);
            analyzeArgByAnnotation(a, args[argc], annotations);
        }
        analyzeFormalArgs(a, args, parameterNames);
    }

    private void analyzePayload(AnalysisState a, Object arg, Annotation[] annotations) {
        // zero annotations, then it could be "String body" or "InputStream body"
        if (annotations.length == 0) {
            if (arg instanceof java.lang.String) {
                try {
                    a.payload = (String) arg;
                    JsonElement body = new JsonParser().parse((String) arg);
                    if (body.isJsonObject()) {
                        JsonObject bo = body.getAsJsonObject();
                        for (Map.Entry<String, JsonElement> e : bo.entrySet()) {
                            if (isSubKey(e.getKey())) {
                                MDC.put("subscriber", e.getValue().getAsString());
                                a.subFound = true;
                            } else if (isMacKey(e.getKey())) {
                                MDC.put("mac", e.getValue().getAsString());
                                a.macFound = true;
                            } else if (isIpKey(e.getKey())) {
                                MDC.put("ip", e.getValue().getAsString());
                                a.ipFound = true;
                            }
                        }
                    }
                } catch (Exception e) {
                    // well, json parsing didn't work so just ignore it silently
                }
                return;
            }
            if (arg instanceof java.io.InputStream) {
                // need to be sneaky here, we want to peek at the InputStream while still leaving it to the real method to read and parse it
                // not supported
            }
        }
    }

    private void analyzeArgByAnnotation(AnalysisState a, Object arg, Annotation[] annotations) {
        // else check annotations for anything interesting on this argument
        for (Annotation annotation : annotations) {
            try {
                Class<? extends Annotation> annotationType = annotation.annotationType();
                String value = null;
                if (annotationType.equals(javax.ws.rs.PathParam.class)) {
                    javax.ws.rs.PathParam pathParam = (javax.ws.rs.PathParam) annotation;
                    value = pathParam.value();
                } else if (annotationType.equals(javax.ws.rs.QueryParam.class)) {
                    javax.ws.rs.QueryParam queryParam = (javax.ws.rs.QueryParam) annotation;
                    value = queryParam.value();
                } else {
                    System.out.println("Not a QUery or PathParam: " + annotationType);
                }
                if (value == null)
                    continue;
                if (arg == null)
                    continue;
                if (!a.subFound) {
                    if (isSubKey(value)) {
                        MDC.put("subscriber", arg);
                        a.subFound = true;
                    }
                }
                if (!a.macFound) {
                    if (isMacKey(value)) {
                        String argStr = arg.toString();
                        MACaddress mac = MACaddress.parseMACaddress(argStr);
                        if (mac != null)
                            argStr = mac.toString(MACaddress.fmtCOLON);
                        MDC.put("mac", argStr);
                        a.macFound = true;
                    }
                }
                if (!a.ipFound) {
                    if (isIpKey(value)) {
                        MDC.put("ip", arg);
                        a.ipFound = true;
                    }
                }
            } catch (ClassCastException ex) {
                // not possible
            }
        }
    }

    private void analyzeFormalArgs(AnalysisState a, Object[] args, String[] parameterNames) {
        // Check formal arguments for anything that looks like a name of a subscriber, mac or ip; use the first found
        for (int argc = 0; argc < parameterNames.length; argc++) {
            String paramName = parameterNames[argc];
            if (paramName == null)
                continue;
            if (args[argc] == null)
                continue;
            if (!a.subFound) {
                if (isSubKey(paramName)) {
                    MDC.put("subscriber", args[argc]);
                    a.subFound = true;
                }
            }
            if (!a.macFound) {
                if (isMacKey(paramName)) {
                    String arg = (args[argc] != null) ? args[argc].toString() : "";
                    MACaddress mac = MACaddress.parseMACaddress(arg);
                    if (mac != null)
                        arg = mac.toString(MACaddress.fmtCOLON);
                    MDC.put("mac", arg);
                    a.macFound = true;
                }
            }
            if (!a.ipFound) {
                if (isIpKey(paramName)) {
                    MDC.put("ip", args[argc]);
                    a.ipFound = true;
                }
            }
        }
    }

    private boolean isSubKey(String paramName) {
        if (paramName == null)
            return false;
        return paramName.equalsIgnoreCase("accno") || paramName.equalsIgnoreCase("subscriber") || paramName.equalsIgnoreCase("subscriberId") || paramName.equalsIgnoreCase("subid");
    }

    private boolean isMacKey(String value) {
        if (value == null)
            return false;
        return value.equalsIgnoreCase("mac") || value.equalsIgnoreCase("macaddress")
                || value.equalsIgnoreCase("cmmac") || value.equalsIgnoreCase("cpemac") || value.equalsIgnoreCase("mtamac");
    }

    private boolean isIpKey(String paramName) {
        if (paramName == null)
            return false;
        return paramName.equalsIgnoreCase("ip") || paramName.equalsIgnoreCase("ipaddress") || paramName.equalsIgnoreCase("ipaddr");
    }
}

/**
 * @author jablo
 */
class AnalysisState {
    boolean subFound = false;
    boolean macFound = false;
    boolean ipFound = false;
    String payload = null;
}
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.yousee.randy.logging;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import javax.ws.rs.core.Response;

import org.apache.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import javax.ws.rs.core.UriInfo;
import org.apache.log4j.MDC;
import org.apache.log4j.NDC;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;

/**
 * Logging aspect wraps top-level ReST service methods and inspects input
 * arguments to find any subscriber, mac or ip address to push into the logging
 * context. Also logs on exit the http return code and clears the logging
 * context again.
 *
 * @author Jacob Lorensen, TDC, December 2013.
 */
@Aspect
public class RandyContextLoggingAspect {
    private static final Logger log = Logger.getLogger(RandyContextLoggingAspect.class);
    private List<ContextLoggingSearchItem> searchItems;
    // Configuration - name of fields in json log document
    private int logPayloadHttpStatusMin = 500;
    private boolean logInput = true;
    private String urlpatternJson = "urlpattern";
    private String requestUriJson = "requesturi";
    private String restMethodJson = "restmethod";
    private String restClassJson = "restclass";
    private String inputJson = "input";
    private String outputJson = "output";
    private String httpstatusJson = "httpstatus";
    private String uncaughtexceptionmsgJson = "uncaughtexceptionmsg";
    private String calluidJson = "calluid";
    //
    private Gson gson = new Gson();

    @Around("execution(javax.ws.rs.core.Response *(..))")
    public Object pushLoggingContext(ProceedingJoinPoint pjp) throws Throwable {
        // Take the proceedingpoint apart
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        Method method = signature.getMethod();
        String methodName = method.getName();
        String methodClassName = method.getDeclaringClass().getCanonicalName();
        String[] formalArgs = signature.getParameterNames();
        Annotation[][] argAnnotations = method.getParameterAnnotations();
        Object[] actualArgs = pjp.getArgs();

        // Interesting finds from the method-to-call
        UriInfo uriInfo;
        File pathPattern;
        String payload = null;
        JsonObject payloadJo = null;

        try {
            MDC.put(calluidJson, UUID.randomUUID());
            // Try and find a uriInfo in the called method's environment
            if ((uriInfo = getUriInfoArg(actualArgs)) != null || (uriInfo = getUriInfoField(pjp.getTarget())) != null)
                MDC.put(requestUriJson, uriInfo.getRequestUri().toString());
            // Log the method name and class
            MDC.put(restMethodJson, methodName);
            MDC.put(restClassJson, methodClassName);
            // Log the method path pattern
            pathPattern = new File(
                    new File(getPathAnnotation(method.getDeclaringClass().getAnnotations())),
                    getPathAnnotation(method.getAnnotations()));
            MDC.put(urlpatternJson, pathPattern.getPath());

            // Find and log interesting arguments
            for (ContextLoggingSearchItem si : searchItems) {
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
                        // we've caught what we assume must be the http entity body, continue with next argument
                        continue;
                    }
                    // Precedence: path/query-param then payload then name of formal arguments
                    analyzeFormalArg(si, actualArgs[argc], formalArgs[argc]);
                    analyzePayload(si, payloadJo);
                    analyzeArgAnnotations(si, actualArgs[argc], annotations);
                }
            }
        } catch (Exception e) {
            log.error("Auto-logging aspect error - continuing", e);
        }

        // proceed calling and analyze result
        try {
            Response r = (Response) pjp.proceed();
            Object entity = r.getEntity();
            MDC.put(httpstatusJson, r.getStatus());
            if (r.getStatus() >= logPayloadHttpStatusMin) {
                if (entity != null) {
                    analyzeResponse(entity);
                    MDC.put(outputJson, entity);
                }
                if (payloadJo != null)
                    MDC.put(inputJson, payloadJo);
                else if (payload != null)
                    MDC.put(inputJson, payload);
            }
            log.info("(return)");
            return r;
        } catch (Throwable exception) {
            MDC.put(uncaughtexceptionmsgJson, exception.toString());
            if (payloadJo != null)
                MDC.put(inputJson, payloadJo);
            else if (payload != null)
                MDC.put(inputJson, payload);
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

    /**
     * Format the response entity as a json object and analysze it. It don't like this
     * <ol>
     * <li>Response gets serialized twice
     * <li>We don't necessarily have the right serialization formatter
     * <li>We should look into the field names instead (fiels and getters&mdash;also complicated)
     * </ol>
     * @param response 
     */
    private void analyzeResponse(Object response) {
        if (response == null)
            return;
        JsonObject joResponse;
        if (response instanceof JsonObject)
            joResponse = (JsonObject) response;
        else {
            JsonElement jsonTree = gson.toJsonTree(response);
            if (!jsonTree.isJsonObject())
                return;
            joResponse = jsonTree.getAsJsonObject();
        }
        for (ContextLoggingSearchItem si : searchItems) {
            analyzePayload(si, joResponse);
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
                } catch (Exception e) {
                    // we have done dynamic type checking, so exception ClassCast etc. can be ignored
                }
            }
        }
        return null;
    }

    private UriInfo getUriInfoArg(Object[] args) {
        for (Object arg : args) {
            if (arg instanceof UriInfo) {
                return (UriInfo) arg;
            }
        }
        return null;
    }

    private String getPathAnnotation(Annotation[] annotations) {
        for (Annotation annotation : annotations) {
            Class<? extends Annotation> annotationType = annotation.annotationType();


            if (annotationType.equals(javax.ws.rs.Path.class)) {
                javax.ws.rs.Path path = (javax.ws.rs.Path) annotation;

                return path.value();
            }
        }
        return "/";
    }

    public List<ContextLoggingSearchItem> getSearchItems() {
        return searchItems;
    }

    public void setSearchItems(List<ContextLoggingSearchItem> searchItems) {
        this.searchItems = searchItems;
    }

    public int getLogPayloadHttpStatusMin() {
        return logPayloadHttpStatusMin;
    }

    public void setLogPayloadHttpStatusMin(int logPayloadHttpStatusMin) {
        this.logPayloadHttpStatusMin = logPayloadHttpStatusMin;
    }

    public boolean isLogInput() {
        return logInput;
    }

    public void setLogInput(boolean logInput) {
        this.logInput = logInput;
    }

    public String getUrlpatternJson() {
        return urlpatternJson;
    }

    public void setUrlpatternJson(String urlpatternJson) {
        this.urlpatternJson = urlpatternJson;
    }

    public String getRequestUriJson() {
        return requestUriJson;
    }

    public void setRequestUriJson(String requestUriJson) {
        this.requestUriJson = requestUriJson;
    }

    public String getRestMethodJson() {
        return restMethodJson;
    }

    public void setRestMethodJson(String restMethodJson) {
        this.restMethodJson = restMethodJson;
    }

    public String getRestClassJson() {
        return restClassJson;
    }

    public void setRestClassJson(String restClassJson) {
        this.restClassJson = restClassJson;
    }

    public String getInputJson() {
        return inputJson;
    }

    public void setInputJson(String inputJson) {
        this.inputJson = inputJson;
    }

    public String getOutputJson() {
        return outputJson;
    }

    public void setOutputJson(String outputJson) {
        this.outputJson = outputJson;
    }

    public String getHttpstatusJson() {
        return httpstatusJson;
    }

    public void setHttpstatusJson(String httpstatusJson) {
        this.httpstatusJson = httpstatusJson;
    }

    public String getUncaughtexceptionmsgJson() {
        return uncaughtexceptionmsgJson;
    }

    public void setUncaughtexceptionmsgJson(String uncaughtexceptionmsgJson) {
        this.uncaughtexceptionmsgJson = uncaughtexceptionmsgJson;
    }

    public String getCalluidJson() {
        return calluidJson;
    }

    public void setCalluidJson(String calluidJson) {
        this.calluidJson = calluidJson;
    }
}

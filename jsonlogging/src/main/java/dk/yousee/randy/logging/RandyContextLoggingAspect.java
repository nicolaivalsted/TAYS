/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.yousee.randy.logging;

import com.google.gson.Gson;

import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;

import org.apache.log4j.Logger;
import org.apache.log4j.MDC;
import org.apache.log4j.NDC;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.Ordered;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import javax.ws.rs.core.PathSegment;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * Logging aspect wraps top-level ReST service methods and inspects input
 * arguments to find any subscriber, mac or ip address to push into the logging
 * context. Also logs on exit the http return code and clears the logging
 * context again.
 *
 * @author Jacob Lorensen, TDC, December 2013.
 */
@Aspect
public class RandyContextLoggingAspect implements Ordered {
    private static final Logger log = Logger.getLogger(RandyContextLoggingAspect.class);
    private List<ContextLoggingSearchItem> searchItems = new ArrayList<ContextLoggingSearchItem>();
    private Gson gson = new Gson();
    // Configuration - aspect
    private int order = Ordered.LOWEST_PRECEDENCE;
    // Configuration - name of fields in json log document
    private int logPayloadHttpStatusMin = 500;
    private boolean logInput = true;
    private String restClassJson = "restclass";
    private String restMethodJson = "restmethod";
    //
    private String uriPatternJson = "uripattern";
    private String requestUriJson = "requesturi";
    private String requestEntityJson = "requestentity";
    private String responseEntityJson = "responseentity";
    private String httpStatusJson = "httpstatus";
    private String uncaughtExcsptionMsgJson = "uncaughtexceptionmsg";
    private String callUUIDJson = "calluuid";

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
        JsonElement payloadParsed = null; // prefer payloadJo if payload is json parseable, otherwise
        String payload = null; // just log the payload as-is. Json log formatter should quote correctly

        try {
            MDC.put(callUUIDJson, UUID.randomUUID());
            // Try and find a uriInfo in the called method's environment
            if ((uriInfo = getUriInfoArg(actualArgs)) != null || (uriInfo = getUriInfoField(pjp.getTarget())) != null) {
                MDC.put(requestUriJson, uriInfo.getRequestUri().toString());
            }
            // Log the method name and class
            MDC.put(restMethodJson, methodName);
            MDC.put(restClassJson, methodClassName);
            // Log the method path pattern
            pathPattern = new File(new File(getPathAnnotation(method.getDeclaringClass().getAnnotations())),
                    getPathAnnotation(method.getAnnotations()));
            MDC.put(uriPatternJson, pathPattern);

            // Find and log interesting arguments. 
            // Output: payloadJo and payload - payloadJo preferred, with payload as second best
            // Side effect: MDC populated with interesting fields
            for (ContextLoggingSearchItem si : searchItems) {
                // Check arguments 
                for (int argc = 0; argc < argAnnotations.length; argc++) {
                    if (actualArgs[argc] == null)
                        continue;
                    Annotation[] annotations = argAnnotations[argc];
                    // String arg receiving entity body does not have annotations - check and parse payload
                    // or has RequestBody annotation
                    if (payload == null) {
                        if (annotations.length == 0 || annotationsContains(annotations, RequestBody.class)) {
                            payload = getPayload(actualArgs[argc]);
                            if (payload != null) {
                                try {
                                    payloadParsed = new JsonParser().parse(payload);
                                } catch (Exception e) {
                                    // well, json parsing didn't work so just ignore it silently
                                }
                            }
                            // we've caught what we assume must be the http entity body, continue with next argument
                            continue;
                        }
                    }
                    // Precedence: path/query-param then payload then name of formal arguments
                    analyzeFormalArg(si, actualArgs[argc], formalArgs[argc]);
                    analyzePayload(si, payloadParsed);
                    analyzeArgAnnotations(si, actualArgs[argc], annotations);
                }
            }
        } catch (Exception e) {
            log.error("Auto-logging aspect error - continuing", e);
        }

        // proceed call and analyze result
        try {
            Response r = (Response) pjp.proceed();
            Object entity = r.getEntity();
            analyzeResponse(entity);
            MDC.put(httpStatusJson, r.getStatus());
            if (r.getStatus() >= logPayloadHttpStatusMin) {
                if (entity != null) {
                    MDC.put(responseEntityJson, entity);
                }
                // prefer parsed json object - it gives nested json object in the log
                if (payloadParsed != null) {
                    MDC.put(requestEntityJson, payloadParsed);
                } else if (payload != null) {
                    MDC.put(requestEntityJson, payload);
                }
            }
            log.info("(return)");
            return r;
        } catch (Throwable exception) {
            MDC.put(uncaughtExcsptionMsgJson, exception.toString());
            // prefer parsed json object - it gives nested json object in the log
            if (payloadParsed != null) {
                MDC.put(requestEntityJson, payloadParsed);
            } else if (payload != null) {
                MDC.put(requestEntityJson, payload);
            }
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

    private void analyzePayload(ContextLoggingSearchItem si, JsonElement ebody) {
        if (ebody == null || !ebody.isJsonObject())
            return;
        JsonObject body = ebody.getAsJsonObject();
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
     * Format the response entity as a json object and analysze it. It don't
     * like this
     * <ol>
     * <li>Response gets serialized twice
     * <li>We don't necessarily have the right serialization formatter
     * <li>We should look into the field names instead (fiels and
     * getters&mdash;also complicated)
     * </ol>
     *
     * @param response
     */
    private void analyzeResponse(Object response) {
        try {
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
        } catch (Exception e) {
            log.warn("Error serializing return value for analysis - ignored", e);
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
            if (s.equalsIgnoreCase(paramName)) {
                Object value = si.getFormat().format(arg.toString());
                if (value != null)
                    MDC.put(si.getKey(), value);
            }
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

    private boolean annotationsContains(Annotation[] annotations, Class<RequestBody> aClass) {
        for (Annotation a : annotations) {
            if (a.annotationType().equals(RequestBody.class))
                return true;
        }
        return false;
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
        return uriPatternJson;
    }

    public void setUrlpatternJson(String urlpatternJson) {
        this.uriPatternJson = urlpatternJson;
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
        return requestEntityJson;
    }

    public void setInputJson(String inputJson) {
        this.requestEntityJson = inputJson;
    }

    public String getOutputJson() {
        return responseEntityJson;
    }

    public void setOutputJson(String outputJson) {
        this.responseEntityJson = outputJson;
    }

    public String getHttpstatusJson() {
        return httpStatusJson;
    }

    public void setHttpstatusJson(String httpstatusJson) {
        this.httpStatusJson = httpstatusJson;
    }

    public String getUncaughtexceptionmsgJson() {
        return uncaughtExcsptionMsgJson;
    }

    public void setUncaughtexceptionmsgJson(String uncaughtexceptionmsgJson) {
        this.uncaughtExcsptionMsgJson = uncaughtexceptionmsgJson;
    }

    @Override
    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    public String getCalluidJson() {
        return callUUIDJson;
    }

    public void setCalluidJson(String calluidJson) {
        this.callUUIDJson = calluidJson;
    }
}

package dk.yousee.randy.logging;

import java.io.IOException;
import java.security.Principal;
import java.util.Enumeration;
import java.util.UUID;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import org.apache.log4j.Logger;
import org.apache.log4j.MDC;
import org.apache.log4j.NDC;

/**
 * Filter to spy on the incoming http request and put log-worthy information on
 * the log4j MDC. Logged:
 * <ul>
 * <li>call UUID
 * <li>Root Context
 * <li>HTTP method
 * <li>Principal / User name
 * <li>Authentication method
 * </ul>
 * Also clears the logging context (NDC and MDC) on return
 * @author Jacob Lorensen, YouSee, 2014-01-11
 * @see RandyContextLoggingAspect
 * @see KVParsingJSONFormatter
 */
public class RandyContextLoggingFilter implements Filter {
    private static final Logger log = Logger.getLogger(RandyContextLoggingFilter.class.getName());
    // parm names
    private static final String calluuidJsonFieldParmName = "calluuidjsonfield";
    private static final String contextJsonFieldParmName = "contextjsonfield";
    private static final String methodJsonFieldParmName = "methodjsonfield";
    private static final String principalJsonFieldParmName = "principaljsonfield";
    private static final String authtypeJsonFieldParmName = "authtypejsonfield";
    private static final String requesturiJsonFieldParmName = "requesturijsonfield";
    // parm values
    private String callUUIDJson = "calluuid";
    private String contextJsonField = "context";
    private String methodJsonField = "httpmethod";
    private String principalJsonField = "username";
    private String authtypeJsonField = "authtype";
    private String requesturiJsonField = "requesturi";

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Enumeration<String> initParameterNames = filterConfig.getInitParameterNames();
        while (initParameterNames.hasMoreElements()) {
            String paramName = initParameterNames.nextElement();
            if (paramName.equals(contextJsonFieldParmName))
                contextJsonField = filterConfig.getInitParameter(paramName);
            else if (paramName.equals(methodJsonFieldParmName))
                methodJsonField = filterConfig.getInitParameter(paramName);
            else if (paramName.equals(principalJsonFieldParmName))
                principalJsonField = filterConfig.getInitParameter(paramName);
            else if (paramName.equals(authtypeJsonFieldParmName))
                authtypeJsonField = filterConfig.getInitParameter(paramName);
            else if (paramName.equals(requesturiJsonFieldParmName))
                requesturiJsonField = filterConfig.getInitParameter(paramName);
            else if (paramName.equals(calluuidJsonFieldParmName))
                callUUIDJson = filterConfig.getInitParameter(paramName);
            else
                log.warn("Unknown parameter " + paramName + " ignored");
        }
        log.info("RandyContextLoggingFilter initialized");
    }

    @Override
    public void destroy() {
    }

    @Override
    public void doFilter(ServletRequest _request, ServletResponse _response,
            FilterChain chain) throws IOException, ServletException {
        log.debug("RandyContextLoggingFilter filter!");
        try {
            MDC.put(callUUIDJson, UUID.randomUUID());
            HttpServletRequest req;
            try {
                req = (HttpServletRequest) _request;
            } catch (ClassCastException ex) {
                log.warn("Cannot cast request to HttpRequest - this filter only works for HttpServlets");
                chain.doFilter(_request, _response);
                return;
            }
            String contextPath = req.getContextPath();
            if (contextPath != null)
                MDC.put(contextJsonField, contextPath);
            String method = req.getMethod();
            if (method != null)
                MDC.put(methodJsonField, method);
            Principal userPrincipal = req.getUserPrincipal();
            if (userPrincipal != null && userPrincipal.getName() != null)
                MDC.put(principalJsonField, userPrincipal.getName());
            String authType = req.getAuthType();
            if (authType != null)
                MDC.put(authtypeJsonField, authType);
            String requestURI = req.getRequestURI();
            if (requestURI != null)
                MDC.put(requesturiJsonField, requestURI);
            chain.doFilter(req, _response);
        } finally {
            NDC.remove();
            MDC.clear();
        }
    }
}
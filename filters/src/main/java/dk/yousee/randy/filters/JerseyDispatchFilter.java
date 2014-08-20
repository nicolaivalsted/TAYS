package dk.yousee.randy.filters;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;


/**
 * This Filter prepends a sub path (eg. /rest)&mdash;and forward to the corresponding servlet handler&mdash;
 * to any path which does matches a set of regular expressions; another set of regualar expressions
 * can be given to match urls that should be directed to the default servlset, ie. static content.<p>
 * All regular expressions are given as filter configuration parameters.<p>
 * In the course of rewriting URLs, the original and rewritten URls are stored in headers in the request
 * thus making it possible for another filter (eg. JSONUrlRewriter) to make the opposite rewriting for
 * outgoing content.<p>
 * Configuration parameters:
 * <dl>
 * <dt>original-url-prefix-header-name</dt>
 * <dd>Name of a user-defined HTTP header to store the original unmodified URI</dd>
 * <dt>rewritten-url-prefix-header-name</dt>
 * <dd>Name of a user-defined HTTP header to store the rewritten URI</dd>
 * <dt>jersey-resource-prefix</dt>
 * <dd>The path prefix to prepend diercting the request to the jersey servlet; shuold match the
 * servlet url pattern for the Jersey servlet</dd>
 * <dt>static-resource-regexp<i>/hellip</i></dt>
 * <dd>Any parameter that begins with static-resource-regexp is used as a match against
 * <code>req.getRequestURI().substring(req.getContextPath().length());</code> and if it matches
 * the request is directed to the default servlet.</dd>
 * <dt>jersey-resource-regexp<i>/hellip</i></dt>
 * <dd>Any parameter that begins with jersey-resource-regexp is used as a match against
 * <code>req.getRequestURI().substring(req.getContextPath().length());</code> and if it matches
 * the request is directed to the servlet at <code>jersey-resource-prefix</code>.
 * </dl>
 *
 * @author Jacob Lorensen, YouSee, 2012-06-12
 *         See also JSONUrlRewriter
 */
public class JerseyDispatchFilter implements Filter {
    private static final Logger log = Logger.getLogger(JerseyDispatchFilter.class.getName());
    //    private FilterConfig filterConfig;
    private static final String originHeaderParmName = "original-url-prefix-header-name";
    private static final String rewrittenHeaderParmName = "rewritten-url-prefix-header-name";
    private static final String jerseyPrefixParmName = "jersey-resource-prefix";
    private static final String staticResourceRegexpParmNamePrefix = "static-resource-regexp";
    private static final String jerseyResrouceRegexpParmNamePrefix = "jersey-resource-regexp";
    private final List<Pattern> staticPrefixPatterns = new ArrayList<Pattern>();
    private final List<Pattern> jerseyPrefixPatterns = new ArrayList<Pattern>();
    private String jerseyPrefix;
    private String originHeaderName, rewrittenUrlPrefixHeaderName;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.fine("Jersey filter initializing");
        Enumeration<String> initParameterNames = filterConfig.getInitParameterNames();
        while (initParameterNames.hasMoreElements()) {
            String paramName = initParameterNames.nextElement();
            try {
                if (paramName.startsWith(staticResourceRegexpParmNamePrefix))
                    staticPrefixPatterns.add(Pattern.compile(filterConfig.getInitParameter(paramName)));
                else if (paramName.startsWith(jerseyResrouceRegexpParmNamePrefix))
                    jerseyPrefixPatterns.add(Pattern.compile(filterConfig.getInitParameter(paramName)));
                else if (paramName.equals(originHeaderParmName))
                    originHeaderName = filterConfig.getInitParameter(paramName);
                else if (paramName.equals(rewrittenHeaderParmName))
                    rewrittenUrlPrefixHeaderName = filterConfig.getInitParameter(paramName);
                else if (paramName.equals(jerseyPrefixParmName))
                    jerseyPrefix = filterConfig.getInitParameter(paramName);
                else
                    throw new ServletException("Unknown parameter name " + paramName);
            } catch (PatternSyntaxException ex) {
                throw new ServletException("Regexp error in param " + paramName + " value "
                    + filterConfig.getInitParameter(paramName) + ": " + ex.getMessage());
            }
        }
//        this.filterConfig = filterConfig;
        if (jerseyPrefix == null && jerseyPrefixPatterns.size() > 0)
            throw new ServletException("Missing parameter " + jerseyPrefixParmName);
    }

    @Override
    public void destroy() {
//        this.filterConfig = null;
    }

    @Override
    public void doFilter(ServletRequest _request, ServletResponse _response,
                         FilterChain chain) throws IOException, ServletException {
        log.fine("Jersey filter!");
        MyServletRequestWrapper req = new MyServletRequestWrapper((HttpServletRequest) _request);
        final String matchPath = req.getRequestURI().substring(req.getContextPath().length());
        log.log(Level.FINE, "Matching: {0}", matchPath);
        HttpServletResponse resp = (HttpServletResponse) _response;
        if (matchOneOf(staticPrefixPatterns, matchPath)) {
            log.fine("Jersey filter mathched static content");
            chain.doFilter(req, resp); // Goes to default servlet.
        } else if (matchPath.startsWith(jerseyPrefix)) {
            log.fine("Jersey filter mathched erroneous jersey content");
            throw new ServletException("There's an internal link " + jerseyPrefix + " directly in an incoming link. Leak.");
        } else if (matchOneOf(jerseyPrefixPatterns, matchPath)) {
            log.fine("Jersey filter matched jersey content");
            final String originalContextUrl = req.getScheme() + "://"
                + req.getServerName() + ":" + req.getServerPort()
                + req.getContextPath();
            final String jerseyTarget = jerseyPrefix + req.getServletPath();
            req.addHeader(originHeaderName, originalContextUrl);
            req.addHeader(rewrittenUrlPrefixHeaderName, originalContextUrl + jerseyPrefix);
            log.log(Level.FINE, "JerseyFilter dispatching to {0}", jerseyTarget);
            // Tays-2240 - urls with spaces don't work. URLEncode.encode(...) didn't either, 'cause we don't want to encode "/"
            // All other characters seem to work fine even if they have been decoded at this stage. Thus, this little hack:
            // Re-encode spaces to %20
            String encoded = jerseyTarget.replace(" ", "%20");
            RequestDispatcher requestDispatcher = req.getRequestDispatcher(encoded);
            requestDispatcher.forward(req, resp);
        } else {
            log.fine("Jersey filter defaulted to default servlet");
            chain.doFilter(req, resp); // Goes to default servlet.
        }
    }

    private boolean matchOneOf(List<Pattern> patterns, String s) {
        for (Pattern p : patterns)
            if (s.matches(p.pattern()))
                return true;
        return false;
    }
}

/**
 * Http servlet request wrapper that makes it possible for us to add a header
 * field to the http servlet request.<p>
 *
 * @author Jacob Lorensen, YouSee, 2012.
 *         <p/>
 *         http://www.tidytutorials.com/2009/11/adding-headers-to-requests-in-filters.html
 */
class MyServletRequestWrapper extends HttpServletRequestWrapper {
    private Map<String, String> headerMap;

    public MyServletRequestWrapper(HttpServletRequest request) {
        super(request);
        headerMap = new HashMap<String, String>();
    }

    public void addHeader(String name, String value) {
        headerMap.put(name, value);
    }

    @Override
    public Enumeration<String> getHeaderNames() {
        HttpServletRequest request = (HttpServletRequest) getRequest();
        List<String> list = new ArrayList<String>();
        for (Enumeration e = request.getHeaderNames(); e.hasMoreElements(); )
            list.add(e.nextElement().toString());
        for (String s : headerMap.keySet()) {
            list.add(s);
        }
        return Collections.enumeration(list);
    }

    @Override
    public String getHeader(String name) {
        Object value;
        if ((value = headerMap.get("" + name)) != null)
            return value.toString();
        else
            return ((HttpServletRequest) getRequest()).getHeader(name);

    }
}

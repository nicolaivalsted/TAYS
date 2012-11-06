package dk.yousee.randy.filters;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * This servlet filter rewrites outgoing http payload content replacing anything
 * substring that matches one header with a substring from another header.<p>
 * The header names are configurable, using the filter param <dl>
 * <dt>origin-url-prefix-header-name</dt> <dd>The original string to use as
 * replacement</dd> <dt>rewritten-url-prefix-header-name</dt> <dd>The substring
 * to search for and replace</dd> </dl> The idea is to use this filter in
 * conjunction with JerseyDispatchFilter which rewrites incoming URLs adding a
 * prefix to the path based on regular expressions.<p> This way eg. ReST Jersey
 * servlets may be placed in a sub-directory (under /rest eg) while still
 * appearing to lie directly in the root servlet name space, making a much nicer
 * and intuitive resource naming.
 *
 * @author Jacob Lorensen, YouSee, 2012-06-12
 * @see JerseyDispatchFilter
 */
public class UrlRewriter implements Filter {
    private final static Logger log = Logger.getLogger(UrlRewriter.class.getName());
    // Regexp to match urls with ":80" - this substring must be removed if explicitly present on incoming urls
    // See Tays-1253
    private final static Pattern p = Pattern.compile(".*(:80)([^0-9].*|$)");
    private final static String originalUrlPrefixHeaderNameParm = "origin-url-prefix-header-name";
    private final static String rewrittenUrlPrefixHeaderNameParm = "rewritten-url-prefix-header-name";
    private FilterConfig filterConfig;
    private String originalUrlPrefixHeaderName;
    private String rewrittenUrlPrefixHeaderName;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info("JSONUrlRewriter configuring");
        Enumeration<String> initParameterNames = filterConfig.getInitParameterNames();
        while (initParameterNames.hasMoreElements()) {
            String paramName = initParameterNames.nextElement();
            if (paramName.startsWith(originalUrlPrefixHeaderNameParm))
                originalUrlPrefixHeaderName = filterConfig.getInitParameter(paramName);
            else if (paramName.startsWith(rewrittenUrlPrefixHeaderNameParm))
                rewrittenUrlPrefixHeaderName = filterConfig.getInitParameter(paramName);
            else
                throw new ServletException("Unknown parameter name " + paramName);
        }
        if (originalUrlPrefixHeaderName == null)
            throw new ServletException("URL rewriter filter missing parameter " + originalUrlPrefixHeaderNameParm);
        if (rewrittenUrlPrefixHeaderName == null)
            throw new ServletException("URL rewriter filter missing parameter " + rewrittenUrlPrefixHeaderNameParm);
        this.filterConfig = filterConfig;
    }

    @Override
    public void destroy() {
        this.filterConfig = null;
    }

    @Override
    public void doFilter(ServletRequest _request, ServletResponse _response, FilterChain chain)
            throws IOException, ServletException {
        log.log(Level.FINE, "{0} filter executing", this.getClass().getName());
        HttpServletRequest req = (HttpServletRequest) _request;
        String originalUrl = req.getHeader(originalUrlPrefixHeaderName);
        String rewrittenUrl = req.getHeader(rewrittenUrlPrefixHeaderName);
        log.log(Level.FINER, "OriginalUrl: {0}; RewrittenUrl: {1}", new Object[]{originalUrl, rewrittenUrl});
        if (rewrittenUrl == null) {
            log.log(Level.FINE, "No header {0} not rewriting urls", rewrittenUrlPrefixHeaderName);
            chain.doFilter(_request, _response);
            return;
        }
        if (originalUrl == null) {
            log.log(Level.FINE, "No header {0} url rewriting will simply delete {1} url components", new Object[]{originalUrlPrefixHeaderName, rewrittenUrl});
            originalUrl = "";
        }
        // Tays-1253
        originalUrl = filter80(originalUrl);
        rewrittenUrl = filter80(rewrittenUrl);
        // End Tays-1253
        ByteArrayOutputStream os = new ByteArrayOutputStream(64 * 1024);
        ResponseWrapper wrapper = new ResponseWrapper((HttpServletResponse) _response, os);
        chain.doFilter(_request, wrapper);
        StringBuilder sb = new StringBuilder(os.toString(wrapper.getCharacterEncoding()));
        replaceAll(sb, rewrittenUrl, originalUrl);
        String result = sb.toString();
        byte[] resultBytes = result.getBytes(wrapper.getCharacterEncoding());
        _response.setContentLength(resultBytes.length);
        ServletOutputStream out = _response.getOutputStream();
        out.write(resultBytes);
        out.close();
    }

    private void replaceAll(StringBuilder sb, String search, String replace) {
        log.log(Level.FINER, "REPLACING {0} WITH {1}", new Object[]{search, replace});
        log.log(Level.FINEST, "Before replace: {0}", sb.toString());
        for (;;) {
            int i = sb.indexOf(search);
            if (i == -1) {
                log.log(Level.FINEST, "After replace: {0}", sb.toString());
                return;
            }
            sb.replace(i, i + search.length(), replace);
        }
    }

    // Tays-1253 incoming urls with explicit port :80 will not match outgoing urls 
    // where the port is not port 80. We'll remove the explicit port 80 from the input url in this case
    // This breaks, then, if the outgoing url DOES have an explicit port 80.
    /**
     * Replace ":80" in an incoming and rewritten url with the empty substring, working around Tays-1253.
     * @param url
     * @return modified url without :80 or original url if no ":80" match
     */
    private String filter80(String url) {
        Matcher m = p.matcher(url);
        if (m.matches()) { // port 80
            StringBuilder urlb = new StringBuilder();
            urlb.append(url.substring(0, m.start(1)));
            urlb.append(url.subSequence(m.end(1), url.length()));
            String res = urlb.toString();
            log.log(Level.FINER, "Port-80 exception. New url: {0}", res);
            return res;
        }
        return url;
    }
}
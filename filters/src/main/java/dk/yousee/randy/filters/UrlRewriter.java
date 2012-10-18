package dk.yousee.randy.filters;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * This servlet filter rewrites outgoing http payload content replacing anything substring
 * that matches one header with a substring from another header.<p>
 * The header names are configurable, using the filter param 
 * <dl>
 * <dt>origin-url-prefix-header-name</dt>
 * <dd>The original string to use as replacement</dd>
 * <dt>rewritten-url-prefix-header-name</dt>
 * <dd>The substring to search for and replace</dd>
 * </dl>
 * The idea is to use this filter in conjunction with JerseyDispatchFilter which rewrites incoming
 * URLs adding a prefix to the path based on regular expressions.<p>
 * This way eg. ReST Jersey servlets may be placed in a sub-directory (under /rest eg) while still appearing
 * to lie directly in the root servlet name space, making a much nicer and intuitive resource naming.
 * 
 * @author Jacob Lorensen, YouSee, 2012-06-12
 * @see JerseyDispatchFilter
 */
public class UrlRewriter implements Filter {
    private final static Logger log = Logger.getLogger(UrlRewriter.class.getName());
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
        log.log(Level.INFO, "{0} filter executing", this.getClass().getName());
        HttpServletRequest req = (HttpServletRequest) _request;
        String originalUrl = req.getHeader(originalUrlPrefixHeaderName);
        String rewrittenUrl = req.getHeader(rewrittenUrlPrefixHeaderName);
        if (rewrittenUrl == null) {
            log.log(Level.INFO, "No header {0} not rewriting urls", rewrittenUrlPrefixHeaderName);
            chain.doFilter(_request, _response);
            return;
        }
        if (originalUrl == null) {
            log.log(Level.INFO, "No header {0} url rewriting will simply delete {1} url components", new Object[]{originalUrlPrefixHeaderName, rewrittenUrl});
            originalUrl = "";
        }
        ByteArrayOutputStream os = new ByteArrayOutputStream(64 * 1024);
        ResponseWrapper wrapper = new ResponseWrapper((HttpServletResponse) _response, os);
//        wrapper.setHeader("Charset", "UTF-8");
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
        log.log(Level.INFO, "Rewriting: {0} REPLACING {1} WITH {2}", new Object[]{sb.toString(), search, replace});
        for (;;) {
            int i = sb.indexOf(search);
            if (i == -1)
                return;
            sb.replace(i, i + search.length(), replace);
        }
    }
}
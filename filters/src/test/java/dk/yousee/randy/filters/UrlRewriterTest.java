/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.yousee.randy.filters;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author jablo
 */
public class UrlRewriterTest {
    public UrlRewriterTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }
    private FilterConfig filterConfig;

    @Before
    public void setUp() {
        // Filterconfig key/value
        Map<String, String> cf = new HashMap();
        cf.put("origin-url-prefix-header-name", "Original-URL");
        cf.put("rewritten-url-prefix-header-name", "Rewritten-URL");
        filterConfig = new FilterConfigProxy(cf);
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of init method, of class UrlRewriter.
     */
    @Test
    public void testInit() throws Exception {
        System.out.println("init");
        UrlRewriter instance = new UrlRewriter();
        instance.init(filterConfig);
        // TODO review the generated test code and remove the default call to fail.
    }

    /**
     * Test of destroy method, of class UrlRewriter.
     */
    @Test
    public void testDestroy() throws ServletException {
        System.out.println("destroy");
        UrlRewriter instance = new UrlRewriter();
        instance.init(filterConfig);
        instance.destroy();
        // TODO review the generated test code and remove the default call to fail.
    }

    /**
     * Test of doFilter method, of class UrlRewriter.
     */
    @Test
    public void testDoFilter() throws Exception {
        System.out.println("doFilter");
        // Headers
        Map<String, String> hs = new HashMap();
        hs.put("Original-URL", "http://my.server.dk/myservice/api/myfunction");
        hs.put("Rewritten-URL", "http://my.server.dk/myservice/myfunction");
        ServletRequest _request = new ServletRequestProxy(hs);
        ServletOutputStreamProxy os = new ServletOutputStreamProxy();
        ServletResponse _response = new ServletResponseProxy(os);
        FilterChain chain = new FilterChainProxy();

        //
        UrlRewriter instance = new UrlRewriter();
        instance.init(filterConfig);
        instance.doFilter(_request, _response, chain);
        // TODO review the generated test code and remove the default call to fail.
        System.out.println(os.getByteOS().toString());
//        fail("The test case is a prototype.");
    }
}

class ServletRequestProxy implements HttpServletRequest {
    private Map<String, String> headers;

    public ServletRequestProxy(Map<String, String> headers) {
        this.headers = headers;
    }

    @Override
    public String getHeader(String string) {
        return headers.get(string);
    }

    @Override
    public Object getAttribute(String string) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Enumeration getAttributeNames() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getCharacterEncoding() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setCharacterEncoding(String string) throws UnsupportedEncodingException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int getContentLength() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getContentType() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getParameter(String string) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Enumeration getParameterNames() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String[] getParameterValues(String string) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Map getParameterMap() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getProtocol() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getScheme() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getServerName() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int getServerPort() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public BufferedReader getReader() throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getRemoteAddr() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getRemoteHost() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setAttribute(String string, Object o) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void removeAttribute(String string) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Locale getLocale() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Enumeration getLocales() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isSecure() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public RequestDispatcher getRequestDispatcher(String string) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getRealPath(String string) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getAuthType() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Cookie[] getCookies() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public long getDateHeader(String string) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Enumeration getHeaders(String string) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Enumeration getHeaderNames() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int getIntHeader(String string) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getMethod() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getPathInfo() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getPathTranslated() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getContextPath() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getQueryString() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getRemoteUser() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isUserInRole(String string) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Principal getUserPrincipal() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getRequestedSessionId() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getRequestURI() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public StringBuffer getRequestURL() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getServletPath() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public HttpSession getSession(boolean bln) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public HttpSession getSession() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isRequestedSessionIdValid() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isRequestedSessionIdFromCookie() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isRequestedSessionIdFromURL() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isRequestedSessionIdFromUrl() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}

class ServletResponseProxy implements HttpServletResponse {
    private ServletOutputStreamProxy os;

    public ServletResponseProxy(ServletOutputStreamProxy os) {
        this.os = os;
    }

    @Override
    public String getCharacterEncoding() {
        return "UTF-8";
    }

    @Override
    public void setContentLength(int i) {
    }

    @Override
    public ServletOutputStream getOutputStream() throws IOException {
        return os;
    }

    @Override
    public PrintWriter getWriter() throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setContentType(String string) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setBufferSize(int i) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int getBufferSize() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void flushBuffer() throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void resetBuffer() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean isCommitted() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void reset() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setLocale(Locale locale) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public Locale getLocale() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void addCookie(Cookie cookie) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public boolean containsHeader(String string) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String encodeURL(String string) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String encodeRedirectURL(String string) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String encodeUrl(String string) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String encodeRedirectUrl(String string) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void sendError(int i, String string) throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void sendError(int i) throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void sendRedirect(String string) throws IOException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setDateHeader(String string, long l) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void addDateHeader(String string, long l) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setHeader(String string, String string1) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void addHeader(String string, String string1) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setIntHeader(String string, int i) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void addIntHeader(String string, int i) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setStatus(int i) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void setStatus(int i, String string) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}

class FilterChainProxy implements FilterChain {
    @Override
    public void doFilter(ServletRequest sr, ServletResponse sr1) throws IOException, ServletException {
    }
}

class FilterConfigProxy implements FilterConfig {
    private final Map<String, String> cfg;

    public FilterConfigProxy(Map<String, String> cfg) {
        this.cfg = cfg;
    }

    @Override
    public String getFilterName() {
        return "ProxyFilter";
    }

    @Override
    public ServletContext getServletContext() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public String getInitParameter(String string) {
        return cfg.get(string);
    }

    @Override
    public Enumeration getInitParameterNames() {
        return new EnumerationProxy(cfg.keySet());
    }
}

class EnumerationProxy<T extends Iterable> implements Enumeration {
    private Iterator<T> it;

    public EnumerationProxy(T t) {
        it = t.iterator();
    }

    @Override
    public boolean hasMoreElements() {
        return it.hasNext();
    }

    @Override
    public Object nextElement() {
        return it.next();
    }
}

class ServletOutputStreamProxy extends ServletOutputStream {
    private ByteArrayOutputStream os = new ByteArrayOutputStream(64 * 1024);

    public ServletOutputStreamProxy() {
    }

    public ByteArrayOutputStream getByteOS() {
        return os;
    }

    @Override
    public void write(int i) throws IOException {
        os.write(i);
    }
}

package dk.yousee.smp.smpclient;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by IntelliJ IDEA.
 * User: aka
 * Date: 15/02/12
 * Time: 15.52
 * Context to access the webservice, just to make it simpler to pass url, user, pwd, proxy, timeout etc.
 */
public class UrlContext {

    private String url;
    private String proxyHost = null;
    private String proxyPort = null;
    private Integer connectionTimeout = null;
    private Integer operationTimeout=null;

    private String username = null;
    private String password = null;

    private URL url2;

    public UrlContext() {
    }

    public UrlContext(String url, String proxyHost, String proxyPort
        , Integer connectionTimeout,Integer operationTimeout, String username, String password) {
        setUrl(url);
        this.proxyHost = proxyHost;
        this.proxyPort = proxyPort;
        this.connectionTimeout = connectionTimeout;
        this.operationTimeout=operationTimeout;
        this.username = username;
        this.password = password;
    }

    public void setUrl(String url) {
        this.url = url;
        try {
            url2=new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            throw new IllegalArgumentException(String.format("Could not parse URL %s, got exception %s",url,e.getMessage()));
        }
    }

    public void setProxyHost(String proxyHost) {
        this.proxyHost = proxyHost;
    }

    public void setProxyPort(String proxyPort) {
        this.proxyPort = proxyPort;
    }

    public void setConnectionTimeout(Integer connectionTimeout) {
        this.connectionTimeout = connectionTimeout;
    }

    public void setOperationTimeout(Integer operationTimeout) {
        this.operationTimeout = operationTimeout;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getUrl() {
        return url;
    }

    public String getProxyHost() {
        return proxyHost;
    }

    public String getProxyPort() {
        return proxyPort;
    }

    public Integer getConnectionTimeout() {
        return connectionTimeout;
    }

    public Integer getOperationTimeout() {
        return operationTimeout;
    }


    public boolean isUsingProxy() {
        return proxyHost != null && !proxyHost.equals("null") && !proxyHost.equals("none");
    }

    public URL getUrl2() {
        return url2;
    }
}

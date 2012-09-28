package dk.yousee.randy.base;

/**
 * Created by IntelliJ IDEA.
 * User: aka
 * Date: 15/02/12
 * Time: 15.52
 * Context to access the webservice, just to make it simpler to pass  user, pwd, proxy, timeout etc.
 */
public class UrlContext {

    private String proxyHost = null;
    private String proxyPort = null;
    private Integer connectionTimeout = null;
    private Integer operationTimeout=null;

    public UrlContext() {
    }

    public UrlContext(String proxyHost, String proxyPort, Integer connectionTimeout, Integer operationTimeout) {
        this.proxyHost = proxyHost;
        this.proxyPort = proxyPort;
        this.connectionTimeout = connectionTimeout;
        this.operationTimeout = operationTimeout;
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

}

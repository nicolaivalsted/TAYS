package dk.yousee.randy.base;

import org.apache.http.HttpHost;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.HttpParams;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: aka
 * Date: 06/09/12
 * Time: 23.08
 * Connectors - that can be configured from Spring
 */
public abstract class AbstractConnector {


    /**
     * Default 1 seconds for connection timeout
     */
    public static final int DEFAULT_CONNECTION_TIMEOUT=1000;

    /**
     * Default 20 seconds to ask this question as max.
     */
    public static final int DEFAULT_OPERATION_TIMEOUT=20000;

    /**
     * Default no proxy host
     */
    public static final String DEFAULT_PROXY_HOST="none";
    /**
     * Default number of connections in pool
     */
    public static final int DEFAULT_MAX_TOTAL_CONNECTIONS=10;

    private ThreadSafeClientConnManager cm;

    protected ThreadSafeClientConnManager getCm() {
        return cm;
    }
    private Map<Integer,DefaultHttpClient> clients=new HashMap<Integer, DefaultHttpClient>();

    public void clearClients(){
        clients=new HashMap<Integer, DefaultHttpClient>();
    }

    private UrlContext urlContext;

    protected AbstractConnector() {
        cm = new ThreadSafeClientConnManager();
        cm.setDefaultMaxPerRoute(DEFAULT_MAX_TOTAL_CONNECTIONS);
        cm.setMaxTotal(DEFAULT_MAX_TOTAL_CONNECTIONS);
        urlContext=new UrlContext();
        urlContext.setProxyHost(DEFAULT_PROXY_HOST);
        urlContext.setConnectionTimeout(DEFAULT_CONNECTION_TIMEOUT);
        urlContext.setOperationTimeout(DEFAULT_OPERATION_TIMEOUT);
    }

    public String getProxyHost() {
        return urlContext.getProxyHost();
    }

    public void setProxyHost(String proxyHost) {
        urlContext.setProxyHost(proxyHost);
    }

    public String getProxyPort() {
        return urlContext.getProxyPort();
    }

    public void setProxyPort(String proxyPort) {
        urlContext.setProxyPort(proxyPort);
    }

    public void setConnectionTimeout(Integer connectionTimeout) {
        urlContext.setConnectionTimeout(connectionTimeout);
    }

    public void setOperationTimeout(Integer operationTimeout) {
        urlContext.setOperationTimeout(operationTimeout);
    }

    public Integer getConnectionTimeout() {
        return urlContext.getConnectionTimeout();
    }
    public Integer getOperationTimeout() {
        return urlContext.getOperationTimeout();
    }

    public UrlContext getUrlContext() {
        return urlContext;
    }

    public void setMaxTotalConnections(int maxTotalConnections){
        cm.setDefaultMaxPerRoute(maxTotalConnections);
        cm.setMaxTotal(maxTotalConnections);
    }
    public int getMaxTotalConnections(){
        return cm.getDefaultMaxPerRoute();
    }

    public HttpHost extractHttpHost(URL url) {
        HttpHost host;
        host = new HttpHost(url.getHost(), url.getPort(), url.getProtocol());
        return host;
    }

    public HttpHost extractProxy() {
        String ph = getProxyHost();
        if (ph != null && !"null".equals(ph) && !"none".equals(ph)) {
            HttpHost host;
            host = new HttpHost(ph, Integer.parseInt(getProxyPort()));
            return host;

        } else {
            return null;
        }
    }
    /**
     * Get a client that matches requirements.
     * <p>
     * We understand DefaultHttpClient as a "Facade" that can handle multiple concurrent request.
     * The facade is configured specific for each purpose.
     * Therefore it takes two facades to call the same service with different operation timeout.
     * And this is the reason that this class uses a map of DefaultHttpClient that is different by operation timeout.
     * </p>
     * @param operationTimeout requered timeout to be this value. Null means default operation timeout
     * @return a DefaultHttpClient that matches requirements.
     */
    public DefaultHttpClient getClient(Integer operationTimeout) {
        Integer ot=operationTimeout==null?getOperationTimeout():operationTimeout;
        DefaultHttpClient client= clients.get(ot);
        if(client==null){
            client=addClient(ot);
        }
        return client;
    }

    private synchronized DefaultHttpClient addClient(Integer operationTimeout) {
        DefaultHttpClient client= clients.get(operationTimeout); //maybe last thread just made me..
        if(client==null){
            client=createClient(operationTimeout);
            clients.put(operationTimeout,client);
        }
        return client;
    }

    private DefaultHttpClient createClient(Integer operationTimeout){
        HttpParams params = new BasicHttpParams();
        HttpHost proxy = extractProxy();
        if (proxy != null) {
            params.setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);
        }
        params.setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, getConnectionTimeout());
        params.setParameter(CoreConnectionPNames.SO_TIMEOUT, operationTimeout);
        return new DefaultHttpClient(getCm(),params);
    }

    public String connectInfo() {
        StringBuilder sb = new StringBuilder();
        sb.append("{");

        if (urlContext.isUsingProxy()) {
//            if (getProxyHost() != null && !"none".equals(getProxyHost()) && !"null".equals(getProxyHost())) {
            sb.append("\"proxyHost\":\"").append(getProxyHost()).append('"');
            sb.append(",\"proxyPort\":\"").append(getProxyPort()).append('"').append("\n");
        } else {
            sb.append("\"proxyHost\":\"none\"");
        }
        if (getConnectionTimeout() != 0) {
            sb.append(",\"defaultConnectionTimeout\":").append(getConnectionTimeout()).append("\n");
        }
        if (getOperationTimeout() != 0) {
            sb.append(",\"operationTimeout\":").append(getOperationTimeout()).append("\n");
        }
        sb.append("}");
        return sb.toString();
    }
    /**
     * Take down services, called by Spring at deactivation
     */
    public void destroy() {
        cm.shutdown();
    }

}

package dk.yousee.smp.smpclient;

import org.apache.http.HttpHost;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.HttpParams;
import sun.misc.BASE64Encoder;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: aka
 * Date: 09/04/12
 * Time: 14.46
 * Class that helps managing HttpClient
 */
public class SmpConnectorImpl implements SmpConnector {

    private UrlContext urlContext;
    private ThreadSafeClientConnManager cm;
    private Map<Integer,DefaultHttpClient> clients=new HashMap<Integer, DefaultHttpClient>();

    public SmpConnectorImpl() {
        urlContext=new UrlContext();
        urlContext.setProxyHost(DEFAULT_PROXY_HOST);
        urlContext.setConnectionTimeout(DEFAULT_CONNECTION_TIMEOUT);
        urlContext.setOperationTimeout(DEFAULT_OPERATION_TIMEOUT);
        cm = new ThreadSafeClientConnManager();
        cm.setDefaultMaxPerRoute(DEFAULT_MAX_TOTAL_CONNECTIONS);
        cm.setMaxTotal(DEFAULT_MAX_TOTAL_CONNECTIONS);
    }

    public String getUrl() {
        return urlContext.getUrl();
    }

    public void setUrl(String url) {
        urlContext.setUrl(url);
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

    public void setUsername(String username) {
        urlContext.setUsername(username);
    }
    public String getUsername() {
        return urlContext.getUsername();
    }

    public void setPassword(String password) {
        urlContext.setPassword(password);
    }
    public String getPassword() {
        return urlContext.getPassword();
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

    public void setMaxTotalConnections(int maxTotalConnections){
        cm.setDefaultMaxPerRoute(maxTotalConnections);
        cm.setMaxTotal(maxTotalConnections);
    }
    public int getMaxTotalConnections(){
        return cm.getDefaultMaxPerRoute();
    }

    public UrlContext getUrlContext() {
        return urlContext;
    }

    public HttpHost extractHttpHost() {
        HttpHost host;
        host = new HttpHost(urlContext.getUrl2().getHost(), urlContext.getUrl2().getPort(), urlContext.getUrl2().getProtocol());
        return host;
    }

    public HttpHost extractProxy() {
        String ph = urlContext.getProxyHost();
        if (ph != null && !"null".equals(ph) && !"none".equals(ph)) {
            HttpHost host;
            host = new HttpHost(ph, Integer.parseInt(urlContext.getProxyPort()));
            return host;

        } else {
            return null;
        }
    }

    public String extractUri() {
        return urlContext.getUrl2().getPath();
    }


    public String connectInfo() {
        StringBuilder sb = new StringBuilder();
        if (getProxyHost() != null && !"none".equals(getProxyHost()) && !"null".equals(getProxyHost())) {
            sb.append("proxyHost=").append(getProxyHost());
            sb.append(",proxyPort=").append(getProxyPort()).append("\n");
        }
        sb.append(" username=").append(getUsername());
        if (getConnectionTimeout() != 0) {
            sb.append(",defaultConnectionTimeout=").append(getConnectionTimeout()).append("\n");
        }
        if (getOperationTimeout() != 0) {
            sb.append(",operationTimeout=").append(getOperationTimeout()).append("\n");
        }
        return sb.toString();
    }

    public URL getURL() {
        return urlContext.getUrl2();
    }

    public String encodeBasic() {
        return encodeBasic(urlContext.getUsername(), urlContext.getPassword());
    }

    public String encodeBasic(String userName, String password) {

// code from
//        otherHeaders.append(HTTPConstants.HEADER_AUTHORIZATION)
//            .append(": Basic ")
//            .append(Base64.encode(tmpBuf.toString().getBytes()))
//            .append("\r\n");
        // stuff the Authorization request header
        byte[] encodedPassword = (userName + ":" + password).getBytes();
        BASE64Encoder encoder = new BASE64Encoder();
        String val;
        val = "Basic " + encoder.encode(encodedPassword);
        return val;
    }

//    public DefaultHttpClient getClient() {
//        return getClient(null);
//    }

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
        return new DefaultHttpClient(cm,params);
    }

    /**
     * Take down services, called by Spring at deactivation
     */
    public void destroy() {
        cm.shutdown();
    }
}

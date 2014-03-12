package dk.yousee.randy.base;

import org.apache.http.client.config.RequestConfig;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

/**
 * New pool instead of AbstractConnector
 *
 * @author m27236
 */
public class HttpPool {
    private PoolingHttpClientConnectionManager phccm;

    private int max_conn = 20;
    private int max_conn_route = 10;
    private final int SO_TIMEOUT = 2000;
    private final int CONNECTION_TIMEOUT = 5000;

    public void setMax_conn(int max_conn) {
        this.max_conn = max_conn;
    }

    public void setMax_conn_route(int max_conn_route) {
        this.max_conn_route = max_conn_route;
    }

    public HttpPool() {
    }

    public CloseableHttpClient getClient() {
        RequestConfig req = RequestConfig.custom().setSocketTimeout(SO_TIMEOUT).setConnectTimeout(CONNECTION_TIMEOUT).build();
        return HttpClientBuilder.create().setConnectionManager(phccm).setDefaultRequestConfig(req).build();
    }

    public CloseableHttpClient getClient(RequestConfig config) {
        return HttpClientBuilder.create().setConnectionManager(phccm).setDefaultRequestConfig(config).build();
    }

    public void initPool() {
        Registry registry = RegistryBuilder.create().register("http", PlainConnectionSocketFactory.getSocketFactory()).register("https", SSLConnectionSocketFactory.getSocketFactory()).build();
        phccm = new PoolingHttpClientConnectionManager(registry);
        phccm.setMaxTotal(max_conn);
        phccm.setDefaultMaxPerRoute(max_conn_route);
    }

    public void shutdown() {
        phccm.shutdown();
    }

    public PoolingHttpClientConnectionManager getPccm() {
        return phccm;
    }

    public void setPccm(PoolingHttpClientConnectionManager phccm) {
        this.phccm = phccm;
    }
}

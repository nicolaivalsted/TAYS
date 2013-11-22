package dk.yousee.randy.base;

import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.PoolingClientConnectionManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.HttpParams;

/**
 * New pool instead of AbstractConnector
 *
 * @author m27236
 */
public class HttpPool {
    private PoolingClientConnectionManager pccm;
    private int max_conn = 20;
    private int max_conn_route = 10;
    private final int SO_TIMEOUT = 2000;
    private final int CONNECTION_TIMEOUT = 5000;
    private final HttpParams params = new BasicHttpParams();

    //    private CredentialsProvider credsProvider;
    public void setMax_conn(int max_conn) {
        this.max_conn = max_conn;
    }

    public void setMax_conn_route(int max_conn_route) {
        this.max_conn_route = max_conn_route;
    }

    public HttpPool() {
    }

    public DefaultHttpClient getClient() {
        DefaultHttpClient res = new DefaultHttpClient(pccm);
        res.setParams(params);
        return res;
    }

    public DefaultHttpClient getClient(HttpParams params) {
        DefaultHttpClient res = new DefaultHttpClient(pccm);
        res.setParams(params);
        return res;
    }

    public void initPool() {       
        SchemeRegistry schemeRegistry = new SchemeRegistry();
        schemeRegistry.register(
                new Scheme("http", 80, PlainSocketFactory.getSocketFactory()));
        schemeRegistry.register(
                new Scheme("https", 443, SSLSocketFactory.getSocketFactory()));

        pccm = new PoolingClientConnectionManager(schemeRegistry);
        pccm.setMaxTotal(max_conn);
        pccm.setDefaultMaxPerRoute(max_conn_route);

        params.setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, CONNECTION_TIMEOUT);
        params.setParameter(CoreConnectionPNames.SO_TIMEOUT, SO_TIMEOUT);
    }

    public void shutdown() {
        pccm.shutdown();
    }

    public PoolingClientConnectionManager getPccm() {
        return pccm;
    }

    public void setPccm(PoolingClientConnectionManager pccm) {
        this.pccm = pccm;
    }
}

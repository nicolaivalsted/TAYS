package dk.yousee.randy.yspro;

import dk.yousee.randy.base.HttpPool;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.HttpParams;

/**
 * Created with IntelliJ IDEA. User: aka Date: 09/04/12 Time: 14.46 Class that
 * helps managing HttpClient
 */
public class ProStoreConnectorImpl {
    public static final String DEV_YSPRO_HOST = "http://ysprodev.yousee.dk";
    public static final String TEST_YSPRO_HOST = "https://ysprotest.yousee.dk";
    public static final String YSPRO_HOST = "http://yspro.yousee.dk";
    public static final String NEW_YSPRO_HOST = "https://yspro3.yousee.dk";
    private static final int DEFAULT_SO_TIMEOUT = 2000;
    private static final int DEFAULT_CONNECTION_TIMEOUT = 5000;
    private HttpParams params = new BasicHttpParams();
    private HttpPool pool; //set with singleton in spring

    public HttpPool getPool() {
        return pool;
    }

    public void setPool(HttpPool pool) {
        this.pool = pool;
    }

    private volatile String handleId;

    public ProStoreConnectorImpl() {
        params.setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, DEFAULT_CONNECTION_TIMEOUT);
        params.setParameter(CoreConnectionPNames.SO_TIMEOUT, DEFAULT_SO_TIMEOUT);
    }

    public DefaultHttpClient getClient() {
        return pool.getClient(params);
    }
    private String ysProHost;

    public String getYsProHost() {
        return ysProHost;
    }

    public void setYsProHost(String ysProHost) {
        this.ysProHost = ysProHost;
    }
    private String systemLogin = "RANDY";

    public String getSystemLogin() {
        return systemLogin;
    }

    public void setSystemLogin(String systemLogin) {
        this.systemLogin = systemLogin;
    }
    private String systemPassword;

    public String getSystemPassword() {
        return systemPassword;
    }

    public void setSystemPassword(String systemPassword) {
        this.systemPassword = systemPassword;
    }

    public String getHandleId() {
        return handleId;
    }

    public void setHandleId(String handleId) {
        this.handleId = handleId;
    }

    public void clearHandle() {
        this.handleId = null;
    }

    public void clearHandleId() {
        this.handleId = null;
    }

    public void disconnect() {
        clearHandleId();
    }

    public void setSO_TIMEOUT(int SO_TIMEOUT) {
        params.setParameter(CoreConnectionPNames.SO_TIMEOUT, SO_TIMEOUT);
    }

    public void setCONNECTION_TIMEOUT(int CONNECTION_TIMEOUT) {
        params.setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, CONNECTION_TIMEOUT);
    }
}

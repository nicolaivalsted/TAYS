package dk.yousee.randy.yspro;

import dk.yousee.randy.base.HttpPool;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.CloseableHttpClient;

/**
 * Created with IntelliJ IDEA. User: aka Date: 09/04/12 Time: 14.46 Class that
 * helps managing HttpClient
 */
public class ProStoreConnectorImpl {
    public static final String DEV_YSPRO_HOST = "http://ysprodev.yousee.dk";
    public static final String TEST_YSPRO_HOST = "https://ysprotest.yousee.dk";
    public static final String YSPRO_HOST = "http://yspro.yousee.dk";
    public static final String NEW_YSPRO_HOST = "https://yspro3.yousee.dk";
    private HttpPool pool; //set with singleton in spring
    
    private final RequestConfig req;
    
    public HttpPool getPool() {
        return pool;
    }

    public void setPool(HttpPool pool) {
        this.pool = pool;
    }

    private volatile String handleId;
    
    public ProStoreConnectorImpl(int socketTimeout, int connTimeout) {
        req = RequestConfig.custom().setSocketTimeout(socketTimeout).setConnectTimeout(connTimeout).build();
    }

    public CloseableHttpClient getClient() {
        return pool.getClient(req);
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
}

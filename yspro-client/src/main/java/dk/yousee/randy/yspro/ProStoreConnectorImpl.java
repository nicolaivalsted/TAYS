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
    public static final String TEST_YSPRO_HOST = "http://ysprotest.yousee.dk";
    public static final String YSPRO_HOST = "http://yspro.yousee.dk";
    public static final String NEW_YSPRO_HOST = "https://yspro3.yousee.dk";
    private int SO_TIMEOUT = 1000;
    private int CONNECTION_TIMEOUT = 3000;
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
        params.setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, CONNECTION_TIMEOUT);
        params.setParameter(CoreConnectionPNames.SO_TIMEOUT, SO_TIMEOUT);
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

    /**
     * When on Koncern net it is possible to use these proxy settings
     */
//    private String alternativeProxyHost = null;
//    private String primaryProxyHost = null;
//
//    public String getAlternativeProxyHost() {
//        return alternativeProxyHost;
//    }
//
//    public void setAlternativeProxyHost(String alternativeProxyHost) {
//        this.alternativeProxyHost = alternativeProxyHost;
//        primaryProxyHost=getProxyHost();
//    }
//
//    private String alternativeProxyPort = null;
//    private String primaryProxyPort = null;
//    /**
//     * When on Koncern net it is possible to use these proxy settings
//     */
//    public String getAlternativeProxyPort() {
//        return alternativeProxyPort;
//    }
//
//    public void setAlternativeProxyPort(String alternativeProxyPort) {
//        this.alternativeProxyPort = alternativeProxyPort;
//        primaryProxyPort=getProxyPort();
//    }
//    public boolean hasAlternativeProxyHost(){
//        return !DEFAULT_PROXY_HOST.equals(alternativeProxyHost) && alternativeProxyHost!=null;
//    }
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

    @Deprecated
    public String connectInfo() {
        StringBuilder sb = new StringBuilder();
        sb.setLength(sb.length() - 1);
        sb.append(",\"ysProHost\":").append('"').append(getYsProHost()).append('"');
        sb.append(",\"systemLogin\":").append('"').append(getSystemLogin()).append('"');
        sb.append(",\"systemPasswordHash\":").append('"').append(getSystemPassword().hashCode()).append('"');
        if (handleId != null)
            sb.append(",\"handle\":").append('"').append(handleId).append('"');
        sb.append("}");
        return sb.toString();
    }

    public void setSO_TIMEOUT(int SO_TIMEOUT) {
        this.SO_TIMEOUT = SO_TIMEOUT;
    }

    public void setCONNECTION_TIMEOUT(int CONNECTION_TIMEOUT) {
        this.CONNECTION_TIMEOUT = CONNECTION_TIMEOUT;
    }
}

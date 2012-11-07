package dk.yousee.randy.yspro;

import dk.yousee.randy.base.AbstractConnector;

/**
 * Created with IntelliJ IDEA.
 * User: aka
 * Date: 09/04/12
 * Time: 14.46
 * Class that helps managing HttpClient
 */
public class ProStoreConnectorImpl extends AbstractConnector {

    public static final String DEV_YSPRO_HOST="http://ysprodev.yousee.dk";
    public static final String TEST_YSPRO_HOST="http://ysprotest.yousee.dk";
    public static final String YSPRO_HOST="http://yspro.yousee.dk";

    public ProStoreConnectorImpl() {
        super();
    }

    private String ysProHost;

    public String getYsProHost() {
        return ysProHost;
    }

    public void setYsProHost(String ysProHost) {
        this.ysProHost = ysProHost;
    }

    private String systemLogin="RANDY";

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

    public void clearClients(){
        super.clearClients();
        setProxyHost(primaryProxyHost);
        setProxyPort(primaryProxyPort);
    }

    /**
     * When on Koncern net it is possible to use these proxy settings
     */
    private String alternativeProxyHost = null;
    private String primaryProxyHost = null;

    public String getAlternativeProxyHost() {
        return alternativeProxyHost;
    }

    public void setAlternativeProxyHost(String alternativeProxyHost) {
        this.alternativeProxyHost = alternativeProxyHost;
        primaryProxyHost=getProxyHost();
    }

    private String alternativeProxyPort = null;
    private String primaryProxyPort = null;
    /**
     * When on Koncern net it is possible to use these proxy settings
     */
    public String getAlternativeProxyPort() {
        return alternativeProxyPort;
    }

    public void setAlternativeProxyPort(String alternativeProxyPort) {
        this.alternativeProxyPort = alternativeProxyPort;
        primaryProxyPort=getProxyPort();
    }
    public boolean hasAlternativeProxyHost(){
        return !DEFAULT_PROXY_HOST.equals(alternativeProxyHost) && alternativeProxyHost!=null;
    }

    public String connectInfo() {
        StringBuilder sb = new StringBuilder();
        sb.append(super.connectInfo());
        sb.setLength(sb.length() - 1);
        sb.append(",\"ysProHost\":").append('"').append(getYsProHost()).append('"');
        sb.append(",\"systemLogin\":").append('"').append(getSystemLogin()).append('"');
        sb.append(",\"systemPasswordHash\":").append('"').append(getSystemPassword().hashCode()).append('"');
        sb.append("}");
        return sb.toString();
    }
}


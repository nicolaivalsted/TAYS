package dk.yousee.smp.smpclient;

import java.net.MalformedURLException;
import java.net.URL;

import org.apache.http.HttpHost;

import dk.yousee.randy.base.AbstractConnector;
import sun.misc.BASE64Encoder;

/**
 * Created with IntelliJ IDEA.
 * User: aka
 * Date: 09/04/12
 * Time: 14.46
 * Class that helps managing HttpClient
 */
public class SmpConnectorImpl extends AbstractConnector  {

    /**
     * Note that K_* is from Koncern net. These are configured from servers like Kenfig
     * <br/>
     * T_* is from Technical net. Development, CI and TEST and PROD has moved here (except for Classic PM)
     */

    public static final String T_NET_UDV_SMP_HOST="http://194.239.10.213:26500";
    public static final String T_NET_QA_SMP_HOST="http://t-smpweb.yousee.idk:41203";
    public static final String T_NET_SMP_HOST="http://p-smpweb.yousee.idk:44001";

    public static final String K_QA_SMP_HOST="http://194.239.10.197:41203";
    public static final String K_SMP_HOST="http://10.114.24.120:44001";


    /**
     * Default 100 seconds to ask this question as max.
     */
    public static final int DEFAULT_OPERATION_TIMEOUT=100000;

    public SmpConnectorImpl() {
        super();
        setOperationTimeout(DEFAULT_OPERATION_TIMEOUT);
    }
    private String url;
    private URL url2;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url=url;
        try {
            url2=new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
            throw new IllegalArgumentException(String.format("Could not parse URL %s, got exception %s",url,e.getMessage()));
        }
    }

    private String smpHost;

    public String getSmpHost() {
        return smpHost;
    }

    public void setSmpHost(String smpHost) {
        this.smpHost=smpHost;
        setUrl(smpHost+"/SmpXmlOrderApi/xmlorder");
    }


    private String username;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    private String password;

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public HttpHost extractHttpHost() {
        HttpHost host;
        host = new HttpHost(url2.getHost(), url2.getPort(), url2.getProtocol());
        return host;
    }


    public String extractUri() {
        return url2.getPath();
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
        return url2;
    }

    public String encodeBasic() {
        return encodeBasic(getUsername(), getPassword());
    }

    public String encodeBasic(String userName, String password) {
        // stuff the Authorization request header
        byte[] encodedPassword = (userName + ":" + password).getBytes();
        BASE64Encoder encoder = new BASE64Encoder();
        String val;
        val = "Basic " + encoder.encode(encodedPassword);
        return val;
    }

}

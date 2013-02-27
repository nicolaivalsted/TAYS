package dk.yousee.smp.functions;

import dk.yousee.smp.order.model.AliveService;
import dk.yousee.smp.smpclient.SmpConnectorImpl;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;


/**
 * Created by IDEA.
 * User: m14857
 * Date: Jul 26, 2011
 * Time: 11:17:10 AM
 * On board Alive test
 */
public class OnboardAliveServiceImpl implements AliveService {
    private static final Logger logger = Logger.getLogger(OnboardAliveServiceImpl.class);

    private SmpConnectorImpl smpConnector;
    private int operationTimeout=OPERATION_TIMEOUT;

    public Integer getConnectionTimeout() {
        return smpConnector.getConnectionTimeout();
    }

    public void setOperationTimeout(Integer operationTimeout) {
        this.operationTimeout = operationTimeout;
    }

    public Integer getOperationTimeout() {
        return operationTimeout;
    }

    public void setConnector(SmpConnectorImpl smpConnector) {
        this.smpConnector = smpConnector;
    }

    SmpConnectorImpl getConnector() {
        return smpConnector;
    }

    public String ping() {
        try {
            return ping2();
        } catch (Exception e){
            return null;
        }
    }

    public String ping2() throws Exception {
        try {
            return readWsdl();
        } catch (Exception e) {
            logger.error(String.format("ping of %s failed, with errorMessage: %s", getConnector().getURL(),e.getMessage()));
            throw e;
        }
    }


    String readWsdl() throws Exception {
        DefaultHttpClient client = getConnector().getClient(getOperationTimeout());
        HttpEntity entity=null;
        try {

//TODO: Respect connection timeout current set in getClient() above
//            client.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, getConnectionTimeout());

            HttpHost target = getConnector().extractHttpHost();
            HttpGet req = new HttpGet(getConnector().extractUri() + "?WSDL"); // was "/"
//            logger.debug("executing request to " + target + " via " + proxy);
            HttpResponse rsp = client.execute(target, req);
            StatusLine statusLine = rsp.getStatusLine();
            if (statusLine.getStatusCode() == HttpStatus.SC_OK) {
                entity = rsp.getEntity();
                return String.format("Status %s read %s bytes", statusLine.getReasonPhrase(), entity.getContentLength());
            } else {
                EntityUtils.consume(rsp.getEntity());
                throw new Exception(String.format("Failed to read WSDL for service: %s. Got HTTP status %s %s"
                    ,smpConnector.connectInfo(),statusLine.getStatusCode(),statusLine.getReasonPhrase()));
            }
        } finally {
            EntityUtils.consume(entity);
        }
    }
}

package dk.yousee.randy.yspro;

import dk.yousee.randy.base.AbstractClient;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created with IntelliJ IDEA.
 * User: aka
 * Date: 06/09/12
 * Time: 23.45
 * Client to access prostore
 */
public class ProStoreClient extends AbstractClient<ProStoreConnectorImpl> {


    private String handleId;


    public String getHandleId() {
        return handleId;
    }

    public void setHandleId(String handleId) {
        this.handleId = handleId;
    }
    public void clearHandleId() {
        this.handleId = null;
    }

    public void disconnect(){
        clearHandleId();
        getConnector().clearClients();
    }
    // http://ysprodev.yousee.dk/GetHandle.php?SystemLogin=RANDY&SystemPassword=We4rAndy

    private URL generateHandleUrl() throws MalformedURLException {
        return new URL(String.format("%s/GetHandle.php?SystemLogin=%s&SystemPassword=%s"
            , getConnector().getYsProHost(), getConnector().getSystemLogin(), getConnector().getSystemPassword()));
    }

    // Timeout for HandleID er 300 minutter for RANDY
    public String fetchHandle(int level) throws Exception {
        HttpUriRequest hur;
        URL url = generateHandleUrl();

        hur = new HttpGet(url.toString());
        HttpEntity entity = null;
        try {
            entity = talk2service(hur);
            return readResponse(entity);
        } catch (java.net.UnknownHostException e) {
            HttpHost proxy = getConnector().extractProxy();
            if (proxy != null || level == 1 || !getConnector().hasAlternativeProxyHost()) {
                String message = String.format("Tried to fetch handle on URL %s, got unknownHostException %s,", url.toString(), e.getMessage());
                throw new Exception(message);
            } else {
                getConnector().clearClients();
                getConnector().setProxyHost(getConnector().getAlternativeProxyHost());
                getConnector().setProxyPort(getConnector().getAlternativeProxyPort());
                return fetchHandle(1);
            }
        } finally {
            if (entity != null) EntityUtils.consume(entity); // Make sure the connection can go back to pool
        }
    }

    private void ensureHandle() throws Exception {
        if (handleId == null) {
            handleId = fetchHandle(0);
        }
    }

    /**
     *
     * @param customer de 9 cifre
     * @return json dokument. Der er en liste af "Products" med de - for produktet (7000) definerede - properties
     * @throws Exception
     */
    public ProStoreResponse findOttEngagement(String customer) throws Exception {
        ensureHandle();
        URL url = new URL(String.format("%s/GetOttEngagement.php?HandleID=%s&CustomerNumber=%s&json=1"
            , getConnector().getYsProHost(), handleId, customer));
        return new ProStoreResponse(callInner(url));
    }
    //GetEngagement.php
    public ProStoreResponse findEngagement(String customer) throws Exception {
        ensureHandle();
        URL url = new URL(String.format("%s/GetEngagement.php?HandleID=%s&CustomerNumber=%s"
            , getConnector().getYsProHost(), handleId, customer));
        return new ProStoreResponse(callInner(url));
    }
    
    public ProStoreResponse findEngagementFromProductId(String customer, String productId) throws Exception{
        ensureHandle();
        URL url = new URL(String.format("%s/GetEngagement.php?HandleID=%s&CustomerNumber=%s&ProductID=%s"
            , getConnector().getYsProHost(), handleId, customer, productId));
        return new ProStoreResponse(callInner(url));
    }

    //http://ysprodev.yousee.dk/GetUserInfo.php?HandleID=0nQU9YUs0f4u88czvWCkB2587OL2CX&CustomerNumber=607777777&xml=1
    public ProStoreResponse findUserInfo(String userID) throws Exception {
        ensureHandle();
        URL url = new URL(String.format("%s/GetUserInfo.php?HandleID=%s&UserID=%s&xml=1"
            , getConnector().getYsProHost(), handleId, userID));
        return new ProStoreResponse(callInner(url));
    }
    
    //http://ysprodev.yousee.dk/GetEngagementByValue.php?HandleID=6sz06U5lxwoA85yZJ3239V1CzM5k3G&ProductID=6900&DataName=Device_Mac&Value=12:34:56:78:90:AB
    public ProStoreResponse findCustomersFromOTTmacStb(String mac) throws Exception {
        ensureHandle();
        URL url = new URL(String.format("%s/GetEngagementByValue.php?HandleID=%s&ProductID=6900&DataName=Device_Mac&Value=%s"
            , getConnector().getYsProHost(), handleId, mac));
        return new ProStoreResponse(callInner(url));
    }

//    /**
//     *
//     * @param customer 9 digits
//     * @return conplex string
//     * @throws Exception on access
//     * @deprecated use #findEngagement(String customer)
//     */
//    public String findCustomerProduct(String customer) throws Exception {
//        ensureHandle();
//        URL url = new URL(String.format("%s/GetUserProductData.php?HandleID=%s&CustomerNumber=%s&json=1"
//            , getConnector().getYsProHost(), handleId, customer));
//        return callInner(url);
//    }

    private String callInner(URL url) throws Exception {
        HttpUriRequest hur = new HttpGet(url.toString());
        HttpEntity entity = null;
        try {
            entity = talk2service(hur);
            return readResponse(entity);
        } finally {
            if (entity != null) EntityUtils.consume(entity); // Make sure the connection can go back to pool
        }
    }

    private HttpEntity talk2service(HttpUriRequest hur) throws Exception {
        DefaultHttpClient client = getConnector().getClient(getOperationTimeout());
        HttpEntity entity;
        String errorMessage;
        int httpStatus;
        try {
            HttpResponse rsp = client.execute(hur);
            httpStatus = rsp.getStatusLine().getStatusCode();
            entity = rsp.getEntity();
            if (httpStatus == HttpStatus.SC_OK) {
                errorMessage = null;
            } else if (httpStatus == HttpStatus.SC_NOT_ACCEPTABLE) {
                errorMessage = rsp.getStatusLine().getReasonPhrase();
            } else if (httpStatus == HttpStatus.SC_BAD_REQUEST) {
                errorMessage = rsp.getStatusLine().getReasonPhrase();
            } else if (httpStatus == HttpStatus.SC_INTERNAL_SERVER_ERROR) {
                errorMessage = rsp.getStatusLine().getReasonPhrase();
                String msg = EntityUtils.toString(entity, "UTF-8");
                if (msg != null) {
                    errorMessage = errorMessage + " msg:" + msg;
                }
            } else {
                errorMessage = String.format("Status not handled, %s", rsp.getStatusLine().getReasonPhrase());
            }
        } catch (java.net.UnknownHostException e) {
            throw e;
        } catch (Throwable e) {
            String message = String.format("could not execute get, got error: %s,", e);
            throw new Exception(message);
        }
        if (errorMessage != null) {
            throw new Exception(String.format("status:%s ,phrase:%s", httpStatus, errorMessage));
        }
        return entity;
    }


// http://ysprodev.yousee.dk/AssignProduct.php?HandleID=X71vQm8ZLZ80916omB0I54ZfV48quC&CustomerNumber=607777777
// &Products=[
// {"ProductID":7000
// ,"Description":"My Test..."
// ,"BusinessPosition":"0109"
// ,"ServiceItem":"qos1"
// ,"StaloneID":"123456789"
// ,"From":"2012-09-03 18:30:00"
// ,"To":"2012-10-03 17:59:59"
// ,"UUID":"43"
// }
// ,
// {"ProductID":7000
// ,"Description":"Another test..."
// ,"BusinessPosition":"1023"
// ,"From":"2012-09-03 18:00:00"
// ,"To":"2012-10-03 17:59:59"
// ,"UUID": "d179b909-cd29-4195-8756-ab75bbc8cd89"
// }
// ]
    public URL generateUpdateUrl(String customer, String json) throws MalformedURLException, UnsupportedEncodingException {
        String encoded = URLEncoder.encode(json, "UTF-8");
        return new URL(String.format("%s/AssignProduct.php?HandleID=%s&CustomerNumber=%s&Products=%s"
            , getConnector().getYsProHost(), handleId, customer, encoded));
    }

    public ProStoreResponse assignProduct(String customer, String json) throws Exception {
        ensureHandle();
        HttpPost post;
        URL href=generateUpdateUrl(customer,json);
        post = new HttpPost(href.toString());
        HttpEntity entity = null;
        try {
            entity = talk2service(post);
// read response & parse body
            return new ProStoreResponse(readResponse(entity));
        } finally {
            if (entity != null) EntityUtils.consume(entity); // Make sure the connection can go back to pool
        }
    }

    /**
     * http://ysprodev.yousee.dk/RemoveEngagement.php
     * ?HandleID=MM91dRInu12r9c8D3oMb9RJ8oiH900&CustomerNumber=607777777&ProductID=6900
     *
     * Vær’sgo J
     *
     * Den vil forblive udokumenteret og det er frivilligt om du angiver UserID eller CustomerNumber,
     * men en af dem skal være der. Det er også frivilligt at angive ProductID
     * , men jeg ville nu gøre det.
     * Hvis du ikke angiver ProductID slettes ALLE
     * tilknyttede produkter og egenskaber for tilknytningerne fra kunden.
     * Husk at bruge dit egent HandleID, CustomerNumber og ProductID, ovenstående er blot et eksempel!
     * OTT produktet har ProductID 7000.
     * .. Allan
     */
    public ProStoreResponse removeEngagement(String customer,YsProProduct product) throws Exception {
        ensureHandle();
        URL url = new URL(String.format("%s/RemoveEngagement.php?HandleID=%s&CustomerNumber=%s&ProductID=%s"
            , getConnector().getYsProHost(), handleId, customer,product));
        return new ProStoreResponse(callInner(url));
    }
}

package dk.yousee.randy.yspro;

import com.google.gson.JsonSyntaxException;
import dk.yousee.randy.base.AbstractConnector;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.util.EntityUtils;

/**
 *
 * @author m27236
 */
public class YsProApi {
    private static final Logger LOG = Logger.getLogger(YsProApi.class.getName());

    private static String handleId;

    private static int timeout = 2500;

    public static void setTimeout(int timeout) {
        YsProApi.timeout = timeout;
    }

    private ProStoreConnectorImpl client;

    public void setClient(ProStoreConnectorImpl client) {
        this.client = client;
    }
    
    public YsProApi() {
    }
    
    public void shutDown(){
        try {
            freeHandle();
        } catch (YsProException ex) {
            LOG.log(Level.SEVERE, ex.getMessage(), ex);
        } catch (URISyntaxException ex) {
            LOG.log(Level.SEVERE, ex.getMessage(), ex);
        }
    }

    private String getHandle() throws YsProException {
        URI uri;
        try {
            uri = new URI(String.format("%s/GetHandle.php?SystemLogin=%s&SystemPassword=%s", client.getYsProHost(), client.getSystemLogin(), client.getSystemPassword()));
        } catch (URISyntaxException ex) {
            throw new YsProException("URI syntax in getHandle");
        }

        return excuteGet(uri);
    }

    public String freeHandle() throws YsProException, URISyntaxException {
        URI uri = new URI(String.format("%s/FreeHandle.php?HandleID=%s", client.getYsProHost(), handleId));

        String res = excuteGet(uri);
        handleId = null;
        return res;
    }

    private String excuteGet(URI uri) throws YsProException {
        HttpEntity entity = null;
        try {
            //URI uri = new URI(String.format("%s/GetEngagementByValue.php?HandleID=%s&ProductID=6900&DataName=Device_Mac&Value=%s"
            // , ysProHost, handleId, mac));
            HttpGet get = new HttpGet(uri);
            LOG.log(Level.INFO, "Trying execute url: {0}", uri);
            HttpResponse response = client.getClient(timeout).execute(get);
            int statusSode = response.getStatusLine().getStatusCode();
            LOG.log(Level.INFO, "Executed url with status: {0}", statusSode);

            entity = response.getEntity();

            if (statusSode == HttpStatus.SC_OK) {
                return EntityUtils.toString(entity, "UTF-8");
            } else {
                LOG.log(Level.INFO, "YsPro backend fail! {0}", statusSode);
                throw new YsProException(new YsProErrorVO(statusSode, EntityUtils.toString(entity, "UTF-8")));
            }
        } catch (IOException ioe) {
            LOG.log(Level.SEVERE, ioe.getMessage());
            throw new YsProException("Yspro IOexception error", ioe);
        } finally {
            if (entity != null) {
                try {
                    EntityUtils.consume(entity);
                } catch (IOException ex) {
                    LOG.log(Level.SEVERE, ex.getMessage(), ex);
                }
            }
        }
    }

    private String excutePost(URI uri) throws YsProException {
        HttpEntity entity = null;
        try {
            //URI uri = new URI(String.format("%s/GetEngagementByValue.php?HandleID=%s&ProductID=6900&DataName=Device_Mac&Value=%s"
            // , ysProHost, handleId, mac));
            HttpPost get = new HttpPost(uri);
            LOG.log(Level.INFO, "Trying execute url: {0}", uri);
            HttpResponse response = client.getClient(timeout).execute(get);
            int statusSode = response.getStatusLine().getStatusCode();
            LOG.log(Level.INFO, "Executed url with status: {0}", statusSode);

            entity = response.getEntity();

            if (statusSode == HttpStatus.SC_OK) {
                return EntityUtils.toString(entity, "UTF-8");
            } else {
                LOG.log(Level.INFO, "YsPro backend fail! {0}", statusSode);
                throw new YsProException(new YsProErrorVO(statusSode, EntityUtils.toString(entity, "UTF-8")));
            }
        } catch (IOException ioe) {
            LOG.log(Level.SEVERE, ioe.getMessage());
            throw new YsProException("Yspro IOexception error", ioe);
        } finally {
            if (entity != null) {
                try {
                    EntityUtils.consume(entity);
                } catch (IOException ex) {
                    LOG.log(Level.SEVERE, ex.getMessage(), ex);
                }
            }
        }
    }

    private void ensureHandle() throws YsProException {
        if (handleId == null) {
            handleId = getHandle();
        }
    }

    /**
     *
     * @param customer de 9 cifre
     * @return json dokument. Der er en liste af "Products" med de - for
     * produktet (7000) definerede - properties
     * @throws YsProException
     */
    public ProStoreResponse findOttEngagement(String customer) throws YsProException, JsonSyntaxException, URISyntaxException {
        ensureHandle();
        URI url = new URI(String.format("%s/GetOttEngagement.php?HandleID=%s&CustomerNumber=%s&json=1", client.getYsProHost(), handleId, customer));
        ProStoreResponse psr = new ProStoreResponse(excuteGet(url));

        return psr;
    }
    //GetEngagement.php

    public ProStoreResponse findEngagement(String customer) throws YsProException, JsonSyntaxException, URISyntaxException {
        ensureHandle();
        URI url = new URI(String.format("%s/GetEngagement.php?HandleID=%s&CustomerNumber=%s", client.getYsProHost(), handleId, customer));
        return new ProStoreResponse(excuteGet(url));
    }

    public ProStoreResponse findEngagementFromProductId(String customer, String productId) throws YsProException, JsonSyntaxException, URISyntaxException {
        ensureHandle();
        URI url = new URI(String.format("%s/GetEngagement.php?HandleID=%s&CustomerNumber=%s&ProductID=%s", client.getYsProHost(), handleId, customer, productId));
        return new ProStoreResponse(excuteGet(url));
    }

    //http://ysprodev.yousee.dk/GetUserInfo.php?HandleID=0nQU9YUs0f4u88czvWCkB2587OL2CX&CustomerNumber=607777777&xml=1
    public UserInfo findUserInfo(String userID) throws YsProException, JsonSyntaxException, URISyntaxException {
        ensureHandle();
        URI uri = new URI(String.format("%s/GetUserInfo.php?HandleID=%s&UserID=%s&xml=1", client.getYsProHost(), handleId, userID));
        String st = excuteGet(uri);
        return new UserInfo(UserInfo.DataFormat.xml, st);
    }

    //http://ysprodev.yousee.dk/GetEngagementByValue.php?HandleID=6sz06U5lxwoA85yZJ3239V1CzM5k3G&ProductID=6900&DataName=Device_Mac&Value=12:34:56:78:90:AB
    public ProStoreResponse findCustomersFromOTTmacStb(String mac) throws YsProException, JsonSyntaxException, URISyntaxException {
        ensureHandle();
        URI url = new URI(String.format("%s/GetEngagementByValue.php?HandleID=%s&ProductID=6900&DataName=Device_Mac&Value=%s", client.getYsProHost(), handleId, mac));
        return new ProStoreResponse(excuteGet(url));
    }

    public URI generateUpdateUrl(String customer, String json) throws UnsupportedEncodingException, YsProException, JsonSyntaxException, URISyntaxException {
        String encoded = URLEncoder.encode(json, "UTF-8");
        return new URI(String.format("%s/AssignProduct.php?HandleID=%s&CustomerNumber=%s&Products=%s", client.getYsProHost(), handleId, customer, encoded));
    }

    public ProStoreResponse assignProduct(String customer, String json) throws Exception {
        ensureHandle();
        URI href = generateUpdateUrl(customer, json);
        return new ProStoreResponse(excutePost(href));
    }

    /**
     * http://ysprodev.yousee.dk/RemoveEngagement.php
     * ?HandleID=MM91dRInu12r9c8D3oMb9RJ8oiH900&CustomerNumber=607777777&ProductID=6900
     *
     * Vær’sgo J
     *
     * Den vil forblive udokumenteret og det er frivilligt om du angiver UserID
     * eller CustomerNumber, men en af dem skal være der. Det er også frivilligt
     * at angive ProductID , men jeg ville nu gøre det. Hvis du ikke angiver
     * ProductID slettes ALLE tilknyttede produkter og egenskaber for
     * tilknytningerne fra kunden. Husk at bruge dit egent HandleID,
     * CustomerNumber og ProductID, ovenstående er blot et eksempel! OTT
     * produktet har ProductID 7000. .. Allan
     */
    private ProStoreResponse removeEngagement(String customer, YsProProduct product) throws YsProException, JsonSyntaxException, URISyntaxException {
        ensureHandle();
        URI url = new URI(String.format("%s/RemoveEngagement.php?HandleID=%s&CustomerNumber=%s&ProductID=%s", client.getYsProHost(), handleId, customer, product));
        return new ProStoreResponse(excuteGet(url));
    }
}

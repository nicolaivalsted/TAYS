package dk.yousee.randy.yspro;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
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
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.util.EntityUtils;

/**
 *
 * @author m27236
 */
public class YsProApi {
    private static final Logger LOG = Logger.getLogger(YsProApi.class.getName());
    private int timeout = 2500;

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }
    private volatile ProStoreConnectorImpl client;

    public void setClient(ProStoreConnectorImpl client) {
        this.client = client;
    }

    public YsProApi() {
    }

    private String getHandle() throws YsProException {
        URI uri;
        try {
            uri = new URI(String.format("%s/GetHandle.php?SystemLogin=%s&SystemPassword=%s", client.getYsProHost(), client.getSystemLogin(), client.getSystemPassword()));
        } catch (URISyntaxException ex) {
            throw new YsProException("URI syntax in getHandle");
        }

        return execute(new HttpGet(uri));
    }

    @Deprecated
    public String freeHandle() throws YsProException {

        //URI uri = new URI(String.format("%s/FreeHandle.php?HandleID=%s", client.getYsProHost(), client.getHandleId()));
        client.clearHandle();
        //String res = excuteGet(uri);
        return "done";

    }

    private String execute(HttpUriRequest request) throws YsProException {
        HttpEntity entity = null;
        try {
            LOG.log(Level.FINE, "Trying execute url: {0}", request.getURI());
            HttpResponse response = client.getClient(timeout).execute(request);
            int statusSode = response.getStatusLine().getStatusCode();
            LOG.log(Level.FINE, "Executed url with status: {0}", statusSode);

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
                    LOG.log(Level.FINE, "Cleaning entity");
                    EntityUtils.consume(entity);
                } catch (IOException ex) {
                    LOG.log(Level.SEVERE, ex.getMessage(), ex);
                }
            }
        }
    }

    private void ensureHandle() throws YsProException {
        if (client.getHandleId() == null) {
            client.setHandleId(getHandle());
        }
    }

    /**
     *
     * @param customer de 9 cifre
     * @return json dokument. Der er en liste af "Products" med de - for
     * produktet (7000) definerede - properties
     * @throws YsProException
     */
    public ProStoreResponse findOttEngagement(String customer) throws YsProException {
        try {
            ensureHandle();
            URI url = new URI(String.format("%s/GetOttEngagement.php?HandleID=%s&CustomerNumber=%s&json=1", client.getYsProHost(), client.getHandleId(), customer));
            ProStoreResponse psr = new ProStoreResponse(execute(new HttpGet(url)));
            if (psr.getStatus() == 50) { //handleTimeout clear handle
                client.setHandleId(null);
                ensureHandle();
                url = new URI(String.format("%s/GetOttEngagement.php?HandleID=%s&CustomerNumber=%s&json=1", client.getYsProHost(), client.getHandleId(), customer));
                psr = new ProStoreResponse(execute(new HttpGet(url)));
            }
            return psr;
        } catch (URISyntaxException ex) {
            throw new YsProException(ex.getMessage(), ex);
        }
    }
    //GetEngagement.php

    public ProStoreResponse findEngagement(String customer) throws YsProException {
        try {
            ensureHandle();
            URI url = new URI(String.format("%s/GetEngagement.php?HandleID=%s&CustomerNumber=%s", client.getYsProHost(), client.getHandleId(), customer));
            ProStoreResponse psr = new ProStoreResponse(execute(new HttpGet(url)));
            if (psr.getStatus() == 50) { //handleTimeout clear handle
                client.clearHandle();
                ensureHandle();
                url = new URI(String.format("%s/GetEngagement.php?HandleID=%s&CustomerNumber=%s", client.getYsProHost(), client.getHandleId(), customer));
                psr = new ProStoreResponse(execute(new HttpGet(url)));
            }
            return psr;
        } catch (URISyntaxException ex) {
            throw new YsProException(ex.getMessage(), ex);
        }
    }

    public ProStoreResponse findEngagementOnlyActive(String customer) throws YsProException {
        try {
            ensureHandle();
            URI url = new URI(String.format("%s/GetEngagement.php?HandleID=%s&CustomerNumber=%s&ShowOnlyActive=true", client.getYsProHost(), client.getHandleId(), customer));
            ProStoreResponse psr = new ProStoreResponse(execute(new HttpGet(url)));
            if (psr.getStatus() == 50) { //handleTimeout clear handle
                client.clearHandle();
                ensureHandle();
                url = new URI(String.format("%s/GetEngagement.php?HandleID=%s&CustomerNumber=%s&ShowOnlyActive=true", client.getYsProHost(), client.getHandleId(), customer));
                psr = new ProStoreResponse(execute(new HttpGet(url)));
            }
            return psr;
        } catch (URISyntaxException ex) {
            throw new YsProException(ex.getMessage(), ex);
        }
    }

    public ProStoreResponse findEngagementFromProductId(String customer, String productId) throws YsProException {
        try {
            ensureHandle();
            URI url = new URI(String.format("%s/GetEngagement.php?HandleID=%s&CustomerNumber=%s&ProductID=%s", client.getYsProHost(), client.getHandleId(), customer, productId));
            ProStoreResponse psr = new ProStoreResponse(execute(new HttpGet(url)));
            if (psr.getStatus() == 50) { //handleTimeout clear handle
                client.clearHandle();
                ensureHandle();
                url = new URI(String.format("%s/GetEngagement.php?HandleID=%s&CustomerNumber=%s&ProductID=%s", client.getYsProHost(), client.getHandleId(), customer, productId));
                psr = new ProStoreResponse(execute(new HttpGet(url)));
            }
            return psr;
        } catch (URISyntaxException ex) {
            throw new YsProException(ex.getMessage(), ex);
        }
    }

    //http://ysprodev.yousee.dk/GetUserInfo.php?HandleID=0nQU9YUs0f4u88czvWCkB2587OL2CX&CustomerNumber=607777777&xml=1
    public UserInfo findUserInfo(String userID) throws YsProException {
        try {
            ensureHandle();
            URI uri = new URI(String.format("%s/GetUserInfo.php?HandleID=%s&UserID=%s&xml=1", client.getYsProHost(), client.getHandleId(), userID));
            String st = execute(new HttpGet(uri));

            UserInfo ui = new UserInfo(UserInfo.DataFormat.xml, st);
            if (ui.getStatus() == 50) {
                client.clearHandle();
                ensureHandle();
                uri = new URI(String.format("%s/GetUserInfo.php?HandleID=%s&UserID=%s&xml=1", client.getYsProHost(), client.getHandleId(), userID));
                st = execute(new HttpGet(uri));
                ui = new UserInfo(UserInfo.DataFormat.xml, st);
            }
            return ui;
        } catch (URISyntaxException ex) {
            throw new YsProException(ex.getMessage(), ex);
        }
    }

    //http://ysprodev.yousee.dk/GetUserInfo.php?HandleID=0nQU9YUs0f4u88czvWCkB2587OL2CX&CustomerNumber=607777777&xml=1
    public UserInfo findCustomerInfo(String customer) throws YsProException {
        try {
            ensureHandle();
            URI uri = new URI(String.format("%s/GetUserInfo.php?HandleID=%s&CustomerNumber=%s&xml=1", client.getYsProHost(), client.getHandleId(), customer));
            String st = execute(new HttpGet(uri));

            UserInfo ui = new UserInfo(UserInfo.DataFormat.xml, st);
            if (ui.getStatus() == 50) {
                client.clearHandle();
                ensureHandle();
                uri = new URI(String.format("%s/GetUserInfo.php?HandleID=%s&CustomerNumber=%s&xml=1", client.getYsProHost(), client.getHandleId(), customer));
                st = execute(new HttpGet(uri));
                ui = new UserInfo(UserInfo.DataFormat.xml, st);
            }
            return ui;
        } catch (URISyntaxException ex) {
            throw new YsProException(ex.getMessage(), ex);
        }
    }

    //http://ysprodev.yousee.dk/GetEngagementByValue.php?HandleID=6sz06U5lxwoA85yZJ3239V1CzM5k3G&ProductID=6900&DataName=Device_Mac&Value=12:34:56:78:90:AB
    public ProStoreResponse findCustomersFromOTTmacStb(String mac) throws YsProException {
        try {
            ensureHandle();
            URI url = new URI(String.format("%s/GetEngagementByValue.php?HandleID=%s&ProductID=6900&DataName=Device_Mac&Value=%s", client.getYsProHost(), client.getHandleId(), mac));
            ProStoreResponse psr = new ProStoreResponse(execute(new HttpGet(url)));
            if (psr.getStatus() == 50) { //handleTimeout clear handle
                client.clearHandle();
                ensureHandle();
                url = new URI(String.format("%s/GetEngagementByValue.php?HandleID=%s&ProductID=6900&DataName=Device_Mac&Value=%s", client.getYsProHost(), client.getHandleId(), mac));
                psr = new ProStoreResponse(execute(new HttpGet(url)));
            }
            return psr;
        } catch (URISyntaxException ex) {
            throw new YsProException(ex.getMessage(), ex);
        }
    }

    public URI generateUpdateUrl(String customer, String json) throws YsProException {
        try {
            String encoded = URLEncoder.encode(json, "UTF-8");
            return new URI(String.format("%s/AssignProduct.php?HandleID=%s&CustomerNumber=%s&Products=%s", client.getYsProHost(), client.getHandleId(), customer, encoded));
        } catch (UnsupportedEncodingException ex) {
            throw new YsProException(ex.getMessage(), ex);
        } catch (URISyntaxException ex) {
            throw new YsProException(ex.getMessage(), ex);
        }
    }

    public ProStoreResponse assignProduct(String customer, String json) throws YsProException {
        ensureHandle();
        URI href = generateUpdateUrl(customer, json);
        ProStoreResponse psr = new ProStoreResponse(execute(new HttpPost(href)));
        if (psr.getStatus() == 50) { //handleTimeout clear handle
            client.clearHandle();
            ensureHandle();
            href = generateUpdateUrl(customer, json);
            psr = new ProStoreResponse(execute(new HttpPost(href)));
        }
        return psr;
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
    private ProStoreResponse removeEngagement(String customer, YsProProduct product) throws YsProException {
        try {
            ensureHandle();
            URI url = new URI(String.format("%s/RemoveEngagement.php?HandleID=%s&CustomerNumber=%s&ProductID=%s", client.getYsProHost(), client.getHandleId(), customer, product));
            ProStoreResponse psr = new ProStoreResponse(execute(new HttpGet(url)));
            if (psr.getStatus() == 50) { //handleTimeout clear handle
                client.clearHandle();
                ensureHandle();
                url = new URI(String.format("%s/RemoveEngagement.php?HandleID=%s&CustomerNumber=%s&ProductID=%s", client.getYsProHost(), client.getHandleId(), customer, product));
                psr = new ProStoreResponse(execute(new HttpGet(url)));
            }
            return psr;
        } catch (URISyntaxException ex) {
            throw new YsProException(ex.getMessage(), ex);
        }
    }

    public ProStoreResponse findEngagementFromUuid(String uuid) throws YsProException {
        try {
            ensureHandle();
            URI url = new URI(String.format("%s/GetEngagement.php?HandleID=%s&UUID=%s", client.getYsProHost(), client.getHandleId(), uuid));
            ProStoreResponse psr = new ProStoreResponse(execute(new HttpGet(url)));
            if (psr.getStatus() == 50) { //handleTimeout clear handle
                client.clearHandle();
                ensureHandle();
                url = new URI(String.format("%s/GetEngagement.php?HandleID=%s&UUID=%s", client.getYsProHost(), client.getHandleId(), uuid));
                psr = new ProStoreResponse(execute(new HttpGet(url)));
            }
            return psr;
        } catch (URISyntaxException ex) {
            throw new YsProException(ex.getMessage(), ex);
        }
    }

    public ProStoreResponse findMailGetInfo(String kpmNumber) throws YsProException {
        try {
            ensureHandle();
            URI url = new URI(String.format("%s/MailGetInfo.php?HandleID=%s&id=%s", client.getYsProHost(), client.getHandleId(), kpmNumber));
            JsonObject json = new JsonParser().parse(execute(new HttpGet(url))).getAsJsonObject();
            ProStoreResponse psr = new ProStoreResponse(json.get("StatusCode").getAsInt(), json.get("StatusMessage").getAsString());

            if (psr.getStatus() == 50) { //handleTimeout clear handle
                client.clearHandle();
                ensureHandle();
                url = new URI(String.format("%s/MailGetInfo.php?HandleID=%s&id=%s", client.getYsProHost(), client.getHandleId(), kpmNumber));
                json = new JsonParser().parse(execute(new HttpGet(url))).getAsJsonObject();
                psr = new ProStoreResponse(json.get("StatusCode").getAsInt(), json.get("StatusMessage").getAsString());
            }
            if (json.has("Data")) {
                StoreProduct sp = new StoreProduct(json.get("Data").getAsJsonObject());
                psr.getProducts().add(sp);
            }
            return psr;
        } catch (URISyntaxException ex) {
            throw new YsProException(ex.getMessage(), ex);
        }
    }

    /**
     * Log in to YSPro and return CustomerInfo on successful login. On failure to log in, retur null
     * @param username
     * @param password
     * @return YSpro session id on success, null on failure
     * @throws YsProException 
     */
    public String login(String username, String password) throws YsProException {
        try {
            ensureHandle();
            URI url = new URI(String.format("%s/Login.php?HandleID=%s&UserName=%s&Password=%s",
                        client.getYsProHost(), client.getHandleId(), username, password));
            String sessionid = execute(new HttpGet(url));
            if (sessionid == null || sessionid.trim().equals("0"))
                return null;
            return sessionid;
        } catch (URISyntaxException ex) {
            throw new YsProException(ex.getMessage(), ex);
        }

    }
}

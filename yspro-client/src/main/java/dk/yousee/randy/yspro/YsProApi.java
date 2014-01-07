package dk.yousee.randy.yspro;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

/**
 *
 * @author m27236
 */
public class YsProApi {
    private static final Logger LOG = Logger.getLogger(YsProApi.class);
    private ProStoreConnectorImpl client;

    public void setClient(ProStoreConnectorImpl client) {
        this.client = client;
    }

    public YsProApi() {
    }

	public synchronized void getNewHandleId() throws YsProException {
		client.setHandleId(null);
		ensureHandle();
	}
    
    private String getHandle() throws YsProException {
        URI uri;
        try {
            uri = new URI(String.format("%s/GetHandle.php", client.getYsProHost()));

        } catch (URISyntaxException ex) {
            throw new YsProException("URI syntax in getHandle");
        }
        HttpPost post = new HttpPost(uri);
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("SystemLogin", client.getSystemLogin()));
        params.add(new BasicNameValuePair("SystemPassword", client.getSystemPassword()));
        post.setEntity(new UrlEncodedFormEntity(params, Charset.forName("UTF-8")));

        JsonObject res = new JsonParser().parse(execute(post)).getAsJsonObject();
        return res.getAsJsonObject("Data").get("HandleID").getAsString();
    }

    private String execute(HttpUriRequest request) throws YsProException {
    	return execute(request, true);
    }

    private String execute(HttpUriRequest request, boolean retry) throws YsProException {
        HttpEntity entity = null;
        try {
            LOG.debug("Trying execute url: " + request.getURI());
            HttpResponse response = client.getClient().execute(request);
            int statusSode = response.getStatusLine().getStatusCode();
            LOG.debug("Executed url with status: " + statusSode);

            entity = response.getEntity();

            if (statusSode == HttpStatus.SC_OK) {
            	String responseString = EntityUtils.toString(entity, "UTF-8");
            	
            	ProStoreResponse proStoreResponse = new ProStoreResponse(responseString);
            	if (proStoreResponse.getStatus() == 0) {
                    return EntityUtils.toString(entity, "UTF-8");
            	} else if (proStoreResponse.getStatus() == 50) {
                	if (retry) {
                		getNewHandleId();
                		return execute(request, false);
                	}
            	}
            	
            	throw new YsProException(proStoreResponse.getStatus() + " - " + proStoreResponse.getMessage());
            	
            	
            } else {
                LOG.info("YsPro backend fail! " + statusSode);
                throw new YsProException(new YsProErrorVO(statusSode, EntityUtils.toString(entity, "UTF-8")));
            }
        } catch (IOException ioe) {
            LOG.error(ioe.getMessage(), ioe);
            throw new YsProException("Yspro IOexception error", ioe);
        } finally {
            EntityUtils.consumeQuietly(entity);
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
            URI url = new URI(String.format("%s/GetOttEngagement.php", client.getYsProHost()));
            HttpPost post = new HttpPost(url);
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("HandleID", client.getHandleId()));
            params.add(new BasicNameValuePair("CustomerNumber", customer));
            params.add(new BasicNameValuePair("json", "1"));

            post.setEntity(new UrlEncodedFormEntity(params, Charset.forName("UTF-8")));
            
            return new ProStoreResponse(execute(post));
        } catch (URISyntaxException ex) {
            throw new YsProException(ex.getMessage(), ex);
        }
    }
    //GetEngagement.php

    public ProStoreResponse findEngagement(String customer) throws YsProException {
        try {
            ensureHandle();
            URI url = new URI(String.format("%s/GetEngagement.php", client.getYsProHost()));
            HttpPost post = new HttpPost(url);
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("HandleID", client.getHandleId()));
            params.add(new BasicNameValuePair("CustomerNumber", customer));

            post.setEntity(new UrlEncodedFormEntity(params, Charset.forName("UTF-8")));
            
            return new ProStoreResponse(execute(post));
        } catch (URISyntaxException ex) {
            throw new YsProException(ex.getMessage(), ex);
        }
    }

    public ProStoreResponse findEngagementOnlyActive(String customer) throws YsProException {
        try {
            ensureHandle();
            URI url = new URI(String.format("%s/GetEngagement.php", client.getYsProHost()));
            HttpPost post = new HttpPost(url);
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("HandleID", client.getHandleId()));
            params.add(new BasicNameValuePair("CustomerNumber", customer));
            params.add(new BasicNameValuePair("ShowOnlyActive", "true"));

            post.setEntity(new UrlEncodedFormEntity(params, Charset.forName("UTF-8")));
            
            return new ProStoreResponse(execute(post));
        } catch (URISyntaxException ex) {
            throw new YsProException(ex.getMessage(), ex);
        }
    }

    public ProStoreResponse findEngagementFromProductId(String customer, String productId) throws YsProException {
        try {
            ensureHandle();
            URI url = new URI(String.format("%s/GetEngagement.php", client.getYsProHost()));
            HttpPost post = new HttpPost(url);
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("HandleID", client.getHandleId()));
            params.add(new BasicNameValuePair("CustomerNumber", customer));
            params.add(new BasicNameValuePair("ProductID", productId));

            post.setEntity(new UrlEncodedFormEntity(params, Charset.forName("UTF-8")));

            return new ProStoreResponse(execute(post));
        } catch (URISyntaxException ex) {
            throw new YsProException(ex.getMessage(), ex);
        }
    }

    public ProStoreResponse getCommunityWifiEngagementUserID(String userID) throws YsProException {
        try {
            ensureHandle();
            URI url = new URI(String.format("%s/GetCommunityWifiEngagement.php", client.getYsProHost()));
            HttpPost post = new HttpPost(url);
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("HandleID", client.getHandleId()));
            params.add(new BasicNameValuePair("UserID", userID));

            post.setEntity(new UrlEncodedFormEntity(params, Charset.forName("UTF-8")));

            return new ProStoreResponse(execute(post));
        } catch (URISyntaxException ex) {
            throw new YsProException(ex.getMessage(), ex);
        }
    }

    /**
     * Show active Digital dvb-c tv products
     *
     * @param userID
     * @return
     * @throws YsProException
     */
    public ProStoreResponse getDigiTvEngagementUserID(String userID) throws YsProException {
        try {
            ensureHandle();
            URI url = new URI(String.format("%s/GetDigiTVEngagement.php", client.getYsProHost()));
            HttpPost post = new HttpPost(url);
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("HandleID", client.getHandleId()));
            params.add(new BasicNameValuePair("UserID", userID));

            post.setEntity(new UrlEncodedFormEntity(params, Charset.forName("UTF-8")));

            return new ProStoreResponse(execute(post));
        } catch (URISyntaxException ex) {
            throw new YsProException(ex.getMessage(), ex);
        }
    }
    
     /**
     * Show active Digital dvb-c tv products
     *
     * @param userID
     * @return
     * @throws YsProException
     */
    public ProStoreResponse getDigiTvEngagementSubscriber(String subscriber) throws YsProException {
        try {
            ensureHandle();
            URI url = new URI(String.format("%s/GetDigiTVEngagement.php", client.getYsProHost()));
            HttpPost post = new HttpPost(url);
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("HandleID", client.getHandleId()));
            params.add(new BasicNameValuePair("CustomerNumber", subscriber));

            post.setEntity(new UrlEncodedFormEntity(params, Charset.forName("UTF-8")));

            return new ProStoreResponse(execute(post));
        } catch (URISyntaxException ex) {
            throw new YsProException(ex.getMessage(), ex);
        }
    }

    /**
     * http://ysprodev.yousee.dk/GetUserInfo.php?HandleID=0nQU9YUs0f4u88czvWCkB2587OL2CX&SesssionID=xxxxxxx&xml=1
     */
    public UserInfo findSessionInfo(String sessionId) throws YsProException {
        try {
            ensureHandle();
            URI uri = new URI(String.format("%s/GetUserInfo.php",
                    client.getYsProHost()));

            HttpPost post = new HttpPost(uri);
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("HandleID", client.getHandleId()));
            params.add(new BasicNameValuePair("SessionID", sessionId));

            post.setEntity(new UrlEncodedFormEntity(params, Charset.forName("UTF-8")));
            return new UserInfo(execute(post));
        } catch (URISyntaxException ex) {
            throw new YsProException(ex.getMessage(), ex);
        }
    }

    //http://ysprodev.yousee.dk/GetUserInfo.php?HandleID=0nQU9YUs0f4u88czvWCkB2587OL2CX&CustomerNumber=607777777&xml=1
    public UserInfo findUserInfo(String userID) throws YsProException {
        try {
            ensureHandle();
            URI uri = new URI(String.format("%s/GetUserInfo.php", client.getYsProHost()));

            HttpPost post = new HttpPost(uri);
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("HandleID", client.getHandleId()));
            params.add(new BasicNameValuePair("UserID", userID));

            post.setEntity(new UrlEncodedFormEntity(params, Charset.forName("UTF-8")));
            return new UserInfo(execute(post));
        } catch (URISyntaxException ex) {
            throw new YsProException(ex.getMessage(), ex);
        }
    }

    /**
     * Finds userid for active sessionID
     *
     * @param sessionID
     * @return userID or null if not found
     * @throws YsProException
     */
    public ProStoreResponse findBasicUserInfo(String sessionID) throws YsProException {
        try {
            ensureHandle();
            URI uri = new URI(String.format("%s/GetUserBasicInfo.php", client.getYsProHost()));
            HttpPost post = new HttpPost(uri);
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("HandleID", client.getHandleId()));
            params.add(new BasicNameValuePair("SessionID", sessionID));

            post.setEntity(new UrlEncodedFormEntity(params, Charset.forName("UTF-8")));

            return new ProStoreResponse(execute(post));
        } catch (URISyntaxException ex) {
            throw new YsProException(ex.getMessage(), ex);
        }
    }

    //http://ysprodev.yousee.dk/GetUserInfo.php?HandleID=0nQU9YUs0f4u88czvWCkB2587OL2CX&CustomerNumber=607777777&xml=1
    public UserInfo findCustomerInfo(String customer) throws YsProException {
        try {
            ensureHandle();
            URI uri = new URI(String.format("%s/GetUserInfo.php", client.getYsProHost()));
            HttpPost post = new HttpPost(uri);
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("HandleID", client.getHandleId()));
            params.add(new BasicNameValuePair("CustomerNumber", customer));

            post.setEntity(new UrlEncodedFormEntity(params, Charset.forName("UTF-8")));

            return new UserInfo(execute(post));
        } catch (URISyntaxException ex) {
            throw new YsProException(ex.getMessage(), ex);
        }
    }

    public UserInfo findCustomerInfoAndInactive(String customer) throws YsProException {
        try {
            ensureHandle();
            URI uri = new URI(String.format("%s/GetUserInfo.php", client.getYsProHost()));
            HttpPost post = new HttpPost(uri);
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("HandleID", client.getHandleId()));
            params.add(new BasicNameValuePair("CustomerNumber", customer));
            params.add(new BasicNameValuePair("ShowOnlyActive", "false"));
            post.setEntity(new UrlEncodedFormEntity(params, Charset.forName("UTF-8")));

            String st = execute(post);

            return new UserInfo(st);
        } catch (URISyntaxException ex) {
            throw new YsProException(ex.getMessage(), ex);
        }
    }

    //http://ysprodev.yousee.dk/GetEngagementByValue.php?HandleID=6sz06U5lxwoA85yZJ3239V1CzM5k3G&ProductID=6900&DataName=Device_Mac&Value=12:34:56:78:90:AB
    public ProStoreResponse findCustomersFromOTTmacStb(String mac) throws YsProException {
        try {
            ensureHandle();
            URI url = new URI(String.format("%s/GetEngagementByValue.php", client.getYsProHost()));
            HttpPost post = new HttpPost(url);
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("HandleID", client.getHandleId()));
            params.add(new BasicNameValuePair("ProductID", "6900"));
            params.add(new BasicNameValuePair("DataName", "Device_Mac"));
            params.add(new BasicNameValuePair("Value", mac));
            post.setEntity(new UrlEncodedFormEntity(params, Charset.forName("UTF-8")));

            return new ProStoreResponse(execute(post));
        } catch (URISyntaxException ex) {
            throw new YsProException(ex.getMessage(), ex);
        }
    }

    public ProStoreResponse assignProduct(String customer, String json) throws YsProException {
        ensureHandle();
        try {
            URI uri = new URI(String.format("%s/ApplyEngagement.php", client.getYsProHost()));

            HttpPost post = new HttpPost(uri);
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("HandleID", client.getHandleId()));
            params.add(new BasicNameValuePair("CustomerNumber", customer));
            params.add(new BasicNameValuePair("Products", json));
            post.setEntity(new UrlEncodedFormEntity(params, Charset.forName("UTF-8")));

            return new ProStoreResponse(execute(post));
        } catch (URISyntaxException ex) {
            throw new YsProException(ex.getMessage(), ex);
        }
    }

    public ProStoreResponse findEngagementFromUuid(String uuid) throws YsProException {
        try {
            ensureHandle();
            URI url = new URI(String.format("%s/GetEngagement.php", client.getYsProHost()));
            HttpPost post = new HttpPost(url);
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("HandleID", client.getHandleId()));
            params.add(new BasicNameValuePair("UUID", uuid));
            post.setEntity(new UrlEncodedFormEntity(params, Charset.forName("UTF-8")));

            ProStoreResponse psr = new ProStoreResponse(execute(post));

            return psr;
        } catch (URISyntaxException ex) {
            throw new YsProException(ex.getMessage(), ex);
        }
    }

    public ProStoreResponse findMailGetInfo(String kpmNumber) throws YsProException {
        try {
            ensureHandle();
            URI url = new URI(String.format("%s/MailGetInfo.php", client.getYsProHost()));
            HttpPost post = new HttpPost(url);
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("HandleID", client.getHandleId()));
            params.add(new BasicNameValuePair("MailID", kpmNumber));
            post.setEntity(new UrlEncodedFormEntity(params, Charset.forName("UTF-8")));

            JsonObject json = new JsonParser().parse(execute(post)).getAsJsonObject();
            ProStoreResponse psr = new ProStoreResponse(json);

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
     * Log in to YSPro and return CustomerInfo on successful login. On failure
     * to log in, retur null
     *
     * @param username
     * @param password
     * @return YSpro session id on success, null on failure
     * @throws YsProException
     */
    public String login(String username, String password) throws YsProException {
        try {
            ensureHandle();
            URI uri = new URIBuilder(client.getYsProHost()).setPath("/Login.php").build();
            HttpPost post = new HttpPost(uri);
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("HandleID", client.getHandleId()));
            params.add(new BasicNameValuePair("UserName", username));
            params.add(new BasicNameValuePair("Password", password));
            post.setEntity(new UrlEncodedFormEntity(params, Charset.forName("UTF-8")));

            ProStoreResponse res = new ProStoreResponse(execute(post));
            if (res.getData() != null && res.getData().has("SessionID"))
                return res.getData().get("SessionID").getAsString();

            return null;
        } catch (URISyntaxException ex) {
            throw new YsProException(ex.getMessage(), ex);
        }
    }

    public ProStoreResponse getNewPasswordMethods(String search, InetAddress customerIp) throws YsProException {
        try {
            ensureHandle();
            URI url = new URIBuilder(client.getYsProHost()).setPath("/GetNewPasswordMethods.php").build();
            HttpPost post = new HttpPost(url);
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("HandleID", client.getHandleId()));
            params.add(new BasicNameValuePair("SearchFor", search));
            params.add(new BasicNameValuePair("ClientIP", customerIp.getHostAddress()));
            post.setEntity(new UrlEncodedFormEntity(params, Charset.forName("UTF-8")));

            return new ProStoreResponse(execute(post));
        } catch (URISyntaxException ex) {
            throw new YsProException(ex.getMessage(), ex);
        }
    }

    public ProStoreResponse generateNewPassword(String userId, String using, InetAddress customerIp) throws YsProException {
        try {
            ensureHandle();
            URI url = new URI(String.format("%s/GenerateNewPassword.php",
                    client.getYsProHost()));
            HttpPost post = new HttpPost(url);
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("HandleID", client.getHandleId()));
            params.add(new BasicNameValuePair("UserID", userId));
            params.add(new BasicNameValuePair("SendUsing", using));
            params.add(new BasicNameValuePair("ClientIP", customerIp.getHostAddress()));
            post.setEntity(new UrlEncodedFormEntity(params, Charset.forName("UTF-8")));

            String res = execute(post);
            return new ProStoreResponse(res);
        } catch (URISyntaxException ex) {
            throw new YsProException(ex.getMessage(), ex);
        }
    }

    public ProStoreResponse setPassword(String userId, String password) throws YsProException {
        try {
            ensureHandle();
            URI url = new URIBuilder(client.getYsProHost()).setPath("/SetPassword.php").build();
            HttpPost post = new HttpPost(url);
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("HandleID", client.getHandleId()));
            params.add(new BasicNameValuePair("UserID", userId));
            params.add(new BasicNameValuePair("NewPassword", password));
            post.setEntity(new UrlEncodedFormEntity(params, Charset.forName("UTF-8")));

            String res = execute(post);
            return new ProStoreResponse(res);
        } catch (URISyntaxException ex) {
            throw new YsProException(ex.getMessage(), ex);
        }
    }

    public ProStoreResponse updateUserInfo(String userId, String userName, String oldUserName, String emailAddress, String cellPhone) throws YsProException {
        try {
            ensureHandle();
            URI url = new URIBuilder(client.getYsProHost()).setPath("/UpdateUserInfo.php").build();

            HttpPost post = new HttpPost(url);
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("HandleID", client.getHandleId()));
            params.add(new BasicNameValuePair("UserID", userId));
            if (userName != null) {
                params.add(new BasicNameValuePair("UserName", userName));
                params.add(new BasicNameValuePair("OldUserName", oldUserName));
            }
            if (emailAddress != null)
                params.add(new BasicNameValuePair("EmailAddress", emailAddress));
            if (cellPhone != null)
                params.add(new BasicNameValuePair("CellPhone", cellPhone));
            post.setEntity(new UrlEncodedFormEntity(params, Charset.forName("UTF-8")));

            String res = execute(post);
            return new ProStoreResponse(res);
        } catch (URISyntaxException ex) {
            throw new YsProException(ex.getMessage(), ex);
        }
    }

    public ProStoreResponse createYouSeeLogin(String subscriber) throws YsProException {
        try {
            ensureHandle();
            URI url = new URI(String.format("%s/CreateYouSeeLogin.php",
                    client.getYsProHost()));
            HttpPost post = new HttpPost(url);
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("HandleID", client.getHandleId()));
            params.add(new BasicNameValuePair("CustomerNumber", subscriber));
            post.setEntity(new UrlEncodedFormEntity(params, Charset.forName("UTF-8")));
            String res = execute(post);
            return new ProStoreResponse(res);
        } catch (URISyntaxException ex) {
            throw new YsProException(ex.getMessage(), ex);
        }
    }
}

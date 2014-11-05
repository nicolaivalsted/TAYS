package dk.yousee.randy.yspro;

import com.google.gson.JsonObject;
import java.net.InetAddress;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.message.BasicNameValuePair;
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

    /**
     *
     * @param customer de 9 cifre
     * @return json dokument. Der er en liste af "Products" med de - for
     * produktet (7000) definerede - properties
     * @throws YsProException
     */
    public ProStoreResponse findOttEngagement(String customer) throws YsProException {
        try {
            URI url = new URI(String.format("%s/GetOttEngagement.php", client.getYsProHost()));
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("CustomerNumber", customer));
            params.add(new BasicNameValuePair("json", "1"));
            return client.executeYsPro(url, params);
        } catch (URISyntaxException ex) {
            throw new YsProException(ex.getMessage(), ex);
        }
    }
	
    //GetEngagement.php

    public ProStoreResponse findEngagement(String customer) throws YsProException {
        try {
            URI url = new URI(String.format("%s/GetEngagement.php", client.getYsProHost()));
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("CustomerNumber", customer));
            return client.executeYsPro(url, params);
        } catch (URISyntaxException ex) {
            throw new YsProException(ex.getMessage(), ex);
        }
    }

    public ProStoreResponse findEngagementOnlyActive(String customer) throws YsProException {
        try {
            URI url = new URI(String.format("%s/GetEngagement.php", client.getYsProHost()));
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("CustomerNumber", customer));
            params.add(new BasicNameValuePair("ShowOnlyActive", "true"));
            return client.executeYsPro(url, params);
        } catch (URISyntaxException ex) {
            throw new YsProException(ex.getMessage(), ex);
        }
    }

    public ProStoreResponse findEngagementFromProductId(String customer, String productId) throws YsProException {
        try {
            URI url = new URI(String.format("%s/GetEngagement.php", client.getYsProHost()));
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("CustomerNumber", customer));
            params.add(new BasicNameValuePair("ProductID", productId));
            return client.executeYsPro(url, params);
        } catch (URISyntaxException ex) {
            throw new YsProException(ex.getMessage(), ex);
        }
    }

    public ProStoreResponse getCommunityWifiEngagementUserID(String userID) throws YsProException {
        try {
            URI url = new URI(String.format("%s/GetCommunityWifiEngagement.php", client.getYsProHost()));
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("UserID", userID));
            return client.executeYsPro(url, params);
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
            URI url = new URI(String.format("%s/GetDigiTVEngagement.php", client.getYsProHost()));
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("UserID", userID));
            return client.executeYsPro(url, params);
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
            URI url = new URI(String.format("%s/GetDigiTVEngagement.php", client.getYsProHost()));
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("CustomerNumber", subscriber));
            return client.executeYsPro(url, params);
        } catch (URISyntaxException ex) {
            throw new YsProException(ex.getMessage(), ex);
        }
    }

    /**
     * http://ysprodev.yousee.dk/GetUserInfo.php?HandleID=0nQU9YUs0f4u88czvWCkB2587OL2CX&SesssionID=xxxxxxx&xml=1
     */
    public UserInfo findSessionInfo(String sessionId) throws YsProException {
        try {
            URI uri = new URI(String.format("%s/GetUserInfo.php",
                    client.getYsProHost()));
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("SessionID", sessionId));
            return client.executeYsPro(uri, params, UserInfo.class);
        } catch (URISyntaxException ex) {
            throw new YsProException(ex.getMessage(), ex);
        }
    }

    //http://ysprodev.yousee.dk/GetUserInfo.php?HandleID=0nQU9YUs0f4u88czvWCkB2587OL2CX&CustomerNumber=607777777&xml=1
    public UserInfo findUserInfo(String userID) throws YsProException {
        try {
            URI uri = new URI(String.format("%s/GetUserInfo.php", client.getYsProHost()));
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("UserID", userID));
            return client.executeYsPro(uri, params, UserInfo.class);
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
            URI uri = new URI(String.format("%s/GetUserBasicInfo.php", client.getYsProHost()));
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("SessionID", sessionID));
            return client.executeYsPro(uri, params);
        } catch (URISyntaxException ex) {
            throw new YsProException(ex.getMessage(), ex);
        }
    }

    //http://ysprodev.yousee.dk/GetUserInfo.php?HandleID=0nQU9YUs0f4u88czvWCkB2587OL2CX&CustomerNumber=607777777&xml=1
    public UserInfo findCustomerInfo(String customer) throws YsProException {
        try {
            URI uri = new URI(String.format("%s/GetUserInfo.php", client.getYsProHost()));
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("CustomerNumber", customer));
            return client.executeYsPro(uri, params, UserInfo.class);
        } catch (URISyntaxException ex) {
            throw new YsProException(ex.getMessage(), ex);
        }
    }

    public UserInfo findCustomerInfoAndInactive(String customer) throws YsProException {
        try {
            URI uri = new URI(String.format("%s/GetUserInfo.php", client.getYsProHost()));
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("CustomerNumber", customer));
            params.add(new BasicNameValuePair("ShowOnlyActive", "false"));
            return client.executeYsPro(uri, params, UserInfo.class);
        } catch (URISyntaxException ex) {
            throw new YsProException(ex.getMessage(), ex);
        }
    }

    //http://ysprodev.yousee.dk/GetEngagementByValue.php?HandleID=6sz06U5lxwoA85yZJ3239V1CzM5k3G&ProductID=6900&DataName=Device_Mac&Value=12:34:56:78:90:AB
    public ProStoreResponse findCustomersFromOTTmacStb(String mac) throws YsProException {
        try {
            URI url = new URI(String.format("%s/GetEngagementByValue.php", client.getYsProHost()));
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("ProductID", "6900"));
            params.add(new BasicNameValuePair("DataName", "Device_Mac"));
            params.add(new BasicNameValuePair("Value", mac));
            return client.executeYsPro(url, params);
        } catch (URISyntaxException ex) {
            throw new YsProException(ex.getMessage(), ex);
        }
    }

    public ProStoreResponse assignProduct(String customer, String json) throws YsProException {
        try {
            URI uri = new URI(String.format("%s/ApplyEngagement.php", client.getYsProHost()));
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("CustomerNumber", customer));
            params.add(new BasicNameValuePair("Products", json));
            return client.executeYsPro(uri, params);
        } catch (URISyntaxException ex) {
            throw new YsProException(ex.getMessage(), ex);
        }
    }

    public ProStoreResponse findEngagementFromUuid(String uuid) throws YsProException {
        try {
            URI url = new URI(String.format("%s/GetEngagement.php", client.getYsProHost()));
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("UUID", uuid));
            return client.executeYsPro(url, params);
		} catch (URISyntaxException ex) {
            throw new YsProException(ex.getMessage(), ex);
        }
    }

	// TESTME
    public ProStoreResponse findMailGetInfo(String kpmNumber) throws YsProException {
        try {
            URI url = new URI(String.format("%s/MailGetInfo.php", client.getYsProHost()));
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("MailID", kpmNumber));
			ProStoreResponse psr = client.executeYsPro(url, params);
            if (psr.getJsonSource().has("Data")) {
                StoreProduct sp = new StoreProduct(psr.getData());
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
            URI uri = new URIBuilder(client.getYsProHost()).setPath("/Login.php").build();
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("UserName", username));
            params.add(new BasicNameValuePair("Password", password));
			ProStoreResponse psr = client.executeYsPro(uri, params);
			if (psr.getJsonSource().has("Data") && psr.getData().has("SessionID")) {
                return psr.getData().get("SessionID").getAsString();
            }
            return null;
        } catch (URISyntaxException ex) {
            throw new YsProException(ex.getMessage(), ex);
        }
    }

    public ProStoreResponse getNewPasswordMethods(String search, InetAddress customerIp) throws YsProException {
        try {
            URI url = new URIBuilder(client.getYsProHost()).setPath("/GetNewPasswordMethods.php").build();
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("SearchFor", search));
            params.add(new BasicNameValuePair("ClientIP", customerIp.getHostAddress()));
			return client.executeYsPro(url, params);
        } catch (URISyntaxException ex) {
            throw new YsProException(ex.getMessage(), ex);
        }
    }

    public ProStoreResponse generateNewPassword(String userId, String using, InetAddress customerIp) throws YsProException {
        try {
            URI url = new URI(String.format("%s/GenerateNewPassword.php",
                    client.getYsProHost()));
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("UserID", userId));
            params.add(new BasicNameValuePair("SendUsing", using));
            params.add(new BasicNameValuePair("ClientIP", customerIp.getHostAddress()));
			return client.executeYsPro(url, params);
        } catch (URISyntaxException ex) {
            throw new YsProException(ex.getMessage(), ex);
        }
    }

    public ProStoreResponse setPassword(String userId, String password) throws YsProException {
        try {
            URI url = new URIBuilder(client.getYsProHost()).setPath("/SetPassword.php").build();
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("UserID", userId));
            params.add(new BasicNameValuePair("NewPassword", password));
			return client.executeYsPro(url, params);
        } catch (URISyntaxException ex) {
            throw new YsProException(ex.getMessage(), ex);
        }
    }

    public ProStoreResponse updateUserInfo(String userId, String userName, String oldUserName, String emailAddress, String cellPhone) throws YsProException {
        try {
            URI url = new URIBuilder(client.getYsProHost()).setPath("/UpdateUserInfo.php").build();
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("UserID", userId));
            if (userName != null) {
                params.add(new BasicNameValuePair("UserName", userName));
                params.add(new BasicNameValuePair("OldUserName", oldUserName));
            }
            if (emailAddress != null) {
                params.add(new BasicNameValuePair("EmailAddress", emailAddress));
            }
            if (cellPhone != null) {
                params.add(new BasicNameValuePair("CellPhone", cellPhone));
            }
			return client.executeYsPro(url, params);
        } catch (URISyntaxException ex) {
            throw new YsProException(ex.getMessage(), ex);
        }
    }

    public ProStoreResponse createYouSeeLogin(String subscriber) throws YsProException {
        try {
            URI url = new URI(String.format("%s/CreateYouSeeLogin.php",
                    client.getYsProHost()));
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("CustomerNumber", subscriber));
			return client.executeYsPro(url, params);
        } catch (URISyntaxException ex) {
            throw new YsProException(ex.getMessage(), ex);
        }
    }

    public ProStoreResponse terminateProduct(String uuid) throws YsProException {
        try {
            URI url = new URI(String.format("%s/RemoveEngagement.php",
                    client.getYsProHost()));
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("UUID", uuid));
			return client.executeYsPro(url, params);
		} catch (URISyntaxException ex) {
            throw new YsProException(ex.getMessage(), ex);
        }
    }

    public ProStoreResponse terminateProductOnDate(String uuid, Date closeDate) throws YsProException {
        try {
            URI url = new URI(String.format("%s/RemoveEngagement.php",
                    client.getYsProHost()));
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("UUID", uuid));
            JsonObject o = new JsonObject();
            o.addProperty("To", YsProTime.formatDate(closeDate));
            params.add(new BasicNameValuePair("To", o.toString()));
			return client.executeYsPro(url, params);
		} catch (URISyntaxException ex) {
            throw new YsProException(ex.getMessage(), ex);
        }
    }

    public ProStoreResponse setFirstTimeUsed(String userId) throws YsProException {
        try {
            URI url = new URI(String.format("%s/SetFirstTimeUsed.php",
                    client.getYsProHost()));
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("UserID", userId));
			return client.executeYsPro(url, params);
        } catch (URISyntaxException ex) {
            throw new YsProException(ex.getMessage(), ex);
        }
    }
}

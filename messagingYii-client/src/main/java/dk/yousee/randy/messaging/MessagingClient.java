package dk.yousee.randy.messaging;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dk.yousee.randy.base.HttpPool;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

/**
 *
 * @author m27236
 */
public class MessagingClient {
    private final static Logger LOG = Logger.getLogger(MessagingClient.class);
    private String host;
    private String acceptHeader;
    private HttpPool httpPool;
    private int opretationTimeOut = 10000;

    public MessagingClient() {
    }

    public MessagingClient(String host, String K2acceptHeader, HttpPool httpPool) {
        this.host = host;
        this.acceptHeader = K2acceptHeader;
        this.httpPool = httpPool;
    }

    /**
     * Send message to customer
     * @param subscriber ex608252633
     * @param kontaktForm "RandyLukWebmail" or...
     * @param to ex sikj@yousee.dk
     * @param from null for standard or "YouSee something something<complete@darkside.dk>"
     * @param data the data for the template
     * @return order id
     * @throws MessagingException 
     */
    public String createMessagingOrder(String subscriber, String kontaktForm, String to, String from, JsonObject data) throws MessagingException {

        HttpEntity entity = null;
        try {
            HttpPost post = new HttpPost(host);
            post.setHeader("accept", acceptHeader);
            
            final JsonObject root = new JsonObject();
            if(from!=null)
                root.addProperty("email-from", from);                   
            root.addProperty("kontaktform", kontaktForm);
            root.addProperty("email", to);
            root.addProperty("kundenummer", subscriber);
            root.add("data", data);
            ByteArrayEntity bae = new ByteArrayEntity(root.toString().getBytes(Charset.forName("UTF-8")));
            post.setEntity(bae);
            HttpResponse response = httpPool.getClient().execute(post);
            int statusCode = response.getStatusLine().getStatusCode();
            LOG.debug("createMessagingOrder got status: " + statusCode);
            entity = response.getEntity();
            if (statusCode == HttpStatus.SC_CREATED) { //succes              
                JsonObject res = new JsonParser().parse(new InputStreamReader(entity.getContent())).getAsJsonObject();
                return res.get("id").getAsString();               
            } else {
                throw new MessagingException("Could not create message: "+EntityUtils.toString(entity, "UTF-8") + " status: "+statusCode);
            }

        } catch (ClientProtocolException cpe) {
            LOG.error(cpe.getMessage(), cpe);
            throw new MessagingException("Yii Messaging Connection Problem", cpe);
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
            throw new MessagingException("Unknown Error", e);
        } finally {
            EntityUtils.consumeQuietly(entity);
        }
    }

    public void setHost(String host) {
        this.host = host;
    }

    public void setHttpPool(HttpPool httpPool) {
        this.httpPool = httpPool;
    }

    public void setOpretationTimeOut(int opretationTimeOut) {
        this.opretationTimeOut = opretationTimeOut;
    }

    public void setAcceptHeader(String acceptHeader) {
        this.acceptHeader = acceptHeader;
    }

    public String getHost() {
        return host;
    }

    public String getAcceptHeader() {
        return acceptHeader;
    }

    public HttpPool getHttpPool() {
        return httpPool;
    }

    public int getOpretationTimeOut() {
        return opretationTimeOut;
    }
}

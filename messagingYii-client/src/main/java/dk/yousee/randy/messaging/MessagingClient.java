package dk.yousee.randy.messaging;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dk.yousee.randy.base.AbstractConnector;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
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
    private AbstractConnector connector;
    private int opretationTimeOut = 10000;

    public MessagingClient() {
    }

    public MessagingClient(String host, String K2acceptHeader, AbstractConnector connector) {
        this.host = host;
        this.acceptHeader = K2acceptHeader;
        this.connector = connector;
    }


    public String createMessagingOrder(String subscriber, MessagingKontaktForm kontaktForm, String to, JsonObject data) throws MessagingException {

        HttpEntity entity = null;
        try {
            HttpPost post = new HttpPost(host);
            post.setHeader("accept", acceptHeader);
            
            final JsonObject root = new JsonObject();
            root.addProperty("kontaktform", kontaktForm.name());
            root.addProperty("email", to);
            root.addProperty("kundenummer", subscriber);
            root.add("data", data);
            ByteArrayEntity bae = new ByteArrayEntity(root.toString().getBytes(Charset.forName("UTF-8")));
            post.setEntity(bae);
            HttpResponse response = connector.getClient(opretationTimeOut).execute(post);
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

    public void setConnector(AbstractConnector connector) {
        this.connector = connector;
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

    public AbstractConnector getConnector() {
        return connector;
    }

    public int getOpretationTimeOut() {
        return opretationTimeOut;
    }
}

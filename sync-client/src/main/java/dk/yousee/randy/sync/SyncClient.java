package dk.yousee.randy.sync;

import dk.yousee.randy.base.AbstractClient;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

/**
 * User: aka
 * Date: 11/01/13
 * Client to access randy-sync from java level
 */
public class SyncClient extends AbstractClient<SyncConnectorImpl> {


    URL generateCreateUrl() throws MalformedURLException {
        return new URL(String.format("%s/sync/api/sync", getConnector().getSyncHost()));
    }

    /**
     * Creates a play event
     *
     * @return result of event creation
     */
    public SyncResponse createPlayEvent(CreatePlayRequest request) {
        try {
            return innerCreatePlay(request);
        } catch (Exception e) {
            return new SyncResponse(request.getSubscriber(), "Failed2create", e.getMessage());
        }
    }
    URL generateHref(CreatePlayRequest request) throws MalformedURLException {
        return new URL(String.format("%s/sync/api/play/engagement/%s/%s/%s"
            , getConnector().getSyncHost()
            ,request.getSubscriber()
            ,request.getServiceItem()
            ,request.isSignal()
            ));
    }

    private SyncResponse innerCreatePlay(CreatePlayRequest request) throws Exception {

        HttpPost post;
        URL href = generateCreateUrl();
        post = new HttpPost(href.toString());
        String body=String.format("{\n" +
            "      \"forbruger\": \"%s\",\n" +
            "      \"links\": [{\n" +
            "                \"href\": \"%s\",\n" +
            "                \"mediatype\": \"application/vnd.yousee.kasia2+json;version=1;charset=UTF-8\",\n" +
            "                \"rel\": \"engagement\"\n" +
            "            }\n" +
            "        ],\n" +
            "      \"system\":\"%s\",\n" +
            "      \"reference\":\"%s\",\n" +
            "      \"user\":\"%s\"\n" +
            "    }"
            ,request.getSubscriber()
            ,generateHref(request)
            ,request.getSystem()
            ,request.getReference()
            ,request.getUser()
        );
        post.setEntity(new StringEntity(body, ContentType.APPLICATION_JSON));
        HttpEntity entity = null;
        try {
            entity = talk2service(post);
            SyncResponse res;
            res = new SyncResponse(request.getSubscriber(), readResponse(entity));
            return res;
        } finally {
            if (entity != null) EntityUtils.consume(entity); // Make sure the connection can go back to pool
        }
    }

    /**
     * process a play event
     *
     * @return result of event creation
     */
    public SyncResponse processPlayEvent(SubscriberId subscriber,Link processLink) {
        try {
            return innerProcessPlayEvent(subscriber, processLink);
        } catch (Exception e) {
            return new SyncResponse(subscriber, "Failed2process", e.getMessage());
        }
    }

    private SyncResponse innerProcessPlayEvent(SubscriberId subscriber,Link processLink) throws Exception {

        URI href = new URI(processLink.getHref());
        HttpPut put = new HttpPut(href);

        HttpEntity entity = null;
        try {
            HttpResponse response = execute(put);
//            int httpStatus = extractStatus(response);
//            if(httpStatus !=HttpStatus.SC_NOT_FOUND) {
//
//            }
            entity=response.getEntity();
            SyncResponse res;
            res = new SyncResponse(subscriber, readResponse(entity));
            return res;
        } finally {
            if (entity != null) EntityUtils.consume(entity); // Make sure the connection can go back to pool
        }
    }

}

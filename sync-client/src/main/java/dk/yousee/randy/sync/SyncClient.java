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
        return new URL(String.format("%s/sync/api/event", getConnector().getSyncHost()));
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
        return new URL(String.format("%s/sync/api/play/engagement/%s/position/%s/item/%s/signal/%s"
            , getConnector().getSyncHost()
            ,request.getSubscriber()
            ,request.getPosition()
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
            "                \"mediatype\": \"application/json;charset=UTF-8\",\n" +
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

    /**
     * Create a PmEngagement record in randy-sync
     *
     * @return result of event creation
     */
    public CreatePmResponse createPmEngagement(CreatePmRequest request) {
        try {
            return innerCreatePmEngagement(request);
        } catch (Exception e) {
            return new CreatePmResponse(request.getSubscriber(), "Failed2createPmEngagement", e.getMessage());
        }
    }

    URL generateCreateEngagementUrl(SubscriberId subscriber) throws MalformedURLException {
        return new URL(String.format("%s/sync/api/pm/engagement/subscriber/%s", getConnector().getSyncHost(),subscriber));
    }

    private CreatePmResponse innerCreatePmEngagement(CreatePmRequest request) throws Exception {

        HttpPost post;
        URL href = generateCreateEngagementUrl(request.getSubscriber());
        post = new HttpPost(href.toString());
        String body=String.format("{\n" +
            "      \"forbruger\": \"%s\",\n" +
            "      \"system\":\"%s\",\n" +
            "      \"reference\":\"%s\",\n" +
            "      \"user\":\"%s\",\n" +
            "      \"content\": %s \n" +
            "    }"
            ,request.getSubscriber()
            ,request.getSystem()
            ,request.getReference()
            ,request.getUser()
            ,request.getContent()
        );
        post.setEntity(new StringEntity(body, ContentType.APPLICATION_JSON));
        HttpEntity entity = null;
        try {
            entity = talk2service(post);
            CreatePmResponse res;
            res = new CreatePmResponse(request.getSubscriber(), readResponse(entity));
            return res;
        } finally {
            if (entity != null) EntityUtils.consume(entity); // Make sure the connection can go back to pool
        }
    }


    /**
     * Creates a PM event
     *
     * @return result of event creation
     */
    public SyncResponse createPmEvent(CreatePmRequest request,Long pmId) {
        try {
            return innerCreatePmEvent(request,pmId);
        } catch (Exception e) {
            return new SyncResponse(request.getSubscriber(), "createPmEventFailed", e.getMessage());
        }
    }
    URL generateHref(Long pmId) throws MalformedURLException {
        return new URL(String.format("%s/sync/api/pm/engagement/%s"
            , getConnector().getSyncHost()
            ,pmId
        ));
    }

    private SyncResponse innerCreatePmEvent(CreatePmRequest request,Long pmId) throws Exception {

        HttpPost post;
        URL href = generateCreateUrl();
        post = new HttpPost(href.toString());
        String body=String.format("{\n" +
            "      \"forbruger\": \"%s\",\n" +
            "      \"links\": [{\n" +
            "                \"href\": \"%s\",\n" +
            "                \"mediatype\": \"application/json;charset=UTF-8\",\n" +
            "                \"rel\": \"engagement\"\n" +
            "            }\n" +
            "        ],\n" +
            "      \"system\":\"%s\",\n" +
            "      \"reference\":\"%s\",\n" +
            "      \"user\":\"%s\"\n" +
            "    }"
            ,request.getSubscriber()
            ,generateHref(pmId)
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

}

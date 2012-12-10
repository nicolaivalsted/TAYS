package dk.yousee.randy.dms;

import dk.yousee.randy.base.AbstractClient;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.util.EntityUtils;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * User: aka
 * Date: 06/09/12
 * Time: 23.45
 * Client to access dms
 */
public class DmsClient extends AbstractClient<DmsConnectorImpl> {


    URL generateEventUrl(String customer) throws MalformedURLException {
        return new URL(String.format("%s/dms/customer/%s/event", getConnector().getDmsHost(),customer));
    }

    public EventResponse postEvent(String customer){
        try {
            return makeInvoiceInner(customer);
        } catch (Exception e){
            return new EventResponse(customer,e.getMessage());
        }
    }


    private EventResponse makeInvoiceInner(String customer) throws Exception {
        HttpPost hur;
        URL href=generateEventUrl(customer);
        hur = new HttpPost(href.toString());
        HttpEntity entity = null;
        try {
            HttpResponse rsp = execute(hur);

            int httpStatus = extractStatus(rsp);
            boolean success;
            success = httpStatus == HttpStatus.SC_OK;
            entity = rsp.getEntity();
            String body=readResponse(entity);

            return new EventResponse(customer,success,body);
        } finally {
            if (entity != null) EntityUtils.consume(entity); // Make sure the connection can go back to pool
        }
    }
}

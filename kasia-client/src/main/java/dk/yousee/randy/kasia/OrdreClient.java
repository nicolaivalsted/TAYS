package dk.yousee.randy.kasia;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;

import java.net.URL;

/**
 * User: aka
 * Date: 19/10/12
 * Time: 08.52
 * Ordre handling in kasia II.
 */
public class OrdreClient extends AbstractKasiaClient {


    public InvoiceResponse makeInvoice(InvoiceRequest request)  {
        try {
            return makeInvoiceInner(request);
        } catch (Exception e) {
            String message=String.format("Tried to make Invoice, got exception: %s",e.getMessage());
            return new InvoiceResponse(message,null);
        }
    }

    private InvoiceResponse makeInvoiceInner(InvoiceRequest request) throws Exception {
        HttpPost post;
        URL href=new URL(String.format("%s/ordre/v3", getConnector().getKasiaHost()));
        post = new HttpPost(href.toString());
        post.setHeader(HttpHeaders.ACCEPT, getDefaultMediaType());

        String value=request.printJson();
        post.setEntity(new StringEntity(value, "text/plain", "UTF-8"));
        HttpEntity entity = null;
        try {
            entity = talk2service(post);
            return new InvoiceResponse(null,readResponse(entity));
        } finally {
            if (entity != null) EntityUtils.consume(entity); // Make sure the connection can go back to pool
        }
    }

    public OrderStateResponse queryOrdre(String ordreId)  {
        try {
            return queryOrdreInner(ordreId);
        } catch (Exception e) {
            String message=String.format("Tried to query Order %s, got exception: %s",ordreId,e.getMessage());
            return new OrderStateResponse(message,null);
        }
    }

    private OrderStateResponse queryOrdreInner(String ordreId) throws Exception {
        HttpGet hur;
        URL href=new URL(String.format("%s/ordre/%s", getConnector().getKasiaHost(),ordreId));
        hur = new HttpGet(href.toString());
        hur.setHeader(HttpHeaders.ACCEPT, getDefaultMediaType());
        HttpEntity entity = null;
        try {
            HttpResponse response = execute(hur);
            entity=response.getEntity();
            if(extractStatus(response)== HttpStatus.SC_NOT_FOUND){
                return new OrderStateResponse(String.format("Order %s not found",ordreId),null);
            } else {
                entity = talk2service(hur);
                return new OrderStateResponse(null,readResponse(entity));
            }
        } finally {
            if (entity != null) EntityUtils.consume(entity);
        }
    }


}

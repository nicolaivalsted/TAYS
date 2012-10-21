package dk.yousee.randy.kasia;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
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
//        value=value.replace("\"sikkerhedspakke\"","\"Sikkerhedspakken\"");
//        value=value.replace("\"sikkerhedspakken\"","\"Sikkerhedspakken\"");
        post.setEntity(new StringEntity(value, "text/plain", "UTF-8"));
        HttpEntity entity = null;
        try {
            entity = talk2service(post);
            return new InvoiceResponse(null,readResponse(entity));
        } finally {
            if (entity != null) EntityUtils.consume(entity); // Make sure the connection can go back to pool
        }
    }

}

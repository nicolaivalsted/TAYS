package dk.yousee.randy.kasia;

import dk.yousee.randy.base.HttpPool;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;

import java.net.URL;
import java.nio.charset.Charset;
import java.util.List;

/**
 * User: aka Date: 19/10/12 Time: 08.52 Ordre handling in kasia II.
 */
public class OrdreClient {
    private static final String mediaType = "application/vnd.yousee.kasia2+json;version=1;charset=UTF-8";
    public static final String PREPROD_KASIA_HOST = "http://preprod-kasia.yousee.dk";
    public static final String KASIA_HOST = "http://kasia.yousee.dk";
    private String kasiaHost;
    private HttpPool httpPool;
    
    private final RequestConfig req = RequestConfig.custom().setSocketTimeout(20000).setConnectTimeout(20000).build();

    public OrdreClient() {
    }

    public String getKasiaHost() {
        return kasiaHost;
    }

    public void setKasiaHost(String kasiaHost) {
        this.kasiaHost = kasiaHost;
    }

    public void setHttpPool(HttpPool httpPool) {
        this.httpPool = httpPool;
    }

    public InvoiceResponse makeInvoice(InvoiceRequest request) {
        HttpEntity entity = null;
        try {
            URL href = new URL(String.format("%s/ordre/v3", kasiaHost));
            HttpPost post = new HttpPost(href.toString());
            post.setHeader(HttpHeaders.ACCEPT, mediaType);

            // KASIATWO-2308/TAYS-2386: Identifies resends for kasia, to make them return existing order if they have a match (avoiding duplicate orders)
            if (request.hasRequestId()) {
                post.setHeader("x-kasia2-request-id", request.getRequestId());
            }

            String value = request.printJson().toString();
            post.setEntity(new StringEntity(value, Charset.forName("UTF-8")));
            CloseableHttpResponse httpResponse = httpPool.getClient(req).execute(post);
            entity = httpResponse.getEntity();
            InvoiceResponse invoiceResponse = new InvoiceResponse(null, EntityUtils.toString(entity));
            invoiceResponse.setHttpStatusCode(httpResponse.getStatusLine().getStatusCode());
            return invoiceResponse;
        } catch (Exception e) {
            String message = String.format("Tried to make Invoice, got exception: %s", e.getMessage());
            return new InvoiceResponse(message, null);
        } finally {
            EntityUtils.consumeQuietly(entity); // Make sure the connection can go back to pool
        }
    }

    public OrderStateResponse queryOrdre(String ordreId) {
        HttpEntity entity = null;
        try {
            URL href = new URL(String.format("%s/ordre/%s", kasiaHost, ordreId));
            HttpGet hur = new HttpGet(href.toString());
            hur.setHeader(HttpHeaders.ACCEPT, mediaType);
            HttpResponse response = httpPool.getClient(req).execute(hur);
            entity = response.getEntity();
            if (response.getStatusLine().getStatusCode() == HttpStatus.SC_NOT_FOUND) {
                return new OrderStateResponse(String.format("Order %s not found", ordreId), null);
            } else {
                return new OrderStateResponse(null, EntityUtils.toString(entity));
            }
        } catch (Exception e) {
            String message = String.format("Tried to query Order %s, got exception: %s", ordreId, e.getMessage());
            return new OrderStateResponse(message, null);
        } finally {
            EntityUtils.consumeQuietly(entity);
        }
    }

    /**
     * //http://preprod-kasia.yousee.dk/afsaetning/priser/intet/W GET
     * /afsaetning/priser/<anlaegsid>/<salgskanal>
     * Accept: application/vnd.yousee.kasia2.afsaetning+json;version=1
     *
     * @return list of items + prices
     */
    public PricesResponse prices(List<String> itemKeys) {
        HttpEntity entity = null;
        try {
            URL href = new URL(String.format("%s/afsaetning/priser/intet/K", kasiaHost));
            HttpGet hur = new HttpGet(href.toString());
            hur.setHeader(HttpHeaders.ACCEPT, mediaType);
            HttpResponse response = httpPool.getClient(req).execute(hur);
            entity = response.getEntity();

            return new PricesResponse(itemKeys, null, EntityUtils.toString(entity));
        } catch (Exception e) {
            String message = String.format("Tried to query prices, got exception: %s", e.getMessage());
            return new PricesResponse(itemKeys, message, null);
        } finally {
            EntityUtils.consumeQuietly(entity);
        }
    }
}

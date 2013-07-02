package dk.yousee.randy.voucher;

import dk.yousee.randy.base.HttpPool;
import dk.yousee.randy.base.XmlFiltering;
import java.io.IOException;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;

import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.http.HttpResponse;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.HttpParams;

/**
 * User: aka Date: 06/09/12 Time: 23.45 Client to access voucher
 */
public class VoucherClient {
    private static final Logger LOGGER = Logger.getLogger(VoucherClient.class.getName());
    private HttpPool httpPool;

    public void setHttpPool(HttpPool httpPool) {
        this.httpPool = httpPool;
    }
    private final HttpParams params = new BasicHttpParams();
    public static final String PREPROD_VOUCHER_HOST = "http://192.168.98.10:8080";
    public static final String VOUCHER_HOST = "http://smt-h3106.yousee.dk:8080";
    private String voucherHost;

    public VoucherClient() {
        params.setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 20000);
        params.setParameter(CoreConnectionPNames.SO_TIMEOUT, 20000);
    }

    public String getVoucherHost() {
        return voucherHost;
    }

    public void setVoucherHost(String voucherHost) {
        this.voucherHost = voucherHost;
    }

    String generateBaselUrl() throws MalformedURLException {
        return String.format("%s/voucherService/voucherInterface", voucherHost);
    }

    URL wsdlUrl() throws MalformedURLException {
        return new URL(String.format("%s?wsdl", generateBaselUrl()));
    }

    URL endPoint() throws MalformedURLException {
        return new URL(generateBaselUrl());
    }

    public VoucherResponse consume(VoucherRequest request) {
        try {
            return innerConsume(request);
        } catch (Exception e) {
            return new VoucherResponse("Failed2access", e.getMessage());
        }
    }

    public VoucherResponse innerConsume(VoucherRequest request) throws Exception {
        // build request
        HttpPost post;
        post = new HttpPost(endPoint().toString());
        post.setHeader("accept", "application/soap+xml, application/dime, multipart/related, text/*");
        post.setHeader("Cache-Control", "no-cache");
        post.setHeader("Pragma", "no-cache");
        post.setHeader("SOAPAction", "");
        post.setHeader("Content-Type", "text/xml");

        String body = request.printXml();
        LOGGER.log(Level.WARNING, "Voucher request: {0}", body);
        try {
            post.setEntity(new StringEntity(body, Charset.forName("UTF-8")));
        } catch (Throwable e) {
            String errorMessage = String.format("could not assign entity to HTTP post instance, got error: %s,", e);
            throw new IllegalArgumentException("Request is invalid " + errorMessage);
        }

        HttpEntity entity = null;
        try {
            HttpResponse response = httpPool.getClient(params).execute(post);
            entity = response.getEntity();
            LOGGER.log(Level.INFO, "Status {0}", response.getStatusLine().getStatusCode());
            String xmlResponse = EntityUtils.toString(entity);
            LOGGER.log(Level.WARNING, "Voucher response: {0}", xmlResponse);
            return parseResponse(xmlResponse);
        } finally {
            EntityUtils.consume(entity); // Make sure the connection can go back to pool
        }
    }

    private static final XmlFiltering xmlFilter = new XmlFiltering();

    public VoucherResponse parseResponse(String xml) {
        String returnXml = xmlFilter.filterXml("return", xml);
        String codeStr = xmlFilter.filterXml("code", returnXml);
        int code = codeStr == null ? 0 : Integer.parseInt(codeStr);
        String clientReference = xmlFilter.filterXml("correlator", returnXml);
        String description = xmlFilter.filterXml("description", returnXml);
        String session_id = xmlFilter.filterXml("session_id", returnXml);
        return new VoucherResponse(xml, code, clientReference, description, session_id);
    }
}

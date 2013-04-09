package dk.yousee.randy.voucher;

import dk.yousee.randy.base.AbstractClient;
import dk.yousee.randy.base.XmlFiltering;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * User: aka
 * Date: 06/09/12
 * Time: 23.45
 * Client to access voucher
 */
public class VoucherClient extends AbstractClient<VoucherConnectorImpl> {
    private static final Logger LOGGER = Logger.getLogger(VoucherClient.class.getName());

    String generateBaselUrl() throws MalformedURLException {
        return String.format("%s/voucherService/voucherInterface"
            , getConnector().getVoucherHost());
    }

    URL wsdlUrl() throws MalformedURLException {
        return new URL(String.format("%s?wsdl",generateBaselUrl()));
    }
    URL endPoint() throws MalformedURLException {
        return new URL(generateBaselUrl());
    }

    public VoucherResponse consume(VoucherRequest request) {
        try {
            return innerConsume(request);
        } catch (Exception e){
            return new VoucherResponse("Failed2access",e.getMessage());
        }
    }

    public VoucherResponse innerConsume(VoucherRequest request) throws Exception{
// build request
        HttpPost post;
        post = new HttpPost(endPoint().toString());
        post.setHeader("accept", "application/soap+xml, application/dime, multipart/related, text/*");
//        post.setHeader("Content-Type","text/xml; charset=utf-8"); //??? remove ..
        post.setHeader("Cache-Control", "no-cache");
        post.setHeader("Pragma", "no-cache");
        post.setHeader("SOAPAction", "");

        String body=request.printXml();
        LOGGER.log(Level.WARNING, "Voucher request: {0}", body);
        try {
            post.setEntity(new StringEntity(body, "text/xml", "UTF-8"));
        } catch (Throwable e) {
            String errorMessage = String.format("could not assign entity to HTTP post instance, got error: %s,", e);
            throw new IllegalArgumentException("Request is invalid " + errorMessage);
        }

        HttpEntity entity=null;
        try {
            entity = talk2service(post);
            String xmlResponse=readResponse(entity);
            LOGGER.log(Level.WARNING, "Voucher response: {0}", xmlResponse);
            return parseResponse(xmlResponse);
        } finally {
            if (entity != null) EntityUtils.consume(entity); // Make sure the connection can go back to pool
        }
    }


    public String readWsdl() throws Exception {
        return super.performGet(wsdlUrl());
    }


    private static final XmlFiltering xmlFilter=new XmlFiltering();
    public VoucherResponse parseResponse(String xml) {
        String returnXml=xmlFilter.filterXml("return",xml);
        String codeStr=xmlFilter.filterXml("code",returnXml);
        int code=codeStr==null?0:Integer.parseInt(codeStr);
        String clientReference=xmlFilter.filterXml("correlator",returnXml);
        String description=xmlFilter.filterXml("description",returnXml);
        String session_id=xmlFilter.filterXml("session_id",returnXml);
        return new VoucherResponse(xml,code,clientReference,description,session_id);
    }
}

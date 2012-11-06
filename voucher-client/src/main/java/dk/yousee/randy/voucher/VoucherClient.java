package dk.yousee.randy.voucher;

import dk.yousee.randy.base.AbstractClient;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.util.EntityUtils;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * User: aka
 * Date: 06/09/12
 * Time: 23.45
 * Client to access voucher
 */
public class VoucherClient extends AbstractClient<VoucherConnectorImpl> {



    public VoucherResponse consume(VoucherRequest request) {
        VoucherResponse response=null;



        return response;
    }

    URL generateWsdlUrl() throws MalformedURLException {
        return new URL(String.format("%s/voucherService/voucherInterface?wsdl"
            , getConnector().getVoucherHost()));
    }

    public String readWsdl() throws Exception {
        return super.performGet(generateWsdlUrl());

//        HttpUriRequest hur;
//        URL url = generateHandleUrl();
//
//        hur = new HttpGet(url.toString());
//        HttpEntity entity = null;
//        try {
//            entity = talk2service(hur);
//            return readResponse(entity);
//        } catch (java.net.UnknownHostException e) {
//            HttpHost proxy = getConnector().extractProxy();
//            if (proxy != null || level == 1 || !getConnector().hasAlternativeProxyHost()) {
//                String message = String.format("Tried to fetch handle on URL %s, got unknownHostException %s,", url.toString(), e.getMessage());
//                throw new Exception(message);
//            } else {
//                getConnector().clearClients();
//                getConnector().setProxyHost(getConnector().getAlternativeProxyHost());
//                getConnector().setProxyPort(getConnector().getAlternativeProxyPort());
//                return fetchHandle(1);
//            }
//        } finally {
//            if (entity != null) EntityUtils.consume(entity); // Make sure the connection can go back to pool
//        }
//
//        return null;  //To change body of created methods use File | Settings | File Templates.
    }
}

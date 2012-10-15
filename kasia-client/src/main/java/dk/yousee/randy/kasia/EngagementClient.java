package dk.yousee.randy.kasia;

import dk.yousee.randy.base.AbstractClient;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import org.apache.http.HttpHeaders;
import java.io.InputStream;
import java.lang.Exception;
import java.lang.RuntimeException;
import java.lang.String;
import java.lang.Throwable;
import java.net.URL;

public class EngagementClient extends AbstractClient<KasiaConnectorImpl> {

//    private static final Logger logger = Logger.getLogger(EngagementClient.class);


    public String findEngagement(URL url, String mediatype) throws Exception {

// fire request & parse server response (header)
        DefaultHttpClient client = getConnector().getClient(getOperationTimeout());
        HttpUriRequest hur;
        hur = new HttpGet(url.toString());
        hur.setHeader(HttpHeaders.ACCEPT, mediatype);
//        hur.setHeader(HttpHeaders.ACCEPT_ENCODING,ENCODING);
//        hur.setHeader(HttpHeaders.CONTENT_ENCODING,ENCODING);

        HttpEntity entity = null;
        try {
            String errorMessage;
            int httpStatus;
            try {
                HttpResponse rsp = client.execute(hur);
                httpStatus = rsp.getStatusLine().getStatusCode();
                entity = rsp.getEntity();
                if (httpStatus == HttpStatus.SC_OK) {
                    errorMessage = null;
                } else if (httpStatus == HttpStatus.SC_NOT_ACCEPTABLE) {
                    errorMessage = rsp.getStatusLine().getReasonPhrase();
                } else if (httpStatus == HttpStatus.SC_BAD_REQUEST) {
                    errorMessage = rsp.getStatusLine().getReasonPhrase();
                } else if (httpStatus == HttpStatus.SC_INTERNAL_SERVER_ERROR) {
                    errorMessage = rsp.getStatusLine().getReasonPhrase();
                    String msg = EntityUtils.toString(entity, "UTF-8");
                    if (msg != null) {
                        errorMessage = errorMessage + " msg:" + msg;
                    }
                } else {
                    errorMessage = String.format("Status not handled, %s", rsp.getStatusLine().getReasonPhrase());
                }
            } catch (Throwable e) {
                String message = String.format("could not execute get, got error: %s,", e);
//                logger.fatal(message, e);
                throw new Exception(message);
            }
            if (errorMessage != null) {
                throw new Exception(String.format("status:%s ,phrase:%s", httpStatus, errorMessage));
            }
// read response & parse body
            InputStream is = null;
            String res;
            try {
                is = entity.getContent();
                res = parseInputStream(is);
            } catch (Throwable e) {
                throw new RuntimeException("Tried to read content and parse it", e);
            } finally {
                close(is);
            }
            return res;
        } finally {
            if (entity != null) EntityUtils.consume(entity); // Make sure the connection can go back to pool
        }
    }
}

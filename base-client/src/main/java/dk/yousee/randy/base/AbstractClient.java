package dk.yousee.randy.base;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

/**
 * Created with IntelliJ IDEA.
 * User: aka
 * Date: 06/09/12
 * Time: 23.32
 * Clients extends this
 */
public abstract class AbstractClient<CONNECTOR extends AbstractConnector> {


    private Integer operationTimeout;

    public Integer getOperationTimeout() {
        return operationTimeout;
    }

    public void setOperationTimeout(Integer operationTimeout) {
        this.operationTimeout = operationTimeout;
    }

    private CONNECTOR connector;

    public CONNECTOR getConnector() {
        return connector;
    }

    public void setConnector(CONNECTOR connector) {
        this.connector = connector;
        this.setOperationTimeout(connector.getOperationTimeout());
    }

    /**
     * Execute a get on the specified URL
     * <br/>
     * The objective is to do a simple call to the service without much features - just simple
     *
     * @param url to get data from
     * @return a string with response data
     * @throws Exception when if fails
     */
    protected String performGet(URL url) throws Exception {
        HttpUriRequest hur = new HttpGet(url.toString());
        HttpEntity entity = null;
        try {
            entity = talk2service(hur);
            return readResponse(entity);
        } finally {
            if (entity != null) EntityUtils.consume(entity); // Make sure the connection can go back to pool
        }
    }

    protected HttpResponse execute(HttpUriRequest hur) throws Exception {
        DefaultHttpClient client = getConnector().getClient(getOperationTimeout());
        try {
            HttpResponse rsp;
            rsp = client.execute(hur);
            return rsp;
        } catch (java.net.UnknownHostException e) {
            throw e;
        } catch (Throwable e) {
            String message = String.format("could not execute %s %s got error: %s,", hur.getMethod(),hur.getURI(), e);
            throw new Exception(message);
        }
    }

    /**
     * Build a request and get it fired to the service and return the response
     * <p/>
     * Overload this method if the service makes another response pattern
     *
     * @param hur request instance
     * @return response as entity
     * @throws Exception when there is errors
     */
    protected HttpEntity talk2service(HttpUriRequest hur) throws Exception {

        HttpResponse rsp = execute(hur);
        String errorMessage=extractMessage(rsp);
        if (errorMessage != null) {
            HttpEntity entity = rsp.getEntity();
            if (entity != null) EntityUtils.consume(entity); // Make sure the connection can go back to pool
            throwExceptionWithMessage(rsp, errorMessage);
        }
        return rsp.getEntity();
    }

    /**
     * Throw exception with message
     * @param rsp response that made exception
     * @param errorMessage message to put into exception
     * @throws Exception
     */
    protected void throwExceptionWithMessage(HttpResponse rsp, String errorMessage) throws Exception {
        int httpStatus=extractStatus(rsp);
        throw new Exception(String.format("status:%s ,phrase:%s", httpStatus, errorMessage));
    }

    protected String extractMessage(HttpResponse rsp) throws Exception {
        HttpEntity entity;
        String errorMessage;
        int httpStatus;
        entity = rsp.getEntity();
        httpStatus = extractStatus(rsp);
        if (httpStatus == HttpStatus.SC_OK) {
            errorMessage = null;
        } else if (httpStatus == HttpStatus.SC_CREATED) {
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
        return errorMessage;
    }
    protected int extractStatus(HttpResponse response) {
        return response.getStatusLine().getStatusCode();
    }

    protected String parseInputStream(InputStream is) throws Exception {
        StreamReader sr = new StreamReader();
        String res;
        res = sr.readInputStreamAsString(is);
        return res;
    }


    protected String readResponse(HttpEntity entity) {
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
    }

    protected void close(InputStream is) {
        if (is != null) try {
            is.close();
        } catch (IOException e) {
            System.out.println("AbstractClient, unexpected could not close input stream, message: " + e.getMessage());
        }
    }

}

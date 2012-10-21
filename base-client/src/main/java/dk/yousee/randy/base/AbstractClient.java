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
    /**
     * Build a request and get it fired to the service and return the response
     *
     * Overload this method if the service makes another response pattern
     *
     * @param hur request instance
     * @return response as entity
     * @throws Exception when there is errors
     *
     */
    protected HttpEntity talk2service(HttpUriRequest hur) throws Exception {
        DefaultHttpClient client = getConnector().getClient(getOperationTimeout());
        HttpEntity entity;
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
        } catch (java.net.UnknownHostException e) {
            throw e;
        } catch (Throwable e) {
            String message = String.format("could not execute %s, got error: %s,",hur.getMethod(), e);
            throw new Exception(message);
        }
        if (errorMessage != null) {
            throw new Exception(String.format("status:%s ,phrase:%s", httpStatus, errorMessage));
        }
        return entity;
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
            System.out.println("AbstractClient, unexpected could not close input stream, message: "+ e.getMessage());
        }
    }

}

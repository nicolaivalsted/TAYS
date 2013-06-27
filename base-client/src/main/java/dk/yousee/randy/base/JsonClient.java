package dk.yousee.randy.base;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.nio.charset.Charset;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

/**
 * Refactored from Client in activation-rest. Now uses only JsonElement.
 * @author m27236
 * @author Jacob Lorensen
 */
public class JsonClient {
    private Logger logger;
    private HttpPool httpPool;

    public JsonClient(Logger logger, HttpPool httpPool) {
        this.logger = logger;
        this.httpPool = httpPool;
    }

    /**
     * Get A URI, interpreting it as a JSON object
     *
     * @param uri
     * @return JsonObject obtained from GETing the URI
     * @throws RestClientException
     */
    public JsonElement getUri(URI uri) throws RestClientException {
        return getUri(uri, 2000);
    }

    public JsonElement getUri(URI uri, int timeout) throws RestClientException {
        HttpEntity entity = null;
        try {
            HttpParams p = new BasicHttpParams();
            HttpConnectionParams.setSoTimeout(p, timeout);
            HttpConnectionParams.setConnectionTimeout(p, timeout);
            DefaultHttpClient client = httpPool.getClient(p);
            HttpGet get = new HttpGet(uri);
            HttpResponse response = client.execute(get);
            entity = response.getEntity();
            int status = response.getStatusLine().getStatusCode();
            logger.debug("Uri: " + uri.toString() + " status: " + status);
            if (status == HttpStatus.SC_OK)
                return new JsonParser().parse(new InputStreamReader(entity.getContent(), Charset.forName("UTF-8")));
            if (logger.isDebugEnabled())
                logger.debug("error response: " + EntityUtils.toString(entity, Charset.forName("UTF-8")));
            throw new RestClientException(status, EntityUtils.toString(entity));
        } catch (IOException ex) {
            logger.error(ex.getMessage(), ex);
            throw new RestClientException(HttpStatus.SC_INTERNAL_SERVER_ERROR, "Service Down");
        } finally {
            EntityUtils.consumeQuietly(entity);
        }
    }
    
    /**
     * POST a JSON document, return JSON document result or exception
     *
     * @param uri
     * @param doc
     * @return JsonObject obtained from GETing the URI
     * @throws RestClientException
     */
    public JsonElement postUri(URI uri, JsonObject doc) throws RestClientException{
        return postUri(uri, doc, 5000);
    }

    /**
     * POST a JSON document, return JSON document result or exception
     *
     * @param uri
     * @param doc
     * @return JsonObject obtained from GETing the URI
     * @throws RestClientException
     */
    public JsonElement postUri(URI uri, JsonObject doc, int timeout) throws RestClientException {
        HttpEntity entity = null;
        try {
            HttpParams p = new BasicHttpParams();
            HttpConnectionParams.setSoTimeout(p, timeout);
            HttpConnectionParams.setConnectionTimeout(p, timeout);
            DefaultHttpClient client = httpPool.getClient(p);
            
            HttpPost post = new HttpPost(uri);
            post.setHeader("accept", "application/json");
            post.setHeader("Content-Type", "application/json");
            StringEntity postDocument;
            String s = doc.toString();
            postDocument = new StringEntity(s);
            post.setEntity(postDocument);
            HttpResponse response = client.execute(post);
            entity = response.getEntity();
            int status = response.getStatusLine().getStatusCode();
            logger.debug("Uri: " + uri.toString() + " status: " + status);
            if (status == HttpStatus.SC_CREATED || status == HttpStatus.SC_OK)
                return new JsonParser().parse(new InputStreamReader(entity.getContent(), Charset.forName("UTF-8")));
            if (logger.isDebugEnabled())
                logger.debug("error response: " + EntityUtils.toString(entity, Charset.forName("UTF-8")));
            throw new RestClientException(status, EntityUtils.toString(entity));
        } catch (IOException ex) {
            logger.error(ex.getMessage(), ex);
            throw new RestClientException(HttpStatus.SC_INTERNAL_SERVER_ERROR, "Service Down");
        } finally {
            EntityUtils.consumeQuietly(entity);
        }
    }


    /**
     * PUT a JSON document, return JSON document result or exception
     *
     * @param uri
     * @param doc
     * @return JsonObject obtained from GETing the URI
     * @throws RestClientException
     */
    public JsonElement putUri(URI uri, JsonObject doc, int timeout) throws RestClientException {
        HttpEntity entity = null;
        try {
            HttpParams p = new BasicHttpParams();
            HttpConnectionParams.setSoTimeout(p, timeout);
            HttpConnectionParams.setConnectionTimeout(p, timeout);
            DefaultHttpClient client = httpPool.getClient(p);            
            HttpPut put = new HttpPut(uri);
            put.setHeader("accept", "application/json");
            put.setHeader("Content-Type", "application/json");
            StringEntity putDocument;
            String s = doc.toString();
            putDocument = new StringEntity(s);
            put.setEntity(putDocument);
            HttpResponse response = client.execute(put);
            entity = response.getEntity();
            int status = response.getStatusLine().getStatusCode();
            logger.debug("Uri: " + uri.toString() + " status: " + status);
            if (status == HttpStatus.SC_OK)
                return new JsonParser().parse(new InputStreamReader(entity.getContent(), Charset.forName("UTF-8")));
            if (logger.isDebugEnabled())
                logger.debug("error response: " + EntityUtils.toString(entity, Charset.forName("UTF-8")));
            throw new RestClientException(status, EntityUtils.toString(entity));
        } catch (IOException ex) {
            logger.error(ex.getMessage(), ex);
            throw new RestClientException(HttpStatus.SC_INTERNAL_SERVER_ERROR, "Service Down");
        } finally {
            EntityUtils.consumeQuietly(entity);
        }
    }
}

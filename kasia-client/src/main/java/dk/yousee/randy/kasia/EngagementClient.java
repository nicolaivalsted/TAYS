package dk.yousee.randy.kasia;

import dk.yousee.randy.base.HttpPool;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.util.EntityUtils;

import java.net.URL;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.params.HttpParams;

public class EngagementClient {
    private final HttpParams params = new BasicHttpParams();
    private HttpPool httpPool;

    public EngagementClient() {
        params.setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 20000);
        params.setParameter(CoreConnectionPNames.SO_TIMEOUT, 20000);
    }

    public void setHttpPool(HttpPool httpPool) {
        this.httpPool = httpPool;
    }

    public String findEngagement(URL url, String mediatype) throws Exception {
        HttpGet hur = new HttpGet(url.toString());
        hur.setHeader(HttpHeaders.ACCEPT, mediatype);
        HttpEntity entity = null;
        try {
            entity = httpPool.getClient(params).execute(hur).getEntity();
            return EntityUtils.toString(entity);
        } finally {
            EntityUtils.consumeQuietly(entity); // Make sure the connection can go back to pool
        }
    }
}

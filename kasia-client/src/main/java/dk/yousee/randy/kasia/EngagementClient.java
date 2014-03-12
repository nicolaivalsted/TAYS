package dk.yousee.randy.kasia;

import dk.yousee.randy.base.HttpPool;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;

import java.net.URL;
import org.apache.http.client.config.RequestConfig;

public class EngagementClient {

    private HttpPool httpPool;

    public EngagementClient() {
    }

    public void setHttpPool(HttpPool httpPool) {
        this.httpPool = httpPool;
    }

    public String findEngagement(URL url, String mediatype) throws Exception {
        HttpGet hur = new HttpGet(url.toString());
        hur.setHeader(HttpHeaders.ACCEPT, mediatype);
        HttpEntity entity = null;
        RequestConfig req = RequestConfig.custom().setSocketTimeout(20000).setConnectTimeout(20000).build();
        try {
            entity = httpPool.getClient(req).execute(hur).getEntity();
            return EntityUtils.toString(entity);
        } finally {
            EntityUtils.consumeQuietly(entity); // Make sure the connection can go back to pool
        }
    }
}

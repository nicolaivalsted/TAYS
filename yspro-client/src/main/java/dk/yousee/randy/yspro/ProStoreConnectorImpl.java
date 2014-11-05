package dk.yousee.randy.yspro;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dk.yousee.randy.base.HttpPool;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

/**
 * Created with IntelliJ IDEA. User: aka Date: 09/04/12 Time: 14.46 Class that helps managing HttpClient
 */
public class ProStoreConnectorImpl {
	private static final Logger log = Logger.getLogger(ProStoreConnectorImpl.class);
	public static final String DEV_YSPRO_HOST = "http://ysprodev.yousee.dk";
	public static final String TEST_YSPRO_HOST = "http://ysprotest.yousee.idk";
	public static final String YSPRO_HOST = "http://yspro.yousee.dk";
	public static final String NEW_YSPRO_HOST = "https://yspro3.yousee.dk";
	private String systemPassword;
	private String ysProHost;
	private String systemLogin = "RANDY";
	private HttpPool pool; //set with singleton in spring
	private final RequestConfig req;
	/**
	 * HandleId gets updated regularly while talking to YsPro, and is shared by all threads accessing the same object;
	 * thus we volatile it and protect the getHandleId and setHandleId with synchronized so we guarantee that only one
	 * thread updates it at any one time AND that all threads will see the latest updated value. All accesses of
	 * handleId *must* go through the getHandleId() and setHandleId() methods.
	 */
	private volatile String handleId;

	public ProStoreConnectorImpl(int socketTimeout, int connTimeout) {
		req = RequestConfig.custom().setSocketTimeout(socketTimeout).setConnectTimeout(connTimeout).build();
	}

	public HttpPool getPool() {
		return pool;
	}

	public void setPool(HttpPool pool) {
		this.pool = pool;
	}

	private CloseableHttpClient getClient() {
		return pool.getClient(req);
	}

	public String getYsProHost() {
		return ysProHost;
	}

	public void setYsProHost(String ysProHost) {
		this.ysProHost = ysProHost;
	}

	public String getSystemLogin() {
		return systemLogin;
	}

	public void setSystemLogin(String systemLogin) {
		this.systemLogin = systemLogin;
	}

	public String getSystemPassword() {
		return systemPassword;
	}

	public void setSystemPassword(String systemPassword) {
		this.systemPassword = systemPassword;
	}

	private synchronized String getHandleId() {
		return handleId;
	}

	private synchronized void setHandleId(String handleId) {
		this.handleId = handleId;
	}

	public ProStoreResponse executeYsPro(URI url, List<NameValuePair> params) throws YsProException {
		return executeYsPro(url, params, ProStoreResponse.class);
	}

	/**
	 * YsPro protocol handling&mdash;handles handleId, request timeouts, handleId refhresh and retries along with
	 * parsing of the YsPro response and http response codes
	 *
	 * @param <C>
	 * @param url
	 * @param params
	 * @param clazz
	 * @return
	 * @throws YsProException
	 */
	public <C extends ProStoreResponse> C executeYsPro(URI url, List<NameValuePair> params, Class<C> clazz) throws YsProException {
		int retries;
		try {
			for (retries = 0; retries < 2; retries++) {
				HttpPost post = new HttpPost(url);
				if (getHandleId() == null) 
					refreshHandle();
				params.add(new BasicNameValuePair("HandleID", getHandleId()));
				post.setEntity(new UrlEncodedFormEntity(params, Charset.forName("UTF-8")));
				// original execute
				HttpUriRequest request = post;
				String responseString = executeHttp(request);
				C proStoreResponse = clazz.getConstructor(String.class).newInstance(responseString);
				log.debug("ProStore response: " + proStoreResponse.toString());
				if (proStoreResponse.getStatus() == 50) {
					refreshHandle();
					continue; // retry on handle expiry
				}
				return proStoreResponse;
			}
			throw new YsProException("Did not get an YsPro handleId in " + retries + " attempts");
		} catch (NoSuchMethodException e) {
			throw new YsProException(clazz.getName() + " must have a one-arg constructor with a String argument", e);
		} catch (InstantiationException e) {
			throw new YsProException(clazz.getName() + " must have a one-arg constructor with a String argument", e);
		} catch (IllegalAccessException e) {
			throw new YsProException(clazz.getName() + " must have a one-arg constructor with a String argument", e);
		} catch (IllegalArgumentException e) {
			throw new YsProException(clazz.getName() + " must have a one-arg constructor with a String argument", e);
		} catch (InvocationTargetException e) {
			throw new YsProException(clazz.getName() + " must have a one-arg constructor with a String argument", e);
		}
	}

	private void refreshHandle() throws YsProException {
		URI uri;
		try {
			uri = new URI(String.format("%s/GetHandle.php", getYsProHost()));
		} catch (URISyntaxException ex) {
			throw new YsProException("URI syntax in getHandle", ex);
		}
		HttpPost post = new HttpPost(uri);
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("SystemLogin", getSystemLogin()));
		params.add(new BasicNameValuePair("SystemPassword", getSystemPassword()));
		post.setEntity(new UrlEncodedFormEntity(params, Charset.forName("UTF-8")));
		JsonObject res = new JsonParser().parse(executeHttp(post)).getAsJsonObject();
		String newHandle = res.getAsJsonObject("Data").get("HandleID").getAsString();
		setHandleId(newHandle);
	}

	private String executeHttp(HttpUriRequest request) throws YsProException {
		HttpEntity entity = null;
		try {
			log.debug("Trying execute url: " + request.getURI());
			HttpResponse response = getClient().execute(request);
			int statusSode = response.getStatusLine().getStatusCode();
			log.debug("Executed url with status: " + statusSode);
			entity = response.getEntity();
			if (statusSode == HttpStatus.SC_OK) {
				String responseString = EntityUtils.toString(entity, "UTF-8");
				return responseString;
			} else {
				log.info("YsPro backend fail! " + statusSode);
				throw new YsProException(new YsProErrorVO(statusSode, EntityUtils.toString(entity, "UTF-8")));
			}
		} catch (IOException ioe) {
			log.error(ioe.getMessage(), ioe);
			throw new YsProException("Yspro IOexception error", ioe);
		} finally {
			EntityUtils.consumeQuietly(entity);
		}
	}

}

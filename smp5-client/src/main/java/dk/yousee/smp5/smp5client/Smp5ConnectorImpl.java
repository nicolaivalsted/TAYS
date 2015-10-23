package dk.yousee.smp5.smp5client;

import java.net.MalformedURLException;
import java.net.URL;

import org.apache.http.HttpHost;

import sun.misc.BASE64Encoder;
import dk.yousee.randy.base.AbstractConnector;

public class Smp5ConnectorImpl extends AbstractConnector {
	public static final int DEFAULT_OPERATION_TIMEOUT = 100000;
	private String url;
	private URL url2;
	private String smpHost;
	private String username;
	private String password;

	public Smp5ConnectorImpl() {
		super();
		setOperationTimeout(DEFAULT_OPERATION_TIMEOUT);
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
		try {
			url2 = new URL(url);
		} catch (MalformedURLException e) {
			e.printStackTrace();
			throw new IllegalArgumentException(String.format(
					"Could not parse URL %s, got exception %s", url,
					e.getMessage()));
		}
	}

	public String getSmpHost() {
		return smpHost;
	}

	public void setSmpHost(String smpHost) {
		this.smpHost = smpHost;
		setUrl(smpHost + "/SmpXmlOrderApi/xmlorder");
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public HttpHost extractHttpHost() {
		HttpHost host;
		host = new HttpHost(url2.getHost(), url2.getPort(), url2.getProtocol());
		return host;
	}

	public String extractUri() {
		return url2.getPath();
	}

	public String connectInfo() {
		StringBuilder sb = new StringBuilder();
		if (getProxyHost() != null && !"none".equals(getProxyHost())
				&& !"null".equals(getProxyHost())) {
			sb.append("proxyHost=").append(getProxyHost());
			sb.append(",proxyPort=").append(getProxyPort()).append("\n");
		}
		sb.append(" username=").append(getUsername());
		if (getConnectionTimeout() != 0) {
			sb.append(",defaultConnectionTimeout=")
					.append(getConnectionTimeout()).append("\n");
		}
		if (getOperationTimeout() != 0) {
			sb.append(",operationTimeout=").append(getOperationTimeout())
					.append("\n");
		}
		return sb.toString();
	}

	public URL getURL() {
		return url2;
	}

	public String encodeBasic() {
		return encodeBasic(getUsername(), getPassword());
	}

	public String encodeBasic(String userName, String password) {
		byte[] encodedPassword = (userName + ":" + password).getBytes();
		BASE64Encoder encoder = new BASE64Encoder();
		String val;
		val = "Basic " + encoder.encode(encodedPassword);
		return val;
	}

}

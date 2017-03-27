package dk.yousee.smp5.smp5client;

import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.bind.DatatypeConverter;

import org.apache.http.HttpHost;

import dk.yousee.randy.base.AbstractConnector;

@SuppressWarnings("deprecation")
public class Smp5ConnectorImpl extends AbstractConnector {
	public static final int DEFAULT_OPERATION_TIMEOUT = 100000;
	private String url;
	private URL url2;
	private String smp5Host;
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

	public String getSmp5Host() {
		return smp5Host;
	}

	public void setSmp5Host(String smp5Host) {
		this.smp5Host = smp5Host;
		setUrl(smp5Host + "SmpXmlOrderApi/xmlorder");
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
		String val;
		val = "Basic " + DatatypeConverter.printBase64Binary(encodedPassword);;
		return val;
	}

}

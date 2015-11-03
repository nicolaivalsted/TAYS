package dk.yousee.smp5.smp5client;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.rmi.RemoteException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import dk.yousee.randy.base.AbstractClient;

/**
 * Created with IntelliJ IDEA. User: aka Date: 09/04/12 Time: 14.31 Client that
 * uses HTTP client to access webservice
 */
class HttpSoapClientImpl extends AbstractClient<Smp5ConnectorImpl> implements
		Smp5Client {

	private static Logger logger = Logger.getLogger(HttpSoapClientImpl.class);

	public HttpSoapClientImpl(Smp5ConnectorImpl connector) {
		setConnector(connector);
	}

	public Integer getDefaultOperationTimeout() {
		return getConnector().getOperationTimeout();
	}

	/**
	 * Perform operation
	 *
	 * @param xmlRequest
	 *            input
	 * @return result
	 * @throws RemoteException
	 */
	public String executeXml(String xmlRequest, Integer operationTimeout)
			throws Exception {

		// build request
		HttpPost post;
		post = new HttpPost(getConnector().getUrl());
		post.setHeader("accept",
				"application/soap+xml, application/dime, multipart/related, text/*");
		post.setHeader("Cache-Control", "no-cache");
		post.setHeader("Pragma", "no-cache");
		post.setHeader("SOAPAction", "");
		post.setHeader("Authorization", getConnector().encodeBasic());
		String escapedXml = StringEscapeUtils.escapeXml(xmlRequest);
		String body = String
				.format("<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
						+ "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">"
						+ "<soapenv:Body>"
						+ "<ns1:executeXml soapenv:encodingStyle=\"UTF-8\" xmlns:ns1=\"http://www.sigma-systems.com/schemas/3.1/SmpXmlOrderAPI\">"
						+ "<xmlRequest xsi:type=\"xsd:string\">%s</xmlRequest>"
						+ "</ns1:executeXml>" + "</soapenv:Body>"
						+ "</soapenv:Envelope>", escapedXml);
		FileOutputStream fop = null;
		File file;
		String content = body;
		try {

			file = new File("C:\\Users\\IT People\\Downloads\\smpSoap.xml");
			fop = new FileOutputStream(file);

			// if file doesnt exists, then create it
			if (!file.exists()) {
				file.createNewFile();
			}

			// get the content in bytes
			byte[] contentInBytes = content.getBytes();

			fop.write(contentInBytes);
			fop.flush();
			fop.close();

			System.out.println("Done");

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (fop != null) {
					fop.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		try {
			post.setEntity(new StringEntity(body, "text/xml", "UTF-8"));
		} catch (Throwable e) {
			String errorMessage = String
					.format("could not assign entity to HTTP post instance, got error: %s,",
							e);
			logger.fatal(errorMessage, e);
			throw new IllegalArgumentException("Request is invalid "
					+ errorMessage);
		}
		HttpHost target = getConnector().extractHttpHost();

		// fire request & parse server response (header)
		DefaultHttpClient client = getConnector().getClient(operationTimeout);
		HttpEntity entity = null;
		try {
			String errorMessage;
			int httpStatus;
			try {
				HttpResponse rsp = client.execute(target, post);
				httpStatus = rsp.getStatusLine().getStatusCode();
				entity = rsp.getEntity();
				if (httpStatus == HttpStatus.SC_OK) {
					errorMessage = null;
				} else if (httpStatus == HttpStatus.SC_UNAUTHORIZED) {
					errorMessage = rsp.getStatusLine().getReasonPhrase();
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
					errorMessage = String.format("Status not handled, %s", rsp
							.getStatusLine().getReasonPhrase());
				}
			} catch (Throwable e) {
				String message = String.format(
						"could not execute post, got error: %s,", e);
				logger.fatal(message, e);
				throw new Exception(message);
			}
			if (errorMessage != null) {
				throw new Exception(String.format("status:%s ,phrase:%s",
						httpStatus, errorMessage));
			}
			InputStream is = null;
			String res;
			is = entity.getContent();
			res = parseInputStream(is);
			return res;
		} finally {
			if (entity != null)
				EntityUtils.consume(entity); // Make sure the connection can go
												// back to pool
		}
	}

	protected String parseInputStream(InputStream is) {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		Document dom;
		try {
			DocumentBuilder db = dbf.newDocumentBuilder();
			dom = db.parse(is);
		} catch (ParserConfigurationException pce) {
			String message = String.format(
					"could not configure parser, got error: %s,", pce);
			logger.fatal(message, pce);
			throw new RuntimeException(message);
		} catch (SAXException se) {
			String message = String.format(
					"could not SAXparse , got error: %s,", se);
			logger.fatal(message, se);
			throw new RuntimeException(message);
		} catch (IOException ioe) {
			String message = String.format("could not read inputstream: %s,",
					ioe);
			logger.fatal(message, ioe);
			throw new RuntimeException(message);
		}
		Element envelope = dom.getDocumentElement();
		if (envelope == null)
			throw new RuntimeException("parse found no 'envelope'");
		return parseEnvelope(envelope);
	}

	private String parseEnvelope(Element envelope) {
		NodeList nodes = envelope.getChildNodes();
		int len = nodes.getLength();
		for (int index = 0; index < len; index++) {
			Node node = nodes.item(index);
			if (node.getNodeName().endsWith("Body")) {
				return parseBodyNode(node);
			}
		}
		throw new RuntimeException(String.format(
				"no 'Body' node found in envelope: %s", envelope));
	}

	private String parseBodyNode(Node node) {
		Node executeXmlResponse = node.getFirstChild();
		if (executeXmlResponse == null)
			throw new RuntimeException(String.format(
					"'Body' is expected to contain executeXmlResponse: %s",
					node));
		return parseExecuteXmlResponse(executeXmlResponse);
	}

	private String parseExecuteXmlResponse(Node executeXmlResponse) {
		Node xmlResponse = executeXmlResponse.getFirstChild();
		if (xmlResponse == null)
			throw new RuntimeException(
					String.format(
							"'executeXmlResponse' is expected to contain xmlResponse: %s",
							executeXmlResponse));
		String value;
		value = xmlResponse.getTextContent();
		return value;
	}
}

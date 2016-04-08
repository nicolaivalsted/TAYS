package dk.yousee.smp5.smp5client;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.rmi.RemoteException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.httpclient.Header;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.RequestEntity;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.http.HttpStatus;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import dk.yousee.randy.base.AbstractClient;

/**
 * Created with IntelliJ IDEA. User: aka Date: 09/04/12 Time: 14.31 Client that
 * uses HTTP client to access webservice
 */
class HttpSoapClientImpl extends AbstractClient<Smp5ConnectorImpl> implements Smp5Client {

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
	public String executeXml(String xmlRequest, Integer operationTimeout) throws Exception {

		HttpClient httpclient = new HttpClient();

		PostMethod postMethod = null;
		postMethod = new PostMethod(getConnector().getUrl());
//		postMethod.setRequestHeader("accept", "application/soap+xml;charset=UTF-8, application/dime, multipart/related, text/*");
		postMethod.setRequestHeader("SOAPAction", "");
		//comment to deploy
		postMethod.setRequestHeader("Content-Type", "application/soap+xml;charset=UTF-8");
		postMethod.setRequestHeader("Authorization", getConnector().encodeBasic());

		String body = String
				.format("<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\"  xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">"
						+ "<soapenv:Body>%s </soapenv:Body>  </soapenv:Envelope>", xmlRequest);

		try {
			RequestEntity requestEntity = new StringRequestEntity(body);
			postMethod.setRequestEntity(requestEntity);
		} catch (Throwable e) {
			String errorMessage = String.format("could not assign entity to HTTP post instance, got error: %s,", e);
			logger.fatal(errorMessage, e);
			throw new IllegalArgumentException("Request is invalid " + errorMessage);
		}

		try {
			String errorMessage;
			int httpStatus;
			try {
				httpStatus = httpclient.executeMethod(postMethod);
				if (httpStatus == HttpStatus.SC_OK) {
					errorMessage = null;
				} else if (httpStatus == HttpStatus.SC_UNAUTHORIZED) {
					errorMessage = postMethod.getStatusLine().getReasonPhrase();
				} else if (httpStatus == HttpStatus.SC_NOT_ACCEPTABLE) {
					errorMessage = postMethod.getStatusLine().getReasonPhrase();
				} else if (httpStatus == HttpStatus.SC_BAD_REQUEST) {
					errorMessage = postMethod.getStatusLine().getReasonPhrase();
				} else if (httpStatus == HttpStatus.SC_INTERNAL_SERVER_ERROR) {
					errorMessage = postMethod.getStatusLine().getReasonPhrase();
					errorMessage = null;
				} else {
					errorMessage = String.format("Status not handled, %s", postMethod.getStatusLine().getReasonPhrase());
				}
			} catch (Throwable e) {
				String message = String.format("could not execute post, got error: %s,", e);
				logger.fatal(message, e);
				throw new Exception(message);
			}
			if (errorMessage != null) {
				throw new Exception(String.format("status:%s ,phrase:%s", httpStatus, errorMessage));
			}
			String res = postMethod.getResponseBodyAsString();
			String toReturn;
			try {
				toReturn = parseInputStream(res);
			} catch (Throwable e) {
				throw new RuntimeException("Tried to read content and parse it", e);
			}
			return toReturn;
		} finally {
			try {
				if (postMethod != null) {
					postMethod.releaseConnection();
					postMethod = null;
				}
			} catch (Throwable throwable) {
				logger.fatal("Exception occurred while closing the connection : " + throwable.toString());
			}
		}
	}

	protected String parseInputStream(String response) {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		Document dom = null;
		try {
			DocumentBuilder db = dbf.newDocumentBuilder();
			InputSource is = new InputSource(new StringReader(response));
			dom = db.parse(is);
		} catch (ParserConfigurationException pce) {
			String message = String.format("could not configure parser, got error: %s,", pce);
			logger.fatal(message, pce);
			throw new RuntimeException(message);
		} catch (SAXException se) {
			String message = String.format("could not SAXparse , got error: %s,", se);
			logger.fatal(message, se);
			throw new RuntimeException(message);
		} catch (IOException ioe) {
			String message = String.format("could not read inputstream: %s,", ioe);
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
		throw new RuntimeException(String.format("no 'Body' node found in envelope: %s", envelope));
	}

	private String parseBodyNode(Node node) {
		Node executeXmlResponse = node.getFirstChild();
		if (executeXmlResponse == null) {
			throw new RuntimeException(String.format("'Body' is expected to contain executeXmlResponse: %s", node));
		}
		return parseExecuteXmlResponse(executeXmlResponse);
	}

	private String parseExecuteXmlResponse(Node executeXmlResponse) {
		String responseName = executeXmlResponse.getNodeName();
		StringWriter writer = new StringWriter();
		Node toPerform;
		String xml;
		try {
			Transformer transformer = TransformerFactory.newInstance().newTransformer();
			if (responseName.endsWith("Fault")) {
				toPerform = executeXmlResponse.getLastChild().getFirstChild();
			} else {
				toPerform = executeXmlResponse;
			}
			transformer.transform(new DOMSource(toPerform), new StreamResult(writer));
			xml = writer.toString();
		} catch (Exception e) {
			throw new RuntimeException(String.format("'executeXmlResponse' is expected to contain xmlResponse: %s", executeXmlResponse));
		}
		return xml;
	}

}

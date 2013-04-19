package dk.yousee.smp.smpclient;

import dk.yousee.randy.base.AbstractClient;
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

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.rmi.RemoteException;

/**
 * Created with IntelliJ IDEA.
 * User: aka
 * Date: 09/04/12
 * Time: 14.31
 * Client that uses HTTP client to access webservice
 */
class HttpSoapClientImpl extends AbstractClient<SmpConnectorImpl> implements SmpClient {

    private static Logger logger = Logger.getLogger(HttpSoapClientImpl.class);

//    private SmpConnectorImpl connector;

    public HttpSoapClientImpl(SmpConnectorImpl connector) {
        setConnector(connector);
//        this.connector = connector;
    }

//    private SmpConnectorImpl getConnector() {
//        return connector;
//    }


// below is demo from Apache HTTP client
//POST /SmpXmlOrderApi/xmlorder HTTP/1.1 --> different 1.0
//SOAPAction:                            --> not ""
//Connection: Keep-Alive                 --> not original
//User-Agent: Apache-HttpClient/4.1.3 (java 1.5) --> what it is...
//
//<?xml version="1.0" encoding="UTF-8"?>
//<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:xsd="http://www.w3.org/2001/XMLSchema" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
//<soapenv:Body>
//<ns1:executeXml soapenv:encodingStyle="UTF-8" xmlns:ns1="http://www.sigma-systems.com/schemas/3.1/SmpXmlOrderAPI">
//<xmlRequest xsi:type="xsd:string">&lt;hej/&gt;</xmlRequest>
//</ns1:executeXml>
//</soapenv:Body>
//</soapenv:Envelope>

// below is a sample of HTTP POST content collected from apache-AXIS
//POST /SmpXmlOrderApi/xmlorder HTTP/1.0
//Content-Type: text/xml; charset=utf-8
//Accept: application/soap+xml, application/dime, multipart/related, text/*
//User-Agent: Axis/1.4
//Host: localhost:8010
//Cache-Control: no-cache
//Pragma: no-cache
//SOAPAction: ""
//Content-Length: 1505
//Authorization: Basic c2FtcC5jc3JhMTpwd2NzcmEx
//
//<?xml version="1.0" encoding="UTF-8"?>
//<soapenv:Envelope xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/" xmlns:xsd="http://www.w3.org/2001/XMLSchema" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
//<soapenv:Body>
//<ns1:executeXml soapenv:encodingStyle="UTF-8" xmlns:ns1="http://www.sigma-systems.com/schemas/3.1/SmpXmlOrderAPI">
//<xmlRequest xsi:type="xsd:string">
//&lt;getEntityByKeyRequest cascadeLoading=&quot;true&quot; xmlns=&quot;http://www.sigma-systems.com/schemas/3.1/SmpCBECoreSchema&quot; xmlns:xsi=&quot;http://www.w3.org/2001/XMLSchema-instance&quot; xmlns:com=&quot;http://java.sun.com/products/oss/xml/Common&quot; xmlns:smp=&quot;http://www.sigma-systems.com/schemas/3.1/SmpCommonValues&quot;&gt;
//&lt;entityKeyLst&gt;
//&lt;entityKey xsi:type=&quot;smp:EntityKeyType&quot; xmlns:smp=&quot;http://www.sigma-systems.com/schemas/3.1/SmpCBECoreSchema&quot;&gt;
//&lt;com:type&gt;SubSpec:-&lt;/com:type&gt;
//&lt;nameQuery&gt;
//&lt;smp1:queryName xmlns:smp1=&quot;http://www.sigma-systems.com/schemas/3.1/SmpCommonValues&quot;&gt;get_id_by_parm&lt;/smp1:queryName&gt;
//&lt;smp1:inputParamList xmlns:smp1=&quot;http://www.sigma-systems.com/schemas/3.1/SmpCommonValues&quot;&gt;
//&lt;smp1:param name=&quot;acct&quot;&gt;608252633&lt;/smp1:param&gt;
//&lt;/smp1:inputParamList&gt;
//&lt;/nameQuery&gt;
//&lt;/entityKey&gt;
//&lt;/entityKeyLst&gt;
//&lt;/getEntityByKeyRequest&gt;
//</xmlRequest>
//</ns1:executeXml>
//</soapenv:Body>
//</soapenv:Envelope>

    public Integer getDefaultOperationTimeout() {
        return getConnector().getOperationTimeout();
    }

    /**
     * Perform operation
     *
     * @param xmlRequest input
     * @return result
     * @throws RemoteException
     */
    public String executeXml(String xmlRequest,Integer operationTimeout) throws Exception {

// build request
        HttpPost post;
        post = new HttpPost(getConnector().getUrl());
        post.setHeader("accept", "application/soap+xml, application/dime, multipart/related, text/*");
//        post.setHeader("Content-Type","text/xml; charset=utf-8"); //??? remove ..
        post.setHeader("Cache-Control", "no-cache");
        post.setHeader("Pragma", "no-cache");
        post.setHeader("SOAPAction", "");
        post.setHeader("Authorization", getConnector().encodeBasic());
        String escapedXml = StringEscapeUtils.escapeXml(xmlRequest);
        String body = String.format(
            "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
                "<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:xsd=\"http://www.w3.org/2001/XMLSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\">" +
                "<soapenv:Body>" +
                "<ns1:executeXml soapenv:encodingStyle=\"UTF-8\" xmlns:ns1=\"http://www.sigma-systems.com/schemas/3.1/SmpXmlOrderAPI\">" +
                "<xmlRequest xsi:type=\"xsd:string\">%s</xmlRequest>" +
                "</ns1:executeXml>" +
                "</soapenv:Body>" +
                "</soapenv:Envelope>", escapedXml);
        System.out.println("Sending soap xml: " + body);
        try {
            post.setEntity(new StringEntity(body, "text/xml", "UTF-8"));
        } catch (Throwable e) {
            String errorMessage = String.format("could not assign entity to HTTP post instance, got error: %s,", e);
            logger.fatal(errorMessage, e);
            throw new IllegalArgumentException("Request is invalid " + errorMessage);
        }
        HttpHost target = getConnector().extractHttpHost();

// fire request & parse server response (header)
        DefaultHttpClient client = getConnector().getClient(operationTimeout);
        HttpEntity entity=null;
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
                    String msg=EntityUtils.toString(entity, "UTF-8");
                    if(msg!=null){
                        errorMessage= errorMessage+" msg:"+msg;
                    }
                } else {
                    errorMessage = String.format("Status not handled, %s",rsp.getStatusLine().getReasonPhrase());
                }
            } catch (Throwable e) {
                String message = String.format("could not execute post, got error: %s,", e);
                logger.fatal(message, e);
                throw new Exception(message);
            }
            if (errorMessage != null) {
                throw new Exception(String.format("status:%s ,phrase:%s",httpStatus,errorMessage));
            }
// read response & parse body
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
        } finally {
            if(entity!=null)EntityUtils.consume(entity); // Make sure the connection can go back to pool
        }
    }

//    private void close(InputStream is) {
//        if (is != null) try {
//            is.close();
//        } catch (IOException e) {
//            logger.warn("unexptected could not close input stream", e);
//        }
//    }

// Response looks like this ....
//<?xml version="1.0" encoding="utf-8" standalone="yes"?>
//<env:Envelope xmlns:soapenc="http://schemas.xmlsoap.org/soap/encoding/"
//xmlns:xsd="http://www.w3.org/2001/XMLSchema"
//xmlns:env="http://schemas.xmlsoap.org/soap/envelope/"
//xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
//<env:Header></env:Header>
//<env:Body env:encodingStyle="http://schemas.xmlsoap.org/soap/encoding/">
//<m:executeXmlResponse xmlns:m="http://www.sigma-systems.com/schemas/3.1/SmpXmlOrderAPI">
//<xmlResponse xsi:type="xsd:string">
//&lt; an encoded string ... &gt;
//</xmlResponse>
//</m:executeXmlResponse>
//</env:Body>
//</env:Envelope>
    protected String parseInputStream(InputStream is) {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        Document dom;
        try {
            DocumentBuilder db = dbf.newDocumentBuilder();
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
        if (envelope == null) throw new RuntimeException("parse found no 'envelope'");
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
        if (executeXmlResponse == null)
            throw new RuntimeException(String.format("'Body' is expected to contain executeXmlResponse: %s", node));
        return parseExecuteXmlResponse(executeXmlResponse);
    }

    private String parseExecuteXmlResponse(Node executeXmlResponse) {
        Node xmlResponse = executeXmlResponse.getFirstChild();
        if (xmlResponse == null)
            throw new RuntimeException(String.format("'executeXmlResponse' is expected to contain xmlResponse: %s", executeXmlResponse));
        String value;
        value = xmlResponse.getTextContent();
        return value;
    }
}

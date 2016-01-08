package dk.yousee.smp5.smp5client;

import java.util.Properties;

import javax.jms.JMSException;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueConnectionFactory;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.log4j.Logger;

import dk.yousee.randy.base.AbstractClient;

/**
 * @author m64746
 *
 *         Date: 04/01/2016 Time: 12:01:57
 */
public class JMSClientImpl extends AbstractClient<Smp5ConnectorImpl> implements Smp5Client {

	private static Logger logger = Logger.getLogger(JMSClientImpl.class);

	public JMSClientImpl(Smp5ConnectorImpl connector) {
		setConnector(connector);
	}
	
	public Integer getDefaultOperationTimeout() {
		return getConnector().getOperationTimeout();
	}

	public String executeXml(String xmlRequest, Integer operationTimeout)throws Exception {
		Context jndiContext = null;
		QueueConnectionFactory queueConnectionFactory = null;
		QueueConnection queueConnection = null;
		QueueSession queueSession = null;
		Queue queue = null;
		QueueSender queueSender = null;
		TextMessage message = null;
		Properties props = new Properties();

		try {
			props.put(Context.INITIAL_CONTEXT_FACTORY, "org.jboss.naming.remote.client.InitialContextFactory");
			props.put(Context.PROVIDER_URL, "remote://provisioning-qa.yousee.dk");
			props.put(Context.SECURITY_PRINCIPAL, "system");
			props.put(Context.SECURITY_CREDENTIALS, "dem0@dmin");
//			props.put(Context.URL_PKG_PREFIXES, "org.jboss.ejb.client.naming");
			logger.error("properties created");
			jndiContext = new InitialContext(props);
			logger.error("initial context created");
			
		} catch (NamingException e) {
			logger.error("Could not create JNDI API " + "context: " + e.toString());
			return "FAIL";
		}
		try {
			queueConnectionFactory = (QueueConnectionFactory) jndiContext.lookup("QueueConnectionFactory");
			logger.error("queueConnectionFactory created");
			queue = (Queue) jndiContext.lookup("JSR264XmlRequestQueue");
		} catch (NamingException e) {
			logger.error("JNDI API lookup failed: " + e.toString());
			return "FAIL";
		}
		try {
			queueConnection = queueConnectionFactory.createQueueConnection();
			queueSession = queueConnection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
			queueSender = queueSession.createSender(queue);
			message = queueSession.createTextMessage();

			message.setText(xmlRequest);
			queueSender.send(message);
		} catch (JMSException e) {
			logger.error("Exception occurred: " + e.toString());
			return "FAIL";
		} catch (Exception ex) {
			ex.printStackTrace();
			return "FAIL";
		} finally {
			if (queueConnection != null) {
				try {
					queueConnection.close();
				} catch (JMSException e) {
				}
			}
			System.exit(0);
		}
		return "OK";
	}

}

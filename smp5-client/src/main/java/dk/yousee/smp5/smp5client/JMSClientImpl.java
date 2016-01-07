package dk.yousee.smp5.smp5client;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.QueueSession;
import javax.jms.Session;
import javax.naming.Context;
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

	@Override
	public Integer getDefaultOperationTimeout() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String executeXml(String xmlRequest, Integer operationTimeout) {
		Connection connection = null ;
		Context context;
		try {
			context = ContextUtil.getInitialContext();
			ConnectionFactory connectionFactory = (ConnectionFactory) context.lookup("ConnectionFactory");
			connection = connectionFactory.createConnection();
			Session session = connection.createSession(false, QueueSession.AUTO_ACKNOWLEDGE);
			Queue queue = (Queue) context.lookup("/queue/HelloWorldQueue");
			connection.start();
			MessageProducer producer = session.createProducer(queue);
			Message hellowWorldText = session.createTextMessage("Hello World!");
			producer.send(hellowWorldText);
			connection.close();
		} catch (NamingException e) {
			logger.fatal("Error while sending the request to the queue", e);
		} catch (JMSException e) {
			logger.fatal("Error while sending the request to the queue", e);
		} finally {
			if (connection != null) {
//				connection.close();
			}
		}
		return null;
	}

}

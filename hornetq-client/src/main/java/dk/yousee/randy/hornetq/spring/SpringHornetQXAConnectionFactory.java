package dk.yousee.randy.hornetq.spring;

import javax.jms.Connection;
import javax.jms.JMSException;
import javax.jms.QueueConnection;
import javax.jms.TopicConnection;
import javax.jms.XAConnection;
import javax.jms.XAQueueConnection;
import javax.jms.XAQueueConnectionFactory;
import javax.jms.XATopicConnection;
import javax.jms.XATopicConnectionFactory;

import org.hornetq.jms.client.HornetQXAConnectionFactory;

public class SpringHornetQXAConnectionFactory  implements XATopicConnectionFactory, XAQueueConnectionFactory {

	HornetQXAConnectionFactory connectionFactory;

	public void setConnectionFactory(HornetQXAConnectionFactory connectionFactory) {
		this.connectionFactory = connectionFactory;
	}
	
	@Override
	public XAConnection createXAConnection() throws JMSException {
		return connectionFactory.createXAConnection();
	}

	@Override
	public XAConnection createXAConnection(String arg0, String arg1) throws JMSException {
		return connectionFactory.createXAConnection(arg0, arg1);
	}

	@Override
	public TopicConnection createTopicConnection() throws JMSException {
		return connectionFactory.createTopicConnection();
	}

	@Override
	public TopicConnection createTopicConnection(String arg0, String arg1) throws JMSException {
		return connectionFactory.createTopicConnection(arg0, arg1);
	}

	@Override
	public Connection createConnection() throws JMSException {
		return connectionFactory.createConnection();
	}

	@Override
	public Connection createConnection(String arg0, String arg1) throws JMSException {
		return connectionFactory.createConnection(arg0, arg1);
	}

	@Override
	public QueueConnection createQueueConnection() throws JMSException {
		return connectionFactory.createQueueConnection();
	}

	@Override
	public QueueConnection createQueueConnection(String arg0, String arg1) throws JMSException {
		return connectionFactory.createQueueConnection(arg0, arg1);
	}

	@Override
	public XAQueueConnection createXAQueueConnection() throws JMSException {
		return connectionFactory.createXAQueueConnection();
	}

	@Override
	public XAQueueConnection createXAQueueConnection(String arg0, String arg1) throws JMSException {
		return connectionFactory.createXAQueueConnection(arg0, arg1);
	}

	@Override
	public XATopicConnection createXATopicConnection() throws JMSException {
		return connectionFactory.createXATopicConnection();
	}

	@Override
	public XATopicConnection createXATopicConnection(String arg0, String arg1) throws JMSException {
		return connectionFactory.createXATopicConnection(arg0, arg1);
	}


}

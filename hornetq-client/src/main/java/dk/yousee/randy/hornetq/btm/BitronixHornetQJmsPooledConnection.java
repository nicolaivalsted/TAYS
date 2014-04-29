package dk.yousee.randy.hornetq.btm;

import javax.jms.JMSException;
import javax.jms.TemporaryQueue;
import javax.jms.XAConnection;
import javax.jms.XASession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import bitronix.tm.resource.jms.JmsConnectionHandle;
import bitronix.tm.resource.jms.JmsPooledConnection;
import bitronix.tm.resource.jms.PoolingConnectionFactory;
import bitronix.tm.utils.Decoder;

public class BitronixHornetQJmsPooledConnection extends JmsPooledConnection {

    private final static Logger log = LoggerFactory.getLogger(BitronixHornetQJmsPooledConnection.class);

	protected BitronixHornetQJmsPooledConnection(PoolingConnectionFactory poolingConnectionFactory, XAConnection connection) {
		super(poolingConnectionFactory, connection);
	}
	
	@Override
    public Object getConnectionHandle() throws Exception {
        if (log.isDebugEnabled()) log.debug("getting connection handle from " + this);
        int oldState = getState();

        setState(STATE_ACCESSIBLE);

        if (oldState == STATE_IN_POOL) {
            if (log.isDebugEnabled()) log.debug("connection " + getXAConnection() + " was in state IN_POOL, testing it");
            testXAConnection();
        }
        else {
            if (log.isDebugEnabled()) log.debug("connection " + getXAConnection() + " was in state " + Decoder.decodeXAStatefulHolderState(oldState) + ", no need to test it");
        }

        if (log.isDebugEnabled()) log.debug("got connection handle from " + this);
        return new JmsConnectionHandle(this, getXAConnection());
    }

    
    private void testXAConnection() throws JMSException {
        if (!getPoolingConnectionFactory().getTestConnections()) {
            if (log.isDebugEnabled()) log.debug("not testing connection of " + this);
            return;
        }

        if (log.isDebugEnabled()) log.debug("testing connection of " + this);
        XASession xaSession = getXAConnection().createXASession();
        xaSession.close();
    }	
}

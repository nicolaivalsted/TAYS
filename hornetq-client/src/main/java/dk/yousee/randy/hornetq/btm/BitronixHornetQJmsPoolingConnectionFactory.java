package dk.yousee.randy.hornetq.btm;

import javax.jms.XAConnection;
import javax.jms.XAConnectionFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import bitronix.tm.resource.common.ResourceBean;
import bitronix.tm.resource.common.XAStatefulHolder;
import bitronix.tm.resource.jms.PoolingConnectionFactory;

public class BitronixHornetQJmsPoolingConnectionFactory extends PoolingConnectionFactory {

	private final static Logger log = LoggerFactory.getLogger(BitronixHornetQJmsPoolingConnectionFactory.class);

	@Override
    public XAStatefulHolder createPooledConnection(Object xaFactory, ResourceBean bean) throws Exception {
        if (!(xaFactory instanceof XAConnectionFactory))
            throw new IllegalArgumentException("class '" + xaFactory.getClass().getName() + "' does not implement " + XAConnectionFactory.class.getName());
        XAConnectionFactory xaConnectionFactory = (XAConnectionFactory) xaFactory;

        XAConnection xaConnection;
        if (getUser() == null || getPassword() == null) {
            if (log.isDebugEnabled()) log.debug("creating new JMS XAConnection with no credentials");
            xaConnection = xaConnectionFactory.createXAConnection();
        }
        else {
            if (log.isDebugEnabled()) log.debug("creating new JMS XAConnection with user <" + getUser() + "> and password <*****>");
            xaConnection = xaConnectionFactory.createXAConnection(getUser(), getPassword());
        }

        return new BitronixHornetQJmsPooledConnection(this, xaConnection);
    }

	
}

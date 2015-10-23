package dk.yousee.smp5.com;

import org.apache.log4j.Logger;

import dk.yousee.smp5.smp5client.Smp5Client;
import dk.yousee.smp5.smp5client.Smp5ClientFactory;
import dk.yousee.smp5.smp5client.Smp5ConnectorImpl;

/**
 * Created with IntelliJ IDEA. User: aka Date: 01/06/12 Time: 15.04 Basic things
 * for doing actions on sigma
 * <p/>
 * <p>
 * Design: Must be thread safe
 * </p>
 * todo give it a better name, it is not an action any longer
 */
public class SigmaAction {

	private static final Logger logger = Logger.getLogger(SigmaAction.class);

	private Smp5ConnectorImpl helper;

	public SigmaAction(Smp5ConnectorImpl helper) {
		this.helper = helper;
	}

	private Smp5ConnectorImpl getHelper() {
		return helper;
	}

	Smp5Client getProxy() {
		return Smp5ClientFactory.create(getHelper());
	}

	/**
	 * Takes a string and gives a string
	 * 
	 * @param request
	 *            xml as a string
	 * @param operationTimeout
	 *            number of ms to timeout on operations, null means set to
	 *            default
	 * @return xml as a string
	 * @throws RuntimeException
	 *             when error, contains OperationTimeout information is added.
	 *             Possible "defaulted to configured value X from SmpConnector"
	 */
	String execute(String request, Integer operationTimeout) {
		Smp5Client proxy = getProxy();
		try {
			logger.debug(" * * * * * * * * * * * * REQUEST * * * * * * * * * * * * \n"
					+ request);
			String xmlResponse = proxy.executeXml(request, operationTimeout);
			logger.debug(" * * * * * * * * * * * * RESPONSE * * * * * * * * * * * * \n"
					+ xmlResponse);
			return xmlResponse;
		} catch (RuntimeException e) {
			throw e;
		} catch (Exception e) {
			String msg = e.getMessage();
			if (msg == null) {
				if (e.getCause() != null)
					msg = e.getCause().getMessage();
			}
			if (msg == null)
				msg = "none";
			String opTo = (operationTimeout != null) ? Integer
					.toString(operationTimeout) : String.format(
					"defaulted to %s", proxy.getDefaultOperationTimeout());
			throw new RuntimeException(
					String.format(
							"When execute soap on SMP got exception: %s, msg: %s. OperationTimeout is %s",
							e.getClass().getSimpleName(), msg, opTo));
		}
	}

}

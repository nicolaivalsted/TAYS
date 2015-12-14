package dk.yousee.smp5.com;

import dk.yousee.smp5.order.model.Smp5Xml;

/**
 *
 *
 * <p>
 * The idea is that it takes an input object INPUT as input. Then the specific
 * implementation must generate a string from this.
 * </p>
 * <p>
 * Same with output. A string must be converted to the required OUTPUT class And
 * this is it.
 * </p>
 * <p>
 * This abstract class takes care of: 1) all communication. 2) all exception
 * handling
 * </p>
 * <p>
 * OUTPUT should be made immutable. (Only have a constructor, no setters) - but
 * getters..
 * </p>
 */
public abstract class Smp5Com<INPUT, OUTPUT> {

	private SigmaAction con;

	public Smp5Com<INPUT, OUTPUT> setCon(SigmaAction con) {
		this.con = con;
		return this;
	}

	public OUTPUT process(INPUT input) {
		String request = convertRequest(input);
		String response = null;
		response = con.execute(request, getOperationTimeout());
		OUTPUT res;
		Smp5Xml xml = new Smp5Xml(request, response);
		res = convertResponse(xml, input);
		return res;
	}

	static public Smp5Xml xml(String request, String response) {
		return new Smp5Xml(request, response);
	}

	/**
	 * Convert the input to xml output
	 * 
	 * @param input
	 *            value object to be used for input
	 * @return string containing the xml to send to SMP
	 */
	protected abstract String convertRequest(INPUT input);

	/**
	 * Parse request to output
	 * 
	 * @param xml
	 *            the entire thing we talked to SMP about
	 * @param input
	 *            value object use for query. Placed here to enable you to add
	 *            it to the return value
	 * @return string containing the xml to send to SMP
	 */
	protected abstract OUTPUT convertResponse(Smp5Xml xml, INPUT input);

	/**
	 * How long time is the request allowed to run
	 * 
	 * @return null means default (set to SmpConnector) other value will
	 *         overload
	 */
	protected abstract Integer getOperationTimeout();
}

package dk.yousee.smp5.smp5client;

public interface Smp5Client {

	/**
	 * Client must answer question. What is default operation timeout?
	 * 
	 * @return null or the number of ms in operations timeout
	 */
	Integer getDefaultOperationTimeout();

	/**
	 * Executor of a request to xml that gives a response.
	 * <p>
	 * NOTE - calling SMP is FULLY independent of XML. <br/>
	 * The request string IS a string. (a XML encoded string..) <br/>
	 * The response string IS a string !!!! (a XML encoded string..)
	 * </p>
	 * <P>
	 * The module bss-adapter-xml is used to "format" and "parse" these strings.
	 * So bss-adapter-xml is not needed. It is just a "nice" or "ugly" way to
	 * make java understand XML. I prefer "nice" - because it is XML that is
	 * ugly - not the module.
	 * </P>
	 *
	 * @param xmlRequest
	 *            xml string
	 * @param operationTimeout
	 *            how long must the processing take (ms). Null means default.
	 * @return xml string
	 * @throws Exception
	 *             when it fails on a protocol / transportation level. Missing
	 *             customers etc is not an exception om this level.
	 */
	String executeXml(java.lang.String xmlRequest, Integer operationTimeout)
			throws Exception;

}

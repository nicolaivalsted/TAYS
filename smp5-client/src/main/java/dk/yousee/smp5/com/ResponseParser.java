package dk.yousee.smp5.com;

import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;

import dk.yousee.smp5.order.model.Smp5Xml;

/**
 * @author m64746
 *
 *         13/10/2015
 */
public abstract class ResponseParser<OUTPUT> {

	public abstract OUTPUT convertResponse(Smp5Xml xml);

	public static XmlObject parseResponse(String responseXml) {
		try {
			XmlObject xmlObject;
			xmlObject = XmlObject.Factory.parse(responseXml);
			return xmlObject;
		} catch (XmlException e) {
			throw new RuntimeException("Error while parsing result from SMP", e);
		}
	}

}

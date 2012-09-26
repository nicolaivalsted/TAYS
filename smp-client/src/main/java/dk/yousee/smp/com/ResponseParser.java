package dk.yousee.smp.com;

import dk.yousee.smp.order.model.SmpXml;
import org.apache.xmlbeans.XmlException;
import org.apache.xmlbeans.XmlObject;

/**
 * Created with IntelliJ IDEA.
 * User: aka
 * Date: 02/06/12
 * Time: 11.14
 * Parse the response string to output
 */
public abstract class ResponseParser<OUTPUT> {

    public abstract OUTPUT convertResponse(SmpXml xml);


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

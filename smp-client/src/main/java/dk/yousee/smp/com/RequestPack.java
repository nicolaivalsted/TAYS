package dk.yousee.smp.com;

import org.apache.xmlbeans.XmlObject;

/**
 * Created with IntelliJ IDEA.
 * User: aka
 * Date: 02/06/12
 * Time: 10.30
 * This class type can pack requests
 */
public abstract class RequestPack<INPUT> {

    public String generateRequest(INPUT input){
        XmlObject xml;
        xml=createXml(input);
        return xml.xmlText();
    }

    public abstract XmlObject createXml(INPUT input);

}
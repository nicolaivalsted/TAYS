package dk.yousee.smp5.com;

import org.apache.xmlbeans.XmlObject;

public abstract class RequestPack<INPUT> {

	public String generateRequest(INPUT input) {
		XmlObject xml;
		xml = createXml(input);
		return xml.xmlText();
	}

	public abstract XmlObject createXml(INPUT input);

}
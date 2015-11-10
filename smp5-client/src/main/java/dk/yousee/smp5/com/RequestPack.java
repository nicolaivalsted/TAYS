package dk.yousee.smp5.com;

import org.apache.xmlbeans.XmlObject;

import dk.yousee.smp5.debugonly.SaveToFile;

public abstract class RequestPack<INPUT> {

	public String generateRequest(INPUT input) {
		XmlObject xml;
		xml = createXml(input);
		SaveToFile.saveXmlRequest(xml);
		return xml.xmlText();
	}

	public abstract XmlObject createXml(INPUT input);

}
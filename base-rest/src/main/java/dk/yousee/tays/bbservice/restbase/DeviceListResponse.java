package dk.yousee.tays.bbservice.restbase;

import dk.yousee.tays.bbservice.restbase.Device;
import dk.yousee.tays.bbservice.restbase.Link;
import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author m27236
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.PROPERTY)
public class DeviceListResponse {
	private List<Device> cms = new ArrayList<Device>();
	private Link link;

	public DeviceListResponse() {
	}

	@XmlElement(name = "device")
	public List<Device> getCms() {
		return cms;
	}

	public void setCms(List<Device> cms) {
		this.cms = cms;
	}

	public Link getLink() {
		return link;
	}

	public void setLink(Link link) {
		this.link = link;
	}

	public void addDev(Device devResp) {
		cms.add(devResp);
	}
}
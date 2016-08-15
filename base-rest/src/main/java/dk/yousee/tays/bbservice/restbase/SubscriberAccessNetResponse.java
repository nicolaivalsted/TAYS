package dk.yousee.tays.bbservice.restbase;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * A class used to describe a broadband subscriber's equipment: cable modem, mta
 * and cpes. Used in accessnet-rest tays-rest
 * 
 * @author m27236
 */
@XmlRootElement(name = "AccessNet")
@XmlAccessorType(XmlAccessType.FIELD)
public class SubscriberAccessNetResponse {
	@XmlElement(nillable = true)
	private Device cm;
	@XmlElement(nillable = true)
	private Device mta;
	@XmlElement(name = "cpes")
	private List<Device> cpes = new ArrayList<Device>();
	//
	@XmlElement(name = "links")
	private List<Link> links = new ArrayList<Link>();

	public SubscriberAccessNetResponse() {
	}

	/**
	 * Build an AccessnetResponse object
	 * 
	 * @param cm
	 *            in
	 * @param mta
	 *            in
	 * @param cpes
	 *            in
	 */
	public SubscriberAccessNetResponse(Device cm, Device mta, List<Device> cpes) {
		this.cm = cm;
		this.mta = mta;
		if (cpes != null) {
			this.cpes = cpes;
		}
	}

	public Device getCm() {
		return cm;
	}

	public List<Device> getCpes() {
		return cpes;
	}

	public void addCpe(Device device) {
		cpes.add(device);
	}

	public Device getMta() {
		return mta;
	}

	public void setCm(Device cm) {
		this.cm = cm;
	}

	public void setMta(Device mta) {
		this.mta = mta;
	}
}

package dk.yousee.smp5.casemodel.vo.cablebb;

import dk.yousee.smp5.casemodel.SubscriberModel;
import dk.yousee.smp5.casemodel.vo.BusinessPosition;
import dk.yousee.smp5.casemodel.vo.ModemId;
import dk.yousee.smp5.casemodel.vo.helpers.BasicUnit;
import dk.yousee.smp5.order.model.NickName;
import dk.yousee.smp5.order.model.OrderDataLevel;
import dk.yousee.smp5.order.model.OrderDataType;
import dk.yousee.smp5.order.model.ServicePrefix;

/**
 * Created by IntelliJ IDEA. User: m14857 Date: Oct 12, 2010 Time: 4:27:10 PM
 * The object to contain the InetAccess object and StdCpe object. Data structure
 * reference to YouSee Data Migration Requirements: 5.5 Cable Broadband Services
 * Definition
 */
public class CableBBService extends BasicUnit {
	public static OrderDataLevel LEVEL = OrderDataLevel.SERVICE;
	public static OrderDataType TYPE = new OrderDataType(ServicePrefix.SubSvcSpec, "internet");
	public static NickName NAME = new NickName("broadband");

	/**
	 * Contains modemId as long as the service is under construction
	 */
	private ModemId tempModemId;

	/**
	 * Constructs service -- mostly used by parser (when constructing model)
	 * 
	 * @param model
	 *            to use
	 * @param externalKey
	 *            external key
	 */
	public CableBBService(SubscriberModel model, String externalKey) {
		super(model, externalKey, TYPE, LEVEL, NAME, null);
		model.getServiceLevelUnit().add(this);
	}

	/**
	 * Constructs service based on new data from CRM
	 * 
	 * @param model
	 *            in
	 * @param modemId
	 *            in
	 */
	public CableBBService(SubscriberModel model, ModemId modemId) {
		this(model, model.key().CableBBService(modemId));
		this.tempModemId = modemId;
	}

	// children
	private InetAccess inetAccess;
	private SMPStaticIP smpStaticIP;

	public InetAccess getInetAccess() {
		return inetAccess;
	}

	public void setInetAccess(InetAccess inetAccess) {
		this.inetAccess = inetAccess;
	}

	public SMPStaticIP getSmpStaticIP() {
		return smpStaticIP;
	}

	public void setSmpStaticIP(SMPStaticIP smpStaticIP) {
		this.smpStaticIP = smpStaticIP;
	}

	/**
	 * This is the sik of this cable service will always work
	 * 
	 * @return the true thing
	 */
	public String getPosition() {
		String sik = getInetAccess() == null ? null : getInetAccess().sik.getValue();
		if (sik == null) {
			ModemId md = getModemId();
			if (md != null) {
				sik = md.getId();
			}
		}
		return sik;
	}

	/**
	 * @deprecated This way is deprecated, use the getModemId2()
	 * @return modemId extracted from external key
	 */
	ModemId getModemIdOld() {
		return ModemId.extract(getExternalKey());
	}

	/**
	 * @return modemId based on modemId on Internet Access
	 */
	public ModemId getModemId() {
		InetAccess access = getInetAccess();
		return access == null ? tempModemId : access.getModemId();
	}
}

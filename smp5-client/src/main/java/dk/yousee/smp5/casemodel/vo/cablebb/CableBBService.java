package dk.yousee.smp5.casemodel.vo.cablebb;

import dk.yousee.smp5.casemodel.SubscriberModel;
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
	 * Constructs service -- mostly used by parser (when constructing model)
	 * 
	 * @param model
	 *            to use
	 * @param externalKey
	 *            external key
	 */
	public CableBBService(SubscriberModel model, String sik) {
		super(model, model.key().CableBBService(sik), TYPE, LEVEL, NAME, null);
		model.getServiceLevelUnit().add(this);
	}

	// children
	private InetAccess inetAccess;
	private SMPStaticIP smpStaticIP;

	public String getSik() {
		if (inetAccess != null) {
			return inetAccess.sik.getValue();
		} else {
			return null;
		}
	}

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

}

package dk.yousee.smp5.cases;

import dk.yousee.smp5.casemodel.vo.emta.DeviceControl;
import dk.yousee.smp5.casemodel.vo.emta.HsdAccess;
import dk.yousee.smp5.order.model.Acct;
import dk.yousee.smp5.order.model.OrderService;

public class NodeSplitCase extends AbstractCase {
	/**
	 * Constructor with the customer account.
	 *
	 * @param acct
	 *            account id
	 * @param service
	 *            service to sigma
	 */
	public NodeSplitCase(Acct acct, OrderService service) {
		super(acct, service);
	}

	public void updateForNodeSplit(String sik, String gi_address, String cm_mac, String modemModel, String vendor, String serial) {
		HsdAccess ha = getModel().alloc().HsdAccess(sik);
		ha.data_port_id.setValue(cm_mac);

		DeviceControl deviceControl = getModel().alloc().DeviceControl(sik);
		deviceControl.cm_mac.setValue(cm_mac);
		deviceControl.gi_address.setValue(gi_address);
		deviceControl.sik.setValue(sik);
		if (modemModel != null) {
			deviceControl.model.setValue(modemModel);
		}
		if (vendor != null) {
			deviceControl.manufacturer.setValue(vendor);
		}
		if (serial != null) {
			deviceControl.serial_number.setValue(serial);
		}
	}
}

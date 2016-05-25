package dk.yousee.smp5.cases;

import dk.yousee.smp5.casemodel.vo.ModemId;
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

	public HsdAccess updateForNodeSplit(ModemId modemId, String gi_address, String cm_mac, String modemModel, String vendor, String serial) {
		HsdAccess ha = getModel().alloc().HsdAccess(modemId);
		ha.cm_mac.setValue(cm_mac);
		if (modemModel != null)
			ha.cm_model.setValue(modemModel);
		if (vendor != null)
			ha.cm_manufacturer.setValue(vendor);
		if (serial != null)
			ha.cm_serial_number.setValue(serial);
		DeviceControl deviceControl = getModel().alloc().DeviceControl(modemId);
		deviceControl.gi_address.setValue(gi_address);
		return ha;
	}
}

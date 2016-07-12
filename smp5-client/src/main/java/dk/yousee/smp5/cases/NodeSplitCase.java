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
		ha.data_port_id.setValue(cm_mac);
		DeviceControl deviceControl = getModel().alloc().DeviceControl(modemId);
		deviceControl.gi_address.setValue(gi_address);
		return ha;
	}
}

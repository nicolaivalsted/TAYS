package dk.yousee.smp.cases;

import dk.yousee.smp.casemodel.vo.ModemId;
import dk.yousee.smp.casemodel.vo.cpee.HsdAccess;
import dk.yousee.smp.order.model.Acct;
import dk.yousee.smp.order.model.OrderService;

/**
 * Created by IntelliJ IDEA. User: m14857 Date: Dec 2, 2010 Time: 2:30:40 PM The
 * Node Split use case
 */
public class NodeSplitCase extends AbstractCase {
    /**
     * Constructor with the customer account.
     *
     * @param acct account id
     * @param service service to sigma
     */
    public NodeSplitCase(Acct acct, OrderService service) {
        super(acct, service);
    }

    public HsdAccess updateForNodeSplit(ModemId modemId, String gi_address, String cm_mac,
            String modemModel,
            String vendor,
            String serial) {
        HsdAccess ha = getModel().alloc().HsdAccess(modemId);
        ha.cm_mac.setValue(cm_mac);
        ha.gi_address.setValue(gi_address);
        if (modemModel != null)
            ha.cm_model.setValue(modemModel);
        if (vendor != null)
            ha.cm_manufacturer.setValue(vendor);
        if (serial != null)
            ha.cm_serial_number.setValue(serial);
        return ha;
    }
}

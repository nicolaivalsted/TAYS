package dk.yousee.smp.cases;

import dk.yousee.smp.casemodel.vo.BusinessPosition;
import dk.yousee.smp.casemodel.vo.tdcmail.TdcMail;
import dk.yousee.smp.casemodel.vo.tdcmail.TdcMailResource;
import dk.yousee.smp.casemodel.vo.tdcmail.TdcMailService;
import dk.yousee.smp.order.model.Acct;
import dk.yousee.smp.order.model.Action;
import dk.yousee.smp.order.model.BusinessException;
import dk.yousee.smp.order.model.Order;
import dk.yousee.smp.order.model.OrderService;
import java.util.List;

/**
 *
 * @author m27236
 */
public class TdcMailCase extends AbstractCase {
    public TdcMailCase(OrderService orderService, Acct acct) {
        super(acct, orderService);
    }

    public TdcMailCase(SubscriberCase customerCase, boolean keepModel) {
        super(selectModel(customerCase.getModel(), keepModel), customerCase.getService());
    }

    /**
     * Inner class that holds the contract between CRM and SMP
     */
    public static class TdcMailData {
        public TdcMailData() {
        }

        public TdcMailData(String ysproPcode) {
            this.ysproPcode = ysproPcode;
        }
        private String ysproPcode = "1020";

        public String getYsproPcode() {
            return ysproPcode;
        }
        private String kpmNumber;

        public String getKpmNumber() {
            return kpmNumber;
        }

        public void setKpmNumber(String kpmNumber) {
            this.kpmNumber = kpmNumber;
        }
    }

    public Order createProvisioning(BusinessPosition businessPosition, TdcMailData lineItem) throws BusinessException {
        ensureAcct();

        TdcMail def = getModel().alloc().tdcMail(businessPosition);
        def.yspro_pcode.setValue(lineItem.getYsproPcode());

        return getModel().getOrder();
    }

    public Order createResource(BusinessPosition businessPosition, TdcMailData lineItem) throws BusinessException {
        TdcMailService service = getModel().find().tdcMailService(businessPosition);
        if (service != null) {
            TdcMailResource resource = getModel().alloc().tdcMailResource(service);
            resource.kpm_number.setValue(lineItem.getKpmNumber());
        } else {
            throw new BusinessException("Could not find service on BusinessPosition");
        }

        return getModel().getOrder();
    }

    public Order updateResource(BusinessPosition businessPosition, String newKpm) throws BusinessException {
        ensureAcct();

        TdcMailService service = getModel().find().tdcMailService(businessPosition);
        if (service != null) {
            TdcMailResource resource = service.getTdcMailResource();
            if (resource != null) {
                resource.kpm_number.setValue(newKpm);
                resource.cascadeSendAction(Action.UPDATE);
            } else { //not really an update
                TdcMailData data = new TdcMailData();
                data.setKpmNumber(newKpm);
                return createResource(businessPosition, data);
            }
        } else {
            throw new BusinessException("No tdcmail service to update in SMP");
        }

        return getModel().getOrder();
    }

    public TdcMail readProvisioning(BusinessPosition businessPosition) throws BusinessException {
        TdcMailService service = getModel().find().tdcMailService(businessPosition);
        return service != null ? service.getTdcMail() : null;
    }

    public boolean delete(BusinessPosition businessPosition) throws BusinessException {
        ensureAcct();

        return buildOrderFromAction(businessPosition, Action.DELETE);
    }

    private boolean buildOrderFromAction(BusinessPosition businessPosition, Action action) {
        boolean doAnything = false;
        {
            TdcMailService service = getModel().find().tdcMailService(businessPosition);
            if (service != null) {
                doAnything = true;

                if (action == Action.DELETE) {
                    service.sendAction(action);
                } else {
                    service.cascadeSendAction(action);
                }
            }

        }
        return doAnything;
    }

    public List<TdcMailService> readAll() {
        return getModel().find().tdcMailServices();
    }
}

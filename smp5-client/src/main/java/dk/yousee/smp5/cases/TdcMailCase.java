package dk.yousee.smp5.cases;

import java.util.List;

import dk.yousee.smp5.casemodel.vo.tdcmail.TdcMail;
import dk.yousee.smp5.casemodel.vo.tdcmail.TdcMailService;
import dk.yousee.smp5.order.model.Acct;
import dk.yousee.smp5.order.model.Action;
import dk.yousee.smp5.order.model.BusinessException;
import dk.yousee.smp5.order.model.Order;
import dk.yousee.smp5.order.model.OrderService;

/**
 *
 * @author m64746
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
		private String ysproPcode = "1020";
		private String kpmNumber;

		public TdcMailData() {
		}

		public TdcMailData(String ysproPcode) {
			this.ysproPcode = ysproPcode;
		}

		public String getYsproPcode() {
			return ysproPcode;
		}

		public String getKpmNumber() {
			return kpmNumber;
		}

		public void setKpmNumber(String kpmNumber) {
			this.kpmNumber = kpmNumber;
		}

		@Override
		public String toString() {
			return "TdcMailData [ysproPcode=" + ysproPcode + ", kpmNumber=" + kpmNumber + "]";
		}

	}

	public Order createProvisioning(String sik, TdcMailData lineItem) throws BusinessException {
		ensureAcct();

		TdcMail def = getModel().alloc().tdcMail(sik);
		def.yspro_pcode.setValue(lineItem.getYsproPcode());

		return getModel().getOrder();
	}

	public Order createResource(String sik, TdcMailData lineItem) throws BusinessException {
		TdcMailService service = getModel().find().tdcMailService(sik);
		if (service != null) {
			TdcMail mail = service.getTdcMail();
			mail.kpm_number.setValue(lineItem.getKpmNumber());
		} else {
			throw new BusinessException("Could not find service with sik: " + sik);
		}

		return getModel().getOrder();
	}

	public Order updateResource(String sik, String newKpm) throws BusinessException {
		ensureAcct();

		TdcMailService service = getModel().find().tdcMailService(sik);
		if (service != null) {
			TdcMail mail = service.getTdcMail();
			mail.kpm_number.updateValue(newKpm);
			mail.cascadeSendAction(Action.UPDATE);
		} else {
			throw new BusinessException("No tdcmail service to update in SMP");
		}

		return getModel().getOrder();
	}

	public TdcMail readProvisioning(String sik) throws BusinessException {
		TdcMailService service = getModel().find().tdcMailService(sik);
		return service != null ? service.getTdcMail() : null;
	}

	public boolean delete(String sik) throws BusinessException {
		ensureAcct();

		return buildOrderFromAction(sik, Action.DELETE);
	}

	private boolean buildOrderFromAction(String sik, Action action) {
		boolean doAnything = false;
		{
			TdcMailService service = getModel().find().tdcMailService(sik);
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

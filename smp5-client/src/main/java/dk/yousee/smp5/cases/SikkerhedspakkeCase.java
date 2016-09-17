package dk.yousee.smp5.cases;

import java.util.ArrayList;
import java.util.List;

import dk.yousee.smp5.casemodel.vo.sikpakke.Sikkerhedspakke;
import dk.yousee.smp5.casemodel.vo.sikpakke.SikkerhedspakkeService;
import dk.yousee.smp5.order.model.Acct;
import dk.yousee.smp5.order.model.Action;
import dk.yousee.smp5.order.model.BusinessException;
import dk.yousee.smp5.order.model.Order;
import dk.yousee.smp5.order.model.OrderService;

/**
 * @author m64746
 *
 *         Date: 30/03/2016 Time: 12:33:05
 */
public class SikkerhedspakkeCase extends AbstractCase {
	public SikkerhedspakkeCase(OrderService service, Acct acct) {
		super(acct, service);
	}

	/**
	 * Construct this case based on existing Subscriber Case<br/>
	 * This is a kind of chaining of use-cases. <br/>
	 *
	 * @param customerCase
	 *            subscriber case
	 * @param keepModel
	 *            true to use the model from subscriber case, false to start a
	 *            new model (default originally false)
	 */
	public SikkerhedspakkeCase(SubscriberCase customerCase, boolean keepModel) {
		super(selectModel(customerCase.getModel(), keepModel), customerCase.getService());
	}

	/**
	 * Inner class that holds the contract between CRM and SMP
	 */
	public static class SikkerhedspakkeData {

		private String sik;
		private String licenseType;

		public SikkerhedspakkeData(String sik) {
			this.sik = sik;
		}

		public String getSik() {
			return sik;
		}

		public void setSik(String sik) {
			this.sik = sik;
		}

		public String getLicenseType() {
			return licenseType;
		}

		public void setLicenseType(String licenseType) {
			this.licenseType = licenseType;
		}

		@Override
		public String toString() {
			return "SikkerhedspakkeData [sik=" + sik + ", licenseType=" + licenseType + "]";
		}

	}

	/**
	 * Create Sikkerhedspakke
	 *
	 *
	 *
	 * @param lineItem
	 *            data to add
	 * @return order for this
	 * @throws dk.yousee.smp.order.model.BusinessException
	 *             on error like no subscriber
	 */
	public Order create(SikkerhedspakkeData lineItem) throws BusinessException {
		ensureAcct();

		Sikkerhedspakke def = getModel().alloc().Sikkerhedspakke(lineItem.getSik());

		def.sik.setValue(lineItem.getSik());
		def.license_type.setValue(lineItem.getLicenseType());

		return getModel().getOrder();
	}

	/**
	 * Delete function
	 *
	 * @param position
	 *            selected service plan instance
	 * @return true if anything was marked for delete, false if nothing marked
	 *         for delete.<br/>
	 *         Hereby the client can decide if anything needs to be send to
	 *         Sigma
	 * @throws dk.yousee.smp.order.model.BusinessException
	 *             when <br/>
	 *             1) The customer does not exist<br/>
	 */
	public boolean delete(String sik) throws BusinessException {
		ensureAcct();
		boolean res;
		res = buildOrderFromAction(sik, Action.DELETE);
		return res;
	}

	/**
	 * Constructs an order from action change
	 *
	 * @param position
	 *            selected service plan instance (key is modemId)
	 * @param action
	 *            the action to send to the subscription
	 * @return true if anything to do
	 */
	private boolean buildOrderFromAction(String sik, Action action) {
		boolean doAnything = false;
		SikkerhedspakkeService service = getModel().find().SikkerhedspakkeService(sik);
		if (service != null) {
			doAnything = true;

			if (action == Action.DELETE) {
				service.sendAction(action);
			} else {
				service.cascadeSendAction(action);
			}
		}
		return doAnything;
	}

	public class SikkerhedspakkeActivationData {
		String sik;
		String externalKey;

		public SikkerhedspakkeActivationData(String sik) {
			this.sik = sik;
		}

		public String getSik() {
			return sik;
		}

		public String getExternalKey() {
			return externalKey;
		}

		public void setExternalKey(String externalKey) {
			this.externalKey = externalKey;
		}

		private String uuid;

		public String getUuid() {
			return uuid;
		}

		public void setUuid(String uuid) {
			this.uuid = uuid;
		}

		@Override
		public String toString() {
			final StringBuilder sb = new StringBuilder();
			sb.append("{sik=").append(sik);
			if (getUuid() != null)
				sb.append(", uuid='").append(uuid).append('\'');
			sb.append('}');
			return sb.toString();
		}
	}

	/**
	 * Read all activation data for this subscriber
	 *
	 * @return list of activation data
	 * @throws dk.yousee.smp.order.model.BusinessException
	 *             when subscriber is missing
	 */
	public List<SikkerhedspakkeActivationData> readAll() throws BusinessException {
		ensureAcct();

		List<SikkerhedspakkeActivationData> res = new ArrayList<SikkerhedspakkeActivationData>();
		for (SikkerhedspakkeService service : getModel().find().SikkerhedspakkeService()) {
			SikkerhedspakkeActivationData row = new SikkerhedspakkeActivationData(service.getSik());
			Sikkerhedspakke sikpakke = service.getSikkerhedspakke();
			if (sikpakke != null) { // should never be null
				row.setUuid(sikpakke.provisioningid.getValue());
				row.setExternalKey(sikpakke.getExternalKey());
			} else {
				row.setUuid("error");
			}
			res.add(row);
		}
		return res;
	}

	public boolean update(String sik, SikkerhedspakkeData data) throws BusinessException {
		ensureAcct();
		boolean changed = false;
		Sikkerhedspakke sikkerhedspakke = getModel().find().Sikkerhedspakke(sik);
		if (sikkerhedspakke == null) {
			throw new BusinessException("Update failed, Sikkerhedspakke service Plan was not found: for sik: %s", sik);
		}

		if (!data.getLicenseType().equals("") && !data.getLicenseType().equals(sikkerhedspakke.license_type.getValue())) {
			sikkerhedspakke.license_type.setValue(data.getLicenseType());
			changed = true;
		}
		getModel().getOrder();
		return changed;
	}
}

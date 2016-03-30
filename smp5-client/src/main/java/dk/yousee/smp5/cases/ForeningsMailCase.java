package dk.yousee.smp5.cases;

import java.util.ArrayList;
import java.util.List;

import dk.yousee.smp5.casemodel.vo.mail.ForeningsMailService;
import dk.yousee.smp5.casemodel.vo.mail.Mail;
import dk.yousee.smp5.order.model.Acct;
import dk.yousee.smp5.order.model.Action;
import dk.yousee.smp5.order.model.BusinessException;
import dk.yousee.smp5.order.model.Order;
import dk.yousee.smp5.order.model.OrderService;

/**
 * Case for forenings mail update in SMP
 */
public class ForeningsMailCase extends AbstractCase {
	public ForeningsMailCase(OrderService service, Acct acct) {
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
	 *
	 */
	public ForeningsMailCase(SubscriberCase customerCase, boolean keepModel) {
		super(selectModel(customerCase.getModel(), keepModel), customerCase.getService());
	}

	/**
	 * Inner class that holds the contract between CRM and SMP
	 */
	public static class ForeningsData {
		private String product;
		private String sik;
		private String name;
		private String customerId;

		public String getCustomerId() {
			return customerId;
		}

		public void setCustomerId(String customerId) {
			this.customerId = customerId;
		}

		public String getSik() {
			return sik;
		}

		public void setSik(String sik) {
			this.sik = sik;
		}

		public String getProduct() {
			return product;
		}

		public void setProduct(String product) {
			this.product = product;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		@Override
		public String toString() {
			return "ForeningsData [product=" + product + ", sik=" + sik + ", name=" + name + ", customerId=" + customerId + "]";
		}

	}

	/**
	 * Create ForeningsMail
	 *
	 * @param position
	 *            where?
	 * @param lineItem
	 *            data to add
	 * @return order for this
	 * @throws dk.yousee.smp.order.model.BusinessException
	 *             on error like no subscriber
	 */
	public Order createProvisioning(String sik, ForeningsData lineItem) throws BusinessException {
		ensureAcct();

		Mail def = getModel().alloc().ForeningsMail(sik);

		def.product_code.setValue(lineItem.getProduct());
		def.product_name.setValue(lineItem.getName());
		if (!lineItem.getCustomerId().equals("") && !lineItem.getCustomerId().equals(def.customer_id.getValue())) {
			def.customer_id.setValue(lineItem.getCustomerId());
		}
		def.sik.setValue(lineItem.getSik());
		return getModel().getOrder();
	}

	public ForeningsData readProvisioning(String sik) throws BusinessException {
		ForeningsMailService mailService = getModel().find().ForeningsMailService(sik);
		ForeningsData res;
		if (mailService == null) {
			res = null;
		} else {
			res = new ForeningsData();
			res.setProduct(mailService.getMail().product_code.getValue());
			res.setName(mailService.getMail().product_name.getValue());
		}
		return res;
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
		{
			ForeningsMailService service = getModel().find().ForeningsMailService(sik);
			if (service != null) {
				doAnything = true;

				/**
				 * It was proven from tests that delete on top level works.<br/>
				 * But suspend/resume must be performed at each child-service
				 * <p>
				 * Tests shows that marking elements for something the element
				 * already is results in no order line in Sigma. This might be
				 * use full when sending commands to Sigma - so sending too much
				 * does not really matter in Sigma !!
				 * </p>
				 *
				 */
				if (action == Action.DELETE) {
					service.sendAction(action);
				} else {
					service.cascadeSendAction(action);
				}
			}
		}
		return doAnything;
	}

	public class ForeningsMailActivationData {
		private String sik;
		private String externalKey;
		private String product;
		private String name;
		private String customerId;
		private Boolean conversation;

		public ForeningsMailActivationData(String sik) {
			this.sik = sik;
		}

		public String getProduct() {
			return product;
		}

		public String getSik() {
			return sik;
		}

		public void setProduct(String product) {
			this.product = product;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getCustomerId() {
			return customerId;
		}

		public void setCustomerId(String customerId) {
			this.customerId = customerId;
		}

		public Boolean getConversation() {
			return conversation;
		}

		public void setConversation(Boolean conversation) {
			this.conversation = conversation;
		}

		public String getExternalKey() {
			return externalKey;
		}

		public void setExternalKey(String externalKey) {
			this.externalKey = externalKey;
		}

		@Override
		public String toString() {
			final StringBuilder sb = new StringBuilder();
			sb.append("{sik=").append(sik);
			sb.append(", product='").append(product).append('\'');
			sb.append(", name='").append(getName()).append('\'');
			if (getCustomerId() != null)
				sb.append(", customerId='").append(customerId).append('\'');
			if (getConversation() != null)
				sb.append(", conversation=").append(conversation);
			sb.append('}');
			return sb.toString();
		}
	}

	/**
	 * Read all activation data for this subscriber
	 *
	 * @return list of activation data
	 * @throws BusinessException
	 *             when subscriber is missing
	 */
	public List<ForeningsMailActivationData> readAll() throws BusinessException {
		ensureAcct();

		List<ForeningsMailActivationData> res = new ArrayList<ForeningsMailActivationData>();
		for (ForeningsMailService service : getModel().find().ForeningsMailService()) {
			ForeningsMailActivationData row = new ForeningsMailActivationData(service.getMail().sik.getValue());
			Mail mail = service.getMail();
			if (mail != null) { // should never be null
				row.setProduct(mail.product_code.getValue());
				row.setName(mail.product_name.getValue());
				row.setCustomerId(mail.customer_id.getValue());
				row.setConversation(mail.getConversation());
				row.setExternalKey(mail.getExternalKey());
			} else {
				row.setProduct("error");
				row.setName("error");
			}
			res.add(row);
		}
		return res;
	}

	/**
	 * @param position
	 *            that identifies forenings mail
	 * @param useMailAccount
	 *            is the mail used by subscriber (assign true). If user dismiss
	 *            then assign false.
	 * @return order to pass to SMP
	 * @throws BusinessException
	 *             when subscriber is missing
	 */
	public Order activate(String sik, boolean useMailAccount) throws BusinessException {
		ensureAcct();
		Mail mail = getModel().find().ForeningsMail(sik);
		mail.setConversation(useMailAccount);

		return getModel().getOrder();
	}

	/**
	 * Used to migrate a subscribers forenings mails. It is assumed that the
	 * customer says "YES" to having the mail - otherwise he/she should not
	 * migrate ...
	 *
	 * @param position
	 *            that identifies forenings mail
	 * @param customerId
	 *            - the customer in Dansk Kabel TV original system.
	 * @return order to pass to SMP
	 * @throws BusinessException
	 *             when subscriber is missing or position not found
	 */
	public Order activateOnMigration(String sik, String customerId) throws BusinessException {
		ensureAcct();
		Mail mail = getModel().find().ForeningsMail(sik);
		if (mail == null)
			throw new BusinessException("No mail found on sik: %s", sik);
		mail.customer_id.setValue(customerId);
		mail.setConversation(true);

		return getModel().getOrder();
	}
}

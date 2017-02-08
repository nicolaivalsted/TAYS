package dk.yousee.smp5.cases;

import dk.yousee.smp5.casemodel.SubscriberModel;
import dk.yousee.smp5.casemodel.vo.fordel.FordelSubscription;
import dk.yousee.smp5.order.model.Acct;
import dk.yousee.smp5.order.model.Action;
import dk.yousee.smp5.order.model.BusinessException;
import dk.yousee.smp5.order.model.Order;
import dk.yousee.smp5.order.model.OrderService;

/**
 * @author m64746
 *
 *         Date: Jan 23, 2017 Time: 10:46:56 AM
 */
public class FordelCase extends AbstractCase {

	public FordelCase(SubscriberModel model, OrderService service) {
		super(model, service);
	}

	public FordelCase(Acct acct, OrderService service) {
		super(acct, service);
	}

	public FordelCase(SubscriberCase customerCase, boolean keepModel) {
		super(selectModel(customerCase.getModel(), keepModel), customerCase.getService());
	}

	public static class FordelData {
		private String sik; // bonnier, mofibo,politiken, meraki
		private String sourceCode; // bonnier
		private String sid; // bonnier
		private String la;// bonnier
		private String cnr;// bonnier
		private String identifier;// politiken
		private String id;// meraki
		private String branding;// politiken
		private String email;// bonnier,mofibo, politiken
		private String gllid;// meraki
		private String lid;// meraki
		private String plan; // bonnier,mofibo
		private String type;// bonnier,politiken

		public String getSik() {
			return sik;
		}

		public void setSik(String sik) {
			this.sik = sik;
		}

		public String getLid() {
			return lid;
		}

		public void setLid(String lid) {
			this.lid = lid;
		}

		public String getSourceCode() {
			return sourceCode;
		}

		public void setSourceCode(String sourceCode) {
			this.sourceCode = sourceCode;
		}

		public String getSid() {
			return sid;
		}

		public void setSid(String sid) {
			this.sid = sid;
		}

		public String getLa() {
			return la;
		}

		public void setLa(String la) {
			this.la = la;
		}

		public String getCnr() {
			return cnr;
		}

		public void setCnr(String cnr) {
			this.cnr = cnr;
		}

		public String getIdentifier() {
			return identifier;
		}

		public void setIdentifier(String identifier) {
			this.identifier = identifier;
		}

		public String getBranding() {
			return branding;
		}

		public void setBranding(String branding) {
			this.branding = branding;
		}

		public String getEmail() {
			return email;
		}

		public void setEmail(String email) {
			this.email = email;
		}

		public String getGllid() {
			return gllid;
		}

		public void setGllid(String gllid) {
			this.gllid = gllid;
		}

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getPlan() {
			return plan;
		}

		public void setPlan(String plan) {
			this.plan = plan;
		}

		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

	}

	public Order create(FordelData fordelData) throws BusinessException {
		ensureAcct();

		FordelSubscription fordelSubscription = getModel().alloc().FordelSubscription(fordelData.getSik());
		fordelSubscription.sik.setValue(fordelData.getSik());
		fordelSubscription.sid.setValue(fordelData.getSid());
		fordelSubscription.la.setValue(fordelData.getLa());
		fordelSubscription.cnr.setValue(fordelData.getCnr());
		fordelSubscription.email.setValue(fordelData.getEmail());
		fordelSubscription.type.setValue(fordelData.getType());
		fordelSubscription.plan.setValue(fordelData.getPlan());

		return getModel().getOrder();
	}

	public Order update(FordelData fordelData) throws BusinessException {
		ensureAcct();

		FordelSubscription fordelSubscription = getModel().find().FordelSubscription(fordelData.getSik());
		if (fordelSubscription != null) {
			fordelSubscription.sid.setValue(fordelData.getSid());
			fordelSubscription.la.setValue(fordelData.getLa());
			fordelSubscription.cnr.setValue(fordelData.getCnr());
			fordelSubscription.email.setValue(fordelData.getEmail());
			fordelSubscription.type.setValue(fordelData.getType());

			return getModel().getOrder();
		}
		return null;
	}

	/**
	 * @param fordelData
	 */
	public void delete(FordelData fordelData) {
		FordelSubscription fordelSubscription = getModel().find().FordelSubscription(fordelData.getSik());
		if (fordelSubscription != null) {
			fordelSubscription.sendAction(Action.DELETE);
		}

	}

}

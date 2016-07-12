package dk.yousee.smp5.cases;

import dk.yousee.smp5.casemodel.SubscriberModel;
import dk.yousee.smp5.casemodel.vo.ModemId;
import dk.yousee.smp5.casemodel.vo.PhoneNumber;
import dk.yousee.smp5.casemodel.vo.emta.VoipAccess;
import dk.yousee.smp5.casemodel.vo.voiceline.DialToneAccess;
import dk.yousee.smp5.casemodel.vo.voiceline.MailBox;
import dk.yousee.smp5.casemodel.vo.voiceline.SwitchFeature;
import dk.yousee.smp5.casemodel.vo.voiceline.VoiceService;
import dk.yousee.smp5.order.model.Acct;
import dk.yousee.smp5.order.model.Action;
import dk.yousee.smp5.order.model.BusinessException;
import dk.yousee.smp5.order.model.Order;
import dk.yousee.smp5.order.model.OrderService;

/**
 * Created by IntelliJ IDEA. User: m14857 Date: Nov 2, 2010 Time: 12:26:52 PM
 * Use case for VOIP
 */
public class VoiceCase extends AbstractCase {

	public VoiceCase(Acct acct, OrderService service) {
		super(acct, service);
	}

	/**
	 * Construct this case based on existing Subscriber Case<br/>
	 * This is a kind of chaining of use-cases. <br/>
	 * <p>
	 * First ask for the customer, eventually create him Then work with mobile
	 * broad band.
	 * </p>
	 *
	 * @param customerCase
	 *            subscriber case's
	 */
	public VoiceCase(SubscriberCase customerCase) {
		this(new SubscriberModel(customerCase.getModel().getResponse()), customerCase.getService());
	}

	/**
	 * Create case based on another cases's base
	 * 
	 * @param model
	 *            model from other case
	 * @param service
	 *            the service used
	 */
	public VoiceCase(SubscriberModel model, OrderService service) {
		super(model, service);
	}

	public static class VoiceData {
		private String sik;
		private PhoneNumber phoneNumber;
		private String mta_voice_port;
		private String rate_codes;
		private String Privacy;
		private String Cos_restrict_id;
		private String cnam;
		private String modemActivationCode;

		public String getSik() {
			return sik;
		}

		public void setSik(String sik) {
			this.sik = sik;
		}

		public String getCnam() {
			return cnam;
		}

		public void setCnam(String cnam) {
			this.cnam = cnam;
		}

		public PhoneNumber getPhoneNumber() {
			return phoneNumber;
		}

		public void setPhoneNumber(PhoneNumber phoneNumber) {
			this.phoneNumber = phoneNumber;
		}

		public String getMta_voice_port() {
			return mta_voice_port;
		}

		public void setMta_voice_port(String mta_voice_port) {
			this.mta_voice_port = mta_voice_port;
		}

		public String getRate_codes() {
			return rate_codes;
		}

		public void setRate_codes(String rate_codes) {
			this.rate_codes = rate_codes;
		}

		public String getPrivacy() {
			return Privacy;
		}

		public void setPrivacy(String privacy) {
			Privacy = privacy;
		}

		public String getCos_restrict_id() {
			return Cos_restrict_id;
		}

		public void setCos_restrict_id(String cos_restrict_id) {
			Cos_restrict_id = cos_restrict_id;
		}

		public String getModemActivationCode() {
			return modemActivationCode;
		}

		public void setModemActivationCode(String modemActivationCode) {
			this.modemActivationCode = modemActivationCode;
		}

	}

	public VoiceService findByPhoneNumber(PhoneNumber phoneNumber) {
		return getModel().find().VoiceService(phoneNumber);
	}

	Order createVoiceMail(String sik, PhoneNumber phoneNumber) throws BusinessException {
		ensureAcct();

		MailBox voiceMail = getModel().alloc().MailBox(sik);
		voiceMail.setPhoneNumber(phoneNumber);
		return getModel().getOrder();
	}

	public Order createVoice(ModemId modemId, VoiceData voiceData) throws BusinessException {
		ensureAcct();

		DialToneAccess dialToneAccess = getModel().alloc().DialToneAccess(voiceData.getSik());

		if (modemId != null)
			dialToneAccess.modem_id.setValue(modemId.getId());
		dialToneAccess.sik.setValue(voiceData.getSik());
		dialToneAccess.setPhoneNumber(voiceData.getPhoneNumber());
		dialToneAccess.mta_voice_port.setValue(voiceData.getMta_voice_port());
		dialToneAccess.rate_codes.setValue(voiceData.getRate_codes());
		dialToneAccess.Privacy.setValue(voiceData.getPrivacy());
		dialToneAccess.Cos_restrict_id.setValue(voiceData.getCos_restrict_id());
		dialToneAccess.cnam.setValue(voiceData.getCnam());
		dialToneAccess.lnp_porting_status.setValue("Not Ported");

		// create ASSOC if mta exist
		if (modemId != null) {
			VoipAccess voipAccess = getModel().find().VoipAccess(modemId);
			if (voipAccess != null && dialToneAccess.dt_has_equipment.get() == null) {
				dialToneAccess.dt_has_equipment.add(voipAccess);
			}
		}

		SwitchFeature switchFeature =  getModel().add().SwitchFeature(dialToneAccess.getParent());
		//default error in smp should be changed
		switchFeature.sw_cos.setValue("y");
		switchFeature.sw_ocb.setValue("y");

		createVoiceMail(voiceData.getSik(), voiceData.getPhoneNumber());
		return getModel().getOrder();
	}

	public Order updateDialToneAccess(String sik, VoiceData voiceData) throws BusinessException {
		ensureAcct();

		DialToneAccess dialToneAccess = getModel().find().DialToneAccess(sik);
		if (dialToneAccess == null) {
			throw new BusinessException("Update failed,  Voice service Plan was not found: for position: %s", sik);
		}
		if (voiceData.getSik() != null) {
			dialToneAccess.sik.setValue(voiceData.getSik());
		}
		if (voiceData.getPhoneNumber() != null) {
			dialToneAccess.setPhoneNumber(voiceData.getPhoneNumber());
		}
		if (voiceData.getMta_voice_port() != null) {
			dialToneAccess.mta_voice_port.setValue(voiceData.getMta_voice_port());
		}
		if (voiceData.getRate_codes() != null) {
			dialToneAccess.rate_codes.setValue(voiceData.getRate_codes());
		}
		if (voiceData.getPrivacy() != null) {
			dialToneAccess.Privacy.setValue(voiceData.getPrivacy());
		}
		if (voiceData.getCos_restrict_id() != null) {
			dialToneAccess.Cos_restrict_id.setValue(voiceData.getCos_restrict_id());
		}
		if (voiceData.getModemActivationCode() != null) {
			dialToneAccess.modem_id.setValue(voiceData.getModemActivationCode());
		}

		return null;
	}

	/**
	 * Delete function
	 *
	 * @param modemId
	 *            selected service plan instance (key is modemId)
	 * @return true if anything was marked for delete, false if nothing marked
	 *         for delete.<br/>
	 *         Hereby the client can decide if anything needs to be send to
	 *         Sigma
	 * @throws dk.yousee.smp.order.model.BusinessException
	 *             when <br/>
	 *             1) The customer does not exist<br/>
	 */
	public boolean deleteVoice(String sik) throws BusinessException {
		ensureAcct();
		return buildOrderFromAction(sik, Action.DELETE);
	}

	/**
	 * Suspend funktion
	 *
	 * @param modemId
	 *            selected service plan instance (key is modemId)
	 * @return true if anything was marked for suspension, false if nothing
	 *         marked for suspension.<br/>
	 *         Hereby the client can decide if anything needs to be send to
	 *         Sigma
	 * @throws dk.yousee.smp.order.model.BusinessException
	 *             when <br/>
	 *             1) The customer does not exist<br/>
	 */
	public boolean suspendVoice(String sik) throws BusinessException {
		ensureAcct();
		return buildOrderFromAction(sik, Action.SUSPEND);
	}

	/**
	 * resume funktion
	 *
	 * @param modemId
	 *            selected service plan instance (key is modemId)
	 * @return true if anything was marked for resume, false if nothing marked
	 *         for resume.<br/>
	 *         Hereby the client can decide if anything needs to be send to
	 *         Sigma
	 * @throws dk.yousee.smp.order.model.BusinessException
	 *             when <br/>
	 *             1) The customer does not exist<br/>
	 */
	public boolean resumeVoice(String sik) throws BusinessException {
		ensureAcct();
		return buildOrderFromAction(sik, Action.RESUME);
	}

	/**
	 * Constructs an order from action change
	 *
	 * @param modemId
	 *            selected service plan instance (key is modemId)
	 * @param action
	 *            the action to send to the subscription
	 * @return true if anything to do
	 */
	private boolean buildOrderFromAction(String sik, Action action) {
		boolean doAnything = false;

		VoiceService service = getModel().find().VoiceService(sik);

		if (service != null) {
			doAnything = true;

			/**
			 * It was proven from tests that delete on top level works.<br/>
			 * But suspend/resume must be performed at each child-service
			 * <p>
			 * Tests shows that marking elements for something the element
			 * already is results in no order line in Sigma. This might be use
			 * full when sending commands to Sigma - so sending too much does
			 * not really matter in Sigma !!
			 * </p>
			 *
			 */
			if (action == Action.DELETE) {
				service.sendAction(action);
			} else {
				service.cascadeSendAction(action);
			}
		}
		return doAnything;
	}

	public void updateVoiceMailRandyState(PhoneNumber pn, String state) throws BusinessException {
		ensureAcct();

		VoiceService voiceService = getModel().find().VoiceService(pn);
		if (voiceService != null && voiceService.getVoiceMail() != null) {
			MailBox mailBox = voiceService.getVoiceMail().getMailBox();
			mailBox.randy_status.setValue(state);
		}
	}
}

package dk.yousee.smp5.cases;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.Set;

import dk.yousee.smp5.casemodel.SubscriberModel;
import dk.yousee.smp5.casemodel.vo.stb.STBCas;
import dk.yousee.smp5.casemodel.vo.video.VideoServicePlanAttributes;
import dk.yousee.smp5.casemodel.vo.video.VideoSubscription;
import dk.yousee.smp5.order.model.Acct;
import dk.yousee.smp5.order.model.Action;
import dk.yousee.smp5.order.model.BusinessException;
import dk.yousee.smp5.order.model.Order;
import dk.yousee.smp5.order.model.OrderService;

public class VideoCase extends AbstractCase {
	public static final String VIDEO_SERVICE_ID = "53335324532453245";

	public VideoCase(SubscriberModel model, OrderService service) {
		super(model, service);
	}

	public VideoCase(Acct acct, OrderService service) {
		super(acct, service);
	}

	public VideoCase(SubscriberCase customerCase, boolean keepModel) {
		super(selectModel(customerCase.getModel(), keepModel), customerCase.getService());
	}

	public static class VideoData {
		private String videoEntitlementId;
		private Set<String> packages;
		private String modifyDate;
		private String cableUnit;
		private String acct;

		public String getAcct() {
			return acct;
		}

		public void setAcct(String acct) {
			this.acct = acct;
		}

		public String getVideoEntitlementId() {
			return videoEntitlementId;
		}

		public void setVideoEntitlementId(String videoEntitlementId) {
			this.videoEntitlementId = videoEntitlementId;
		}

		public String getModifyDate() {
			return modifyDate;
		}

		public void setModifyDate(String modifyDate) {
			this.modifyDate = modifyDate;
		}

		public String getCableUnit() {
			return cableUnit;
		}

		public void setCableUnit(String cableUnit) {
			this.cableUnit = cableUnit;
		}

		@Override
		public String toString() {
			return "VideoData [videoEntitlementId=" + videoEntitlementId + ", packages=" + packages + ", modifyDate=" + modifyDate + ", cableUnit=" + cableUnit
					+ ", acct=" + acct + "]";
		}

		public Set<String> getPackages() {
			return packages;
		}

		public void setPackages(Set<String> packages) {
			this.packages = packages;
		}

	}

	public Order create(VideoData lineItem) throws BusinessException {
		ensureAcct();
		String entitlementId = "";
		List<VideoSubscription> vSubs = getModel().find().VideoSubscriptionBP(lineItem.getVideoEntitlementId());
		boolean action;
		boolean changed = false;

		// handle packages to add/nothing
		for (String parcos : lineItem.getPackages()) {
			action = findActionToPerform(parcos, vSubs);
			if (!action) {
				changed = true;
				entitlementId = lineItem.getAcct() + "-" + lineItem.getVideoEntitlementId() + "-" + parcos;
				VideoSubscription videoSubscription = getModel().alloc().VideoSubscription(entitlementId, parcos, getAcct().toString());
				videoSubscription.video_entitlement_id.setValue(entitlementId);
				videoSubscription.packageId.setValue(parcos);
			}
		}
		VideoServicePlanAttributes videoServicePlanAttributes = getModel().find().VideoServicePlanAttributes();
		if (changed) {
			if (videoServicePlanAttributes == null) {
				videoServicePlanAttributes = getModel().alloc().VideoServicePlanAttributes(getAcct().toString());
				videoServicePlanAttributes.video_service_plan_id.setValue(VIDEO_SERVICE_ID);
				videoServicePlanAttributes.cableUnit.setValue(lineItem.getCableUnit());
			} else {
				videoServicePlanAttributes.modify_date.setValue(generateModifyDate());
			}
		}

		STBCas stb = getModel().find().findFirstSTB();
		if (stb != null && videoServicePlanAttributes != null && videoServicePlanAttributes.video_service_defn_has_cas.isEmpty()) {
			videoServicePlanAttributes.video_service_defn_has_cas.add(stb);
		}

		if (!lineItem.getCableUnit().equals(videoServicePlanAttributes.cableUnit.getValue())) {
			videoServicePlanAttributes.cableUnit.setValue(lineItem.getCableUnit());
		}

		return getModel().getOrder();
	}

	public Order createVideoPlanSTB(String cableUnit) throws BusinessException {
		ensureAcct();
		VideoServicePlanAttributes videoServicePlanAttributes = getModel().alloc().VideoServicePlanAttributes(getAcct().toString());
		videoServicePlanAttributes.video_service_plan_id.setValue(VIDEO_SERVICE_ID);
		videoServicePlanAttributes.cableUnit.setValue(cableUnit);

		return getModel().getOrder();
	}

	/**
	 * @param parcos
	 * @param vSubs
	 * @return true if nothing to do or false if is add
	 */
	private boolean findActionToPerform(String parcos, List<VideoSubscription> vSubs) {
		if (vSubs != null) {
			for (VideoSubscription subscription : vSubs) {
				if (subscription.packageId.getValue().toUpperCase().equals(parcos.toUpperCase())) {
					return true;
				}
			}
		}
		return false;
	}

	public boolean sendAction(String id, Action action) throws BusinessException {
		ensureAcct();
		boolean res;
		res = buildOrderFromAction(id, action);
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
	 * @throws BusinessException
	 */
	private boolean buildOrderFromAction(String id, Action action) throws BusinessException {
		boolean changed = false;
		if (action == Action.DELETE) {
			List<VideoSubscription> subscriptionList = getModel().find().VideoSubscriptionBP(id);
			if (subscriptionList != null) {
				for (VideoSubscription videoSubscription : subscriptionList) {
					videoSubscription.sendAction(Action.DELETE);
					changed = true;
				}
			}
		}

		if (action == Action.UPDATE) {
			changed = true;
		}

		if (changed) {
			// IF UPDATE OR DELETE (changed)
			VideoServicePlanAttributes planAttributes = getModel().find().VideoServicePlanAttributes();
			if (planAttributes != null) {
				planAttributes.modify_date.setValue(generateModifyDate());
			} else {
				changed = false;
			}
		}
		return changed;
	}

	public static String generateModifyDate() throws BusinessException {
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss:SSS");
		String dateFinal = sdf.format(new Date()) + "-" + new Random().nextInt(5000);
		return dateFinal;
	}

}

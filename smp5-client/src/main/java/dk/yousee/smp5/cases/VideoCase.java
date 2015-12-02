package dk.yousee.smp5.cases;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.List;

import dk.yousee.smp5.casemodel.SubscriberModel;
import dk.yousee.smp5.casemodel.vo.video.VideoServicePlanAttributes;
import dk.yousee.smp5.casemodel.vo.video.VideoSubscription;
import dk.yousee.smp5.order.model.Acct;
import dk.yousee.smp5.order.model.Action;
import dk.yousee.smp5.order.model.BusinessException;
import dk.yousee.smp5.order.model.Order;
import dk.yousee.smp5.order.model.OrderService;

public class VideoCase extends AbstractCase {

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
		// video subscription
		private String videoEntitlementId;
		// assoc
		private String[] packageList;
		private String modifyDate;

		public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		public String getVideoEntitlementId() {
			return videoEntitlementId;
		}

		public void setVideoEntitlementId(String videoEntitlementId) {
			this.videoEntitlementId = videoEntitlementId;
		}

		public String[] getPackageList() {
			return packageList;
		}

		public void setPackageList(String[] packageList) {
			this.packageList = packageList;
		}

		public String getModifyDate() {
			return modifyDate;
		}

		public void setModifyDate(String modifyDate) {
			this.modifyDate = modifyDate;
		}

		@Override
		public String toString() {
			return "VideoData [videoEntitlementId=" + videoEntitlementId + ", packageList=" + Arrays.toString(packageList)
					+ ", modifyDate=" + modifyDate + "]";
		}

	}

	public Order create(VideoData lineItem) throws BusinessException {
		ensureAcct();
		validateRequired(lineItem);
		List<VideoSubscription> vSubs = getModel().find().VideoSubscription();
		boolean action;
		if (vSubs != null) {
			for (VideoSubscription subscription : vSubs) {
				action = findMissing(lineItem.getPackageList(), subscription);
				if (!action) {
					subscription.sendAction(Action.DELETE);
				}
			}
		}
		action = false;

		VideoServicePlanAttributes videoServicePlanAttributes = getModel().find().VideoServicePlanAttributes();
		if (videoServicePlanAttributes == null) {
//			String id = String.valueOf(new Random().nextInt(1000000));
			String id = "53335324532453245";
			videoServicePlanAttributes = getModel().alloc().VideoServicePlanAttributes();
			videoServicePlanAttributes.video_service_plan_id.setValue(id);
		}

		for (String parcos : lineItem.getPackageList()) {
			action = findActionToPerform(parcos, vSubs);
			if (!action) {
				VideoSubscription videoSubscription = getModel().alloc().VideoSubscription(lineItem.getVideoEntitlementId(), parcos);
				videoSubscription.video_entitlement_id.setValue(lineItem.getVideoEntitlementId());
				videoSubscription.packageId.setValue(parcos);
			}
		}

		return getModel().getOrder();
	}

	/**
	 * @param parcos
	 * @param vSubs
	 * @return true if nothing to do or false if is delete
	 */
	private boolean findActionToPerform(String parcos, List<VideoSubscription> vSubs) {
		if (vSubs != null) {
			for (VideoSubscription subscription : vSubs) {
				if (subscription.packageId.getValue().equals(parcos)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * @param strings
	 * @param subscription2
	 * @return true if nothing to do or false if is delete
	 */
	private boolean findMissing(String[] strings, VideoSubscription subscription2) {
		for (String parcos : strings) {
			if (parcos.equals(subscription2.packageId.getValue())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * @param lineItem
	 * @throws BusinessException
	 */
	private void validateRequired(VideoData lineItem) throws BusinessException {
		if (lineItem.getVideoEntitlementId() == null || lineItem.getVideoEntitlementId().equals("")) {
			throw new BusinessException("rate code is required");
		}
	}

	public boolean delete(String entitlementId) throws BusinessException {
		ensureAcct();
		boolean res;
		res = buildOrderFromAction(entitlementId, Action.DELETE);
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
	private boolean buildOrderFromAction(String entitlementId, Action delete) throws BusinessException {
		List<VideoSubscription> subscriptionList = getModel().find().VideoSubscription(entitlementId);
		if (subscriptionList == null || subscriptionList.size() == 0) {
			throw new BusinessException("Delete failed,  entitlementId=%s was not found", entitlementId);
		}
		for (VideoSubscription videoSubscription : subscriptionList) {
			if (videoSubscription != null) {
				videoSubscription.sendAction(Action.DELETE);
			}
		}
		return true;
	}

}

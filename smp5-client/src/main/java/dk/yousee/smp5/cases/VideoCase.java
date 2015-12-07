package dk.yousee.smp5.cases;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import dk.yousee.smp5.casemodel.SubscriberModel;
import dk.yousee.smp5.casemodel.vo.video.VideoServicePlanAttributes;
import dk.yousee.smp5.casemodel.vo.video.VideoSubscription;
import dk.yousee.smp5.order.model.Acct;
import dk.yousee.smp5.order.model.Action;
import dk.yousee.smp5.order.model.BusinessException;
import dk.yousee.smp5.order.model.Order;
import dk.yousee.smp5.order.model.OrderService;
import dk.yousee.smp5.order.util.OrderHelper;

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
		private String videoEntitlementId;
		private String[] packageList;
		private String modifyDate;
		private String beginDate;
		private String endDate;
		private String sik;

		public String getSik() {
			return sik;
		}

		public void setSik(String sik) {
			this.sik = sik;
		}

		public String getBeginDate() {
			return beginDate;
		}

		public void setBeginDate(String beginDate) {
			this.beginDate = beginDate;
		}

		public String getEndDate() {
			return endDate;
		}

		public void setEndDate(String endDate) {
			this.endDate = endDate;
		}

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
					+ ", modifyDate=" + modifyDate + ", beginDate=" + beginDate + ", endDate=" + endDate + "]";
		}

	}

	public Order create(VideoData lineItem) throws BusinessException {
		ensureAcct();
		String entitlementId = "";
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
			// String id = String.valueOf(new Random().nextInt(1000000));
			String id = "53335324532453245";
			videoServicePlanAttributes = getModel().alloc().VideoServicePlanAttributes();
			videoServicePlanAttributes.video_service_plan_id.setValue(id);
		}

		for (String parcos : lineItem.getPackageList()) {
			action = findActionToPerform(parcos, vSubs);
			if (!action) {
				entitlementId = lineItem.getVideoEntitlementId() + "-" + parcos;
				VideoSubscription videoSubscription = getModel().alloc().VideoSubscription(entitlementId, parcos);
				videoSubscription.video_entitlement_id.setValue(entitlementId);
				videoSubscription.packageId.setValue(parcos);

				if (!lineItem.getBeginDate().equals("")) {
					videoSubscription.begin_date.setValue(OrderHelper.generateOrderDateStringFromString(lineItem.getBeginDate()));
				}
				if (!lineItem.getEndDate().equals("")) {
					videoSubscription.end_date.setValue(OrderHelper.generateOrderDateStringFromString(lineItem.getEndDate()));
				}

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
		if (action == Action.DELETE) {
			List<VideoSubscription> subscriptionList = getModel().find().VideoSubscription(id);
			if (subscriptionList == null || subscriptionList.size() == 0) {
				throw new BusinessException("Delete failed,  sik=%s was not found", id);
			}
			for (VideoSubscription videoSubscription : subscriptionList) {
				if (videoSubscription != null) {
					videoSubscription.sendAction(Action.DELETE);
				}
			}
			return true;
		} else if (action == Action.UPDATE) {
			VideoServicePlanAttributes planAttributes = getModel().find().VideoServicePlanAttributes();
			if (planAttributes != null) {
				planAttributes.modify_date.setValue(OrderHelper.generateOrderModifyDateStringFromDate(new Date()));
			}
		}
		return false;
	}

}

package dk.yousee.smp5.cases;

import java.text.SimpleDateFormat;
import java.util.Date;

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
		// video subscription
		private String videoEntitlementId;
		// Video Service Plan Attributes
		private String videoServicePlanId;
		// assoc
		private String macAddress;
		private String packageId;
		private String beginDate;
		private String endDate;
		private String modifyDate;

		public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		public String getVideoEntitlementId() {
			return videoEntitlementId;
		}

		public void setVideoEntitlementId(String videoEntitlementId) {
			this.videoEntitlementId = videoEntitlementId;
		}

		public String getVideoServicePlanId() {
			return videoServicePlanId;
		}

		public void setVideoServicePlanId(String videoServicePlanId) {
			this.videoServicePlanId = videoServicePlanId;
		}

		public String getMacAddress() {
			return macAddress;
		}

		public void setMacAddress(String macAddress) {
			this.macAddress = macAddress;
		}

		public String getPackageId() {
			return packageId;
		}

		public void setPackageId(String packageId) {
			this.packageId = packageId;
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

		public String getModifyDate() {
			return modifyDate;
		}

		public void setModifyDate(String modifyDate) {
			this.modifyDate = modifyDate;
		}

		@Override
		public String toString() {
			return "VideoData [videoEntitlementId=" + videoEntitlementId + ", videoServicePlanId=" + videoServicePlanId + ", macAddress="
					+ macAddress + ", packageId=" + packageId + ", beginDate=" + beginDate + ", endDate=" + endDate + ", modifyDate="
					+ modifyDate + "]";
		}

	}

	public Order create(VideoData lineItem) throws BusinessException {
		ensureAcct();
		validateRequired(lineItem);

		boolean update = getModel().find().VideoSubscription(lineItem.getVideoServicePlanId(), lineItem.getVideoEntitlementId()) != null;

		VideoServicePlanAttributes videoServicePlanAttributes = getModel().alloc().VideoServicePlanAttributes(
				lineItem.getVideoServicePlanId());
		videoServicePlanAttributes.video_service_plan_id.setValue(lineItem.getVideoServicePlanId());

		VideoSubscription videoSubscription = getModel().alloc().VideoSubscription(lineItem.getVideoServicePlanId(),
				lineItem.getVideoEntitlementId());
		videoSubscription.video_entitlement_id.setValue(lineItem.getVideoEntitlementId());

		if (!lineItem.getPackageId().equals("")) {
			videoSubscription.packageId.setValue(lineItem.getPackageId());
		}

		if (!lineItem.getBeginDate().equals("")) {
			videoSubscription.begin_date.setValue(OrderHelper.generateOrderDateStringFromString(lineItem.getBeginDate()));
		}

		if (!lineItem.getEndDate().equals("")) {
			videoSubscription.end_date.setValue(OrderHelper.generateOrderDateStringFromString(lineItem.getEndDate()));
		}

		if (update) {
			videoSubscription.modify_date.setValue(OrderHelper.generateOrderModifyDateStringFromDate(new Date()));
		}

		return getModel().getOrder();
	}

	/**
	 * @param lineItem
	 * @throws BusinessException
	 */
	private void validateRequired(VideoData lineItem) throws BusinessException {
		if (lineItem.getVideoServicePlanId() == null || lineItem.getVideoServicePlanId().equals("")) {
			throw new BusinessException("service plan id is required");
		}
		if (lineItem.getVideoEntitlementId() == null || lineItem.getVideoEntitlementId().equals("")) {
			throw new BusinessException("rate code is required");
		}
	}

	public boolean delete(String servicePlanId, String entitlementId) throws BusinessException {
		ensureAcct();
		boolean res;
		res = buildOrderFromAction(servicePlanId, entitlementId, Action.DELETE);
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
	private boolean buildOrderFromAction(String servicePlanId, String entitlementId, Action delete) throws BusinessException {
		// in this phase we can only remove subscriptions
		// VideoServicePlan videoServicePlan =
		// getModel().find().VideoServicePlan(servicePlanId);
		// if (videoServicePlan != null && entitlementId == null) {
		// videoServicePlan.sendAction(Action.DELETE);
		// return true;
		// }

		VideoSubscription videoSubscription = getModel().find().VideoSubscription(servicePlanId, entitlementId);
		if (videoSubscription != null) {
			videoSubscription.sendAction(Action.DELETE);
			return true;
		}
		throw new BusinessException("Delete failed, VideoSubscription:  servicePlanId=%s  and entitlementId=%s was not found",
				servicePlanId, entitlementId);

	}
}

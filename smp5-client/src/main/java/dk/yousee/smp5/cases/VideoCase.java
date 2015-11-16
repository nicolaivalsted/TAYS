package dk.yousee.smp5.cases;

import java.text.SimpleDateFormat;

import dk.yousee.smp5.casemodel.SubscriberModel;
import dk.yousee.smp5.casemodel.vo.video.VideoServicePlan;
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
		// Video Service Plan Attributes
		private String videoServicePlanId;
		// assoc
		private String macAddress;
		private String packageId;
		private String beginDate;
		private String endDate;

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

		@Override
		public String toString() {
			return "VideoData [videoEntitlementId=" + videoEntitlementId + ", videoServicePlanId=" + videoServicePlanId + ", macAddress="
					+ macAddress + ", packageId=" + packageId + ", beginDate=" + beginDate + ", endDate=" + endDate + "]";
		}

	}

	public Order create(VideoData lineItem) throws BusinessException {
		ensureAcct();

		if (lineItem.getVideoServicePlanId() == null) {
			throw new BusinessException("service plan id is required");
		}

		VideoServicePlanAttributes videoServicePlanAttributes = getModel().alloc().VideoServicePlanAttributes(
				lineItem.getVideoServicePlanId());
		videoServicePlanAttributes.video_service_plan_id.setValue(lineItem.getVideoServicePlanId());

		if (lineItem.getVideoEntitlementId() != null) {
			VideoSubscription videoSubscription = getModel().alloc().VideoSubscription(lineItem.getVideoServicePlanId(),
					lineItem.getVideoEntitlementId());
			videoSubscription.video_entitlement_id.setValue(lineItem.getVideoEntitlementId());
			videoSubscription.packageId.setValue(lineItem.getPackageId());
		}

		// if (lineItem.getMacAddress() != null &&
		// !lineItem.getMacAddress().equals("")) {
		// STBCas stbCas = getModel().find().STBCas(lineItem.getMacAddress());
		// if (stbCas != null) {
		// videoServicePlanAttributes.video_definition_has_cpe_conditional.add(stbCas);
		// logger.info("has_cpe_conditional was added for customer: " +
		// getAcct() + " and plan: " + lineItem.videoServicePlanId);
		// }
		// }
		return getModel().getOrder();
	}

	public boolean delete(String servicePlanId, String entitlementId) throws BusinessException {
		ensureAcct();
		boolean res;
		res = buildOrderFromAction(null, null, Action.DELETE);
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
		VideoServicePlan videoServicePlan = getModel().find().VideoServicePlan(servicePlanId);
		if (videoServicePlan != null && entitlementId == null) {
			videoServicePlan.sendAction(Action.DELETE);
			return true;
		}

		VideoSubscription videoSubscription = getModel().find().VideoSubscription(servicePlanId, entitlementId);
		if (videoSubscription != null) {
			videoSubscription.sendAction(Action.DELETE);
			return true;
		}
		throw new BusinessException("Delete failed, VideoSubscription:  servicePlanId=%s  and entitlementId=%s was not found",
				servicePlanId, entitlementId);

	}
}

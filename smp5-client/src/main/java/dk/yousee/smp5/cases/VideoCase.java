package dk.yousee.smp5.cases;

import java.text.SimpleDateFormat;

import org.apache.log4j.Logger;

import dk.yousee.smp5.casemodel.SubscriberModel;
import dk.yousee.smp5.casemodel.vo.BusinessPosition;
import dk.yousee.smp5.casemodel.vo.stb.STBCas;
import dk.yousee.smp5.casemodel.vo.video.VideoComposedService;
import dk.yousee.smp5.casemodel.vo.video.VideoServicePlanAttributes;
import dk.yousee.smp5.casemodel.vo.video.VideoSubscription;
import dk.yousee.smp5.order.model.Acct;
import dk.yousee.smp5.order.model.Action;
import dk.yousee.smp5.order.model.BusinessException;
import dk.yousee.smp5.order.model.Order;
import dk.yousee.smp5.order.model.OrderService;

public class VideoCase extends AbstractCase {
	private static final Logger logger = Logger.getLogger(VideoCase.class);

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
		private BusinessPosition position;
		// video subscription
		private String videoEntitlementId;
		// Video Service Plan Attributes
		private String videoServicePlanId;
		// assoc
		private String macAddress;
		// parcosCode???
		private String packageId;
		private String beginDate;
		private String endDate;

		// remover
		private String ippv_entitled;
		private String vod_entitled;
		private String interactive_service_entitled;

		public static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		public VideoData(BusinessPosition position) {
			super();
			this.position = position;
		}

		public String getVideoEntitlementId() {
			return videoEntitlementId;
		}

		public void setVideoEntitlementId(String videoEntitlementId) {
			this.videoEntitlementId = videoEntitlementId;
		}

		public BusinessPosition getPosition() {
			return position;
		}

		public void setPosition(BusinessPosition position) {
			this.position = position;
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

		public String getIppv_entitled() {
			return ippv_entitled;
		}

		public void setIppv_entitled(String ippv_entitled) {
			this.ippv_entitled = ippv_entitled;
		}

		public String getVod_entitled() {
			return vod_entitled;
		}

		public void setVod_entitled(String vod_entitled) {
			this.vod_entitled = vod_entitled;
		}

		public String getInteractive_service_entitled() {
			return interactive_service_entitled;
		}

		public void setInteractive_service_entitled(String interactive_service_entitled) {
			this.interactive_service_entitled = interactive_service_entitled;
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
			return "VideoData [position=" + position + ", videoEntitlementId=" + videoEntitlementId + ", videoServicePlanId="
					+ videoServicePlanId + ", macAddress=" + macAddress + ", packageId=" + packageId + ", beginDate=" + beginDate
					+ ", endDate=" + endDate + ", ippv_entitled=" + ippv_entitled + ", vod_entitled=" + vod_entitled
					+ ", interactive_service_entitled=" + interactive_service_entitled + "]";
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
		videoServicePlanAttributes.interactive_service_entitled.setValue(lineItem.getInteractive_service_entitled());
		videoServicePlanAttributes.ippv_entitled.setValue(lineItem.getIppv_entitled());
		videoServicePlanAttributes.vod_entitled.setValue(lineItem.getVod_entitled());

		if (lineItem.getVideoEntitlementId() != null) {
			VideoSubscription videoSubscription = getModel().alloc().VideoSubscription(lineItem.getVideoServicePlanId());
			videoSubscription.video_entitlement_id.setValue(lineItem.getVideoEntitlementId());
			videoSubscription.begin_date.setValue(lineItem.getBeginDate());
			videoSubscription.end_date.setValue(lineItem.getEndDate());
			videoSubscription.packageId.setValue(lineItem.getPackageId());
		}

		if (lineItem.getMacAddress() != null && !lineItem.getMacAddress().equals("")) {
			STBCas stbCas = getModel().find().STBCas(lineItem.getMacAddress());
			if (stbCas != null) {
				videoServicePlanAttributes.video_definition_has_cpe_conditional.add(stbCas);
				logger.info("has_cpe_conditional was added for customer: " + getAcct() + " and plan: " + lineItem.videoServicePlanId);
			}
		}
		return getModel().getOrder();
	}

	public boolean delete(BusinessPosition position) throws BusinessException {
		ensureAcct();
		boolean res;
		res = buildOrderFromAction(position, Action.DELETE);
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
	private boolean buildOrderFromAction(BusinessPosition position, Action delete) {
		VideoComposedService ottService = getModel().find().VideoComposedService();
		if (ottService != null) {
			ottService.sendAction(Action.DELETE);
			return true;
		}
		return false;
	}
}

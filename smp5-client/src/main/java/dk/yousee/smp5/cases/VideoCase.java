package dk.yousee.smp5.cases;

import org.apache.log4j.Logger;

import dk.yousee.smp5.casemodel.SubscriberModel;
import dk.yousee.smp5.casemodel.vo.BusinessPosition;
import dk.yousee.smp5.casemodel.vo.stb.STBCas;
import dk.yousee.smp5.casemodel.vo.video.VideoEvent;
import dk.yousee.smp5.casemodel.vo.video.VideoServicePlanAttributes;
import dk.yousee.smp5.casemodel.vo.video.VideoSubscription;
import dk.yousee.smp5.order.model.Acct;
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
		private String videoEntitlementId;
		private BusinessPosition position;
		private String videoEntitlementType;
		private String videoServicePlanId;
		private String macAddress;

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

		public String getVideoEntitlementType() {
			return videoEntitlementType;
		}

		public void setVideoEntitlementType(String videoEntitlementType) {
			this.videoEntitlementType = videoEntitlementType;
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

	}

	public Order create(VideoData lineItem) throws BusinessException {
		ensureAcct();

		if (lineItem.getVideoEntitlementType().equals("xyx")) {
			VideoEvent videoEvent = getModel().alloc().VideoEvent(lineItem.getPosition());
			videoEvent.video_entitlement_id.setValue(lineItem.getVideoEntitlementId());
		} else if (lineItem.getVideoEntitlementType().equals("xxx")) {
			VideoSubscription videoSubscription = getModel().alloc().VideoSubscription(lineItem.getPosition());
			videoSubscription.video_entitlement_id.setValue(lineItem.getVideoEntitlementId());
		}

		VideoServicePlanAttributes videoServicePlanAttributes = getModel().alloc().VideoServicePlanAttributes(lineItem.getPosition());
		videoServicePlanAttributes.video_service_plan_id.setValue(lineItem.getVideoServicePlanId());

		if (lineItem.getMacAddress() != null) {
			STBCas stbCas = getModel().find().STBCas(lineItem.getMacAddress());
			if (stbCas != null) {
				videoServicePlanAttributes.video_definition_has_cpe_conditional.add(stbCas);
				logger.info("has_cpe_conditional was added for customer: " + getAcct() + " and plan: " + lineItem.videoServicePlanId);
			}
		}

		return getModel().getOrder();
	}
}

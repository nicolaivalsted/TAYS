package dk.yousee.smp5.cases;

import dk.yousee.smp5.casemodel.SubscriberModel;
import dk.yousee.smp5.casemodel.vo.BusinessPosition;
import dk.yousee.smp5.casemodel.vo.video.VideoEvent;
import dk.yousee.smp5.casemodel.vo.video.VideoServicePlanAttributes;
import dk.yousee.smp5.casemodel.vo.video.VideoSubscription;
import dk.yousee.smp5.order.model.Acct;
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
		private String videoEventCode;
		private String videoSubscriptionCode;
		private String videoSDefCode;
		private BusinessPosition position;
		private String videoServiceUniqueCheck;
		private String ippvEntitled;
		private String vodEntitled;
		private String interactiveServiceEntitled;
		private String analogEntitled;
		private String videoEntitlementType;
		private String videoEntitlementUniquenessCheck;
		private String endDate;

		public VideoData(BusinessPosition position) {
			super();
			this.position = position;
		}

		public String getVideoServiceUniqueCheck() {
			return videoServiceUniqueCheck;
		}

		public void setVideoServiceUniqueCheck(String videoServiceUniqueCheck) {
			this.videoServiceUniqueCheck = videoServiceUniqueCheck;
		}

		public String getIppvEntitled() {
			return ippvEntitled;
		}

		public void setIppvEntitled(String ippvEntitled) {
			this.ippvEntitled = ippvEntitled;
		}

		public String getVodEntitled() {
			return vodEntitled;
		}

		public void setVodEntitled(String vodEntitled) {
			this.vodEntitled = vodEntitled;
		}

		public String getInteractiveServiceEntitled() {
			return interactiveServiceEntitled;
		}

		public void setInteractiveServiceEntitled(String interactiveServiceEntitled) {
			this.interactiveServiceEntitled = interactiveServiceEntitled;
		}

		public String getAnalogEntitled() {
			return analogEntitled;
		}

		public void setAnalogEntitled(String analogEntitled) {
			this.analogEntitled = analogEntitled;
		}

		public String getVideoEntitlementType() {
			return videoEntitlementType;
		}

		public void setVideoEntitlementType(String videoEntitlementType) {
			this.videoEntitlementType = videoEntitlementType;
		}

		public String getVideoEntitlementUniquenessCheck() {
			return videoEntitlementUniquenessCheck;
		}

		public void setVideoEntitlementUniquenessCheck(String videoEntitlementUniquenessCheck) {
			this.videoEntitlementUniquenessCheck = videoEntitlementUniquenessCheck;
		}

		public String getEndDate() {
			return endDate;
		}

		public void setEndDate(String endDate) {
			this.endDate = endDate;
		}

		public BusinessPosition getPosition() {
			return position;
		}

		public void setPosition(BusinessPosition position) {
			this.position = position;
		}

		public String getVideoEventCode() {
			return videoEventCode;
		}

		public void setVideoEventCode(String videoEventCode) {
			this.videoEventCode = videoEventCode;
		}

		public String getVideoSubscriptionCode() {
			return videoSubscriptionCode;
		}

		public void setVideoSubscriptionCode(String videoSubscriptionCode) {
			this.videoSubscriptionCode = videoSubscriptionCode;
		}

		public String getVideoSDefCode() {
			return videoSDefCode;
		}

		public void setVideoSDefCode(String videoSDefCode) {
			this.videoSDefCode = videoSDefCode;
		}

	}

	public Order create(VideoData lineItem) throws BusinessException {
		ensureAcct();

		VideoEvent videoEvent = getModel().alloc().VideoEvent(lineItem.getPosition());
		videoEvent.video_entitlement_type.setValue(lineItem.getVideoEntitlementType());
		videoEvent.video_entitlement_uniqueness_check.setValue(lineItem.getVideoEntitlementUniquenessCheck());
		videoEvent.end_date.setValue(lineItem.getEndDate());

		VideoServicePlanAttributes videoServicePlanAttributes = getModel().alloc().VideoServicePlanAttributes(lineItem.getPosition());
		videoServicePlanAttributes.analog_entitled.setValue(lineItem.getAnalogEntitled());
		videoServicePlanAttributes.interactive_service_entitled.setValue(lineItem.getInteractiveServiceEntitled());
		videoServicePlanAttributes.ippv_entitled.setValue(lineItem.getIppvEntitled());
		videoServicePlanAttributes.vod_entitled.setValue(lineItem.getVodEntitled());

		VideoSubscription videoSubscription = null;

		videoSubscription = getModel().alloc().VideoSubscription(lineItem.getPosition());
		videoSubscription.video_entitlement_type.setValue(lineItem.getVideoEntitlementType());
		videoSubscription.video_entitlement_uniqueness_check.setValue(lineItem.getVideoEntitlementUniquenessCheck());

		return getModel().getOrder();
	}
}

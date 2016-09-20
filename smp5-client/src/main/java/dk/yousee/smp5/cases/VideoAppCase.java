package dk.yousee.smp5.cases;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import dk.yousee.smp5.casemodel.SubscriberModel;
import dk.yousee.smp5.casemodel.vo.video.AppSubscription;
import dk.yousee.smp5.casemodel.vo.video.VideoServicePlanAttributes;
import dk.yousee.smp5.order.model.Acct;
import dk.yousee.smp5.order.model.Action;
import dk.yousee.smp5.order.model.BusinessException;
import dk.yousee.smp5.order.model.Order;
import dk.yousee.smp5.order.model.OrderService;

/**
 * @author m64746
 *
 *         Date: Sep 18, 2016 Time: 12:33:23 PM
 */
public class VideoAppCase extends AbstractCase {
	public static final String VIDEO_SERVICE_ID = "53335324532453245";

	/**
	 * @param acct
	 * @param service
	 */
	public VideoAppCase(Acct acct, OrderService service) {
		super(acct, service);
	}

	/**
	 * @param model
	 * @param service
	 */
	public VideoAppCase(SubscriberModel model, OrderService service) {
		super(model, service);
	}

	public VideoAppCase(SubscriberCase customerCase, boolean keepModel) {
		super(selectModel(customerCase.getModel(), keepModel), customerCase.getService());
	}

	public static class VideoAppData {
		private String id;
		private String channelId;
		private String name;
		private String cableUnit;

		public String getId() {
			return id;
		}

		public void setId(String id) {
			this.id = id;
		}

		public String getChannelId() {
			return channelId;
		}

		public void setChannelId(String channelId) {
			this.channelId = channelId;
		}

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getCableUnit() {
			return cableUnit;
		}

		public void setCableUnit(String cableUnit) {
			this.cableUnit = cableUnit;
		}

	}

	/**
	 * @param lineItem
	 * @throws BusinessException
	 */
	public Order create(VideoAppData lineItem) throws BusinessException {
		ensureAcct();

		String sik = getModel().getAcct().toString() + "-" + lineItem.getId() + "-" + lineItem.getName();

		AppSubscription appSubscription = getModel().alloc().AppSubscription(sik, getModel().getAcct().toString());
		appSubscription.sik.setValue(sik);
		appSubscription.channel_id.setValue(lineItem.getChannelId());
		appSubscription.name.setValue(lineItem.getName());

		VideoServicePlanAttributes videoServicePlanAttributes = getModel().find().VideoServicePlanAttributes();
		if (videoServicePlanAttributes == null) {
			videoServicePlanAttributes = getModel().alloc().VideoServicePlanAttributes(getAcct().toString());
			videoServicePlanAttributes.video_service_plan_id.setValue(VIDEO_SERVICE_ID);
		} else {
			videoServicePlanAttributes.modify_date.setValue(generateModifyDate());
		}

		if (!lineItem.getCableUnit().equals(videoServicePlanAttributes.cableUnit.getValue())) {
			videoServicePlanAttributes.cableUnit.setValue(lineItem.getCableUnit());
		}

		return getModel().getOrder();
	}

	/**
	 * @param id
	 * @param delete
	 * @return
	 */
	public Boolean sendAction(String sik, Action action) {
		AppSubscription appSubscription = getModel().find().AppSubscription(sik);
		if (appSubscription != null) {
			appSubscription.sendAction(action);
			return true;
		}
		return false;
	}

	public static String generateModifyDate() throws BusinessException {
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss:SSS");
		String dateFinal = sdf.format(new Date()) + "-" + new Random().nextInt(5000);
		return dateFinal;
	}

}

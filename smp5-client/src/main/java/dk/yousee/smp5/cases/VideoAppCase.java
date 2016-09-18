package dk.yousee.smp5.cases;

import dk.yousee.smp5.casemodel.SubscriberModel;
import dk.yousee.smp5.order.model.Acct;
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

	}

}

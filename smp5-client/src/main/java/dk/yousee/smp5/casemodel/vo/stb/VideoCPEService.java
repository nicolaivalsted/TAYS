package dk.yousee.smp5.casemodel.vo.stb;

import java.util.ArrayList;
import java.util.List;

import dk.yousee.smp5.casemodel.SubscriberModel;
import dk.yousee.smp5.casemodel.vo.helpers.BasicUnit;
import dk.yousee.smp5.order.model.NickName;
import dk.yousee.smp5.order.model.OrderDataLevel;
import dk.yousee.smp5.order.model.OrderDataType;
import dk.yousee.smp5.order.model.ServicePrefix;

/**
 * @author m64746
 *
 * Date: 26/10/2015
 * Time: 17:31:02
 */
public class VideoCPEService extends BasicUnit {
	public static OrderDataLevel LEVEL = OrderDataLevel.SERVICE;
    public static OrderDataType TYPE = new OrderDataType( ServicePrefix.SubSvcSpec,"video_cpe_equipment");
    public static NickName NAME = new NickName("video_cpe");
    
    List<VideoCPE> videoCPEs = new ArrayList<VideoCPE>();
    
	public VideoCPEService(SubscriberModel model, String externalKey){
		super(model,externalKey,TYPE,LEVEL,NAME,null);
		model.getServiceLevelUnit().add(this);
	}

	public List<VideoCPE> getVideoCPEs() {
		return videoCPEs;
	}

	public void setVideoCPEs(List<VideoCPE> videoCPEs) {
		this.videoCPEs = videoCPEs;
	}

}

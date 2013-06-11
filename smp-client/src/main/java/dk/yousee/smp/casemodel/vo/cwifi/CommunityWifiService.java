package dk.yousee.smp.casemodel.vo.cwifi;

import dk.yousee.smp.casemodel.SubscriberModel;
import dk.yousee.smp.casemodel.vo.BusinessPosition;
import dk.yousee.smp.casemodel.vo.helpers.BasicUnit;
import dk.yousee.smp.order.model.NickName;
import dk.yousee.smp.order.model.OrderDataLevel;
import dk.yousee.smp.order.model.OrderDataType;
import dk.yousee.smp.order.model.ServicePrefix;

/**
 * Created by IntelliJ IDEA. User: aka Date: 28/02/12 Time: 15.54 Play service
 * for music
 */
public class CommunityWifiService extends BasicUnit {
    public static OrderDataLevel LEVEL = OrderDataLevel.SERVICE;
    public static OrderDataType TYPE = new OrderDataType(ServicePrefix.SubSvcSpec, "community_wifi_composed");
    public static NickName NAME = new NickName("cwifi");

    public CommunityWifiService(SubscriberModel model, String externalKey) {
        super(model, externalKey, TYPE, LEVEL, NAME, null);
        model.getServiceLevelUnit().add(this);
    }
    //children
    private CommunityWifi communityWifi;

    public CommunityWifi getCommunityWifi() {
        return communityWifi;
    }

    public void setCommunityWifi(CommunityWifi communityWifi) {
        this.communityWifi = communityWifi;
    }

    public BusinessPosition getPosition() {
        return getCommunityWifi() == null ? null : getCommunityWifi().getPosition();
    }
}

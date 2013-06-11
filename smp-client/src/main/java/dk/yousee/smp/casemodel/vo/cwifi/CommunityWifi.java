package dk.yousee.smp.casemodel.vo.cwifi;

import dk.yousee.smp.casemodel.SubscriberModel;
import dk.yousee.smp.casemodel.vo.BusinessPosition;
import dk.yousee.smp.casemodel.vo.helpers.BasicUnit;
import dk.yousee.smp.casemodel.vo.helpers.PropHolder;
import dk.yousee.smp.order.model.OrderDataLevel;
import dk.yousee.smp.order.model.OrderDataType;
import dk.yousee.smp.order.model.ServicePrefix;

/**
 * Definition of play service
 */
public class CommunityWifi extends BasicUnit {

    public static OrderDataLevel LEVEL = OrderDataLevel.CHILD_SERVICE;
    public static OrderDataType TYPE = new OrderDataType(ServicePrefix.SubSvcSpec,"community_wifi");


    public CommunityWifi(SubscriberModel model, String externalKey, CommunityWifiService parent) {
        super(model, externalKey, TYPE, LEVEL,null,parent);
        parent.setCommunityWifi(this);
    }

    public CommunityWifi(SubscriberModel model, BusinessPosition position) {
        super(model, model.key().generateUUID(), TYPE, LEVEL,null, new CommunityWifiService(model,model.key().generateUUID()));
        business_position.setValue(position.getId());
        getParent().setCommunityWifi(this);
    }
    
    public CommunityWifiService getParent() {
        return (CommunityWifiService) super.getParent();
    }

    /**
     * Name of field
     */
    public static final String BUSINESS_POSITION = "business_position";
    /**
     * identifier that identify the subscribers modem among all the modems the subscriber has.
     * Field value can be "1", "2" etc. It is only required to be unique for the subscriber.
     * So two different subscribers can both have position called "1"
     * The objective is to manage relation to CRM subscription.
     * This is an instance key to service plan. It is normally never modified.
     * YouSee will fill in modem_id / aftale Nr / ... tbd.
     */
    protected PropHolder business_position = new PropHolder(this, BUSINESS_POSITION);

    BusinessPosition getPosition() {
        return BusinessPosition.create(business_position.getValue());
    }

    public static final String YSPRO_PCODE ="yspro_pcode";
    public PropHolder yspro_pcode = new PropHolder(this, YSPRO_PCODE,true);

    public static final String YSPRO_PROVISIONINGID="yspro_provisioningid";
    public PropHolder yspro_provisioningid = new PropHolder(this, YSPRO_PROVISIONINGID);
    
    public static final String MAX_DEVICE ="max_device";
    public PropHolder max_device = new PropHolder(this, MAX_DEVICE, true);
    
    public static final String SPEED_PROFILE ="speed_profile";
    public PropHolder speed_profile = new PropHolder(this, SPEED_PROFILE);
}

package dk.yousee.smp5.casemodel.vo.ott;

import dk.yousee.smp5.casemodel.vo.BusinessPosition;
import dk.yousee.smp5.casemodel.vo.helpers.PropHolder;
import dk.yousee.smp5.casemodel.SubscriberModel;
import dk.yousee.smp5.order.model.Constants;
import dk.yousee.smp5.order.model.OrderDataLevel;
import dk.yousee.smp5.order.model.OrderDataType;
import dk.yousee.smp5.order.model.ServicePrefix;
import dk.yousee.smp5.casemodel.vo.helpers.BasicUnit;

/**
 * @author m64746
 *
 * Date: 21/10/2015
 * Time: 12:04:15
 */
public class OTTSubscription extends BasicUnit {
	public static OrderDataLevel LEVEL = OrderDataLevel.CHILD_SERVICE;
    public static OrderDataType TYPE = new OrderDataType(ServicePrefix.SubSvcSpec,"ott_subscription");
    
    public PropHolder ott_entitlement_desc = new PropHolder(this, Constants.OTT_SUBSCRIPTION_DESCRIPTION, false);
    public PropHolder ott_entitlement_type = new PropHolder(this, Constants.OTT_ENTITLEMENT_TYPE, true);
    public PropHolder ott_entitlement_uniqueness_check = new PropHolder(this, Constants.OTT_ENTITLEMENT_UNIQUENESS_CHECK, true);
    public PropHolder bc_ott_prov_tags = new PropHolder(this, Constants.BC_OTT_PROV_TAGS, false);
    public PropHolder ppv_ott_prov_tags = new PropHolder(this, Constants.PPV_OTT_PROV_TAGS, false);
    public PropHolder ppv_ott_service_tags = new PropHolder(this, Constants.PPV_OTT_SERVICE_TAGS, false);
    public PropHolder vod_prov_tags = new PropHolder(this, Constants.VOD_PROV_TAGS, false);
    public PropHolder begin_date = new PropHolder(this, Constants.BEGIN_DATE, false);
    public PropHolder end_date = new PropHolder(this, Constants.END_DATE, false);
    public PropHolder taping_authorization = new PropHolder(this, Constants.TAPING_AUTHORIZATION, false);
    public PropHolder force_cas_assignment = new PropHolder(this, Constants.FORCE_CAS_ASSIGNMENT, false);
    public PropHolder service_name = new PropHolder(this, Constants.SERVICE_NAME, true);
    public PropHolder rate_code = new PropHolder(this, Constants.RATE_CODE, true);
    public PropHolder product_id = new PropHolder(this, Constants.PRODUCT_ID, true);
    public PropHolder business_position = new PropHolder(this, Constants.BUSINESS_POSITION, false);
    public PropHolder ott_product = new PropHolder(this, Constants.OTT_PRODUCT, true);
    public PropHolder uuid = new PropHolder(this, Constants.UUID, false);
    
    public OTTSubscription(SubscriberModel model, String externalKey, OTTService parent) {
        super(model, externalKey, TYPE, LEVEL,null,parent);
        parent.setOttSubscription(this);
    }
    
    public OTTSubscription(SubscriberModel model, BusinessPosition position){
    	super(model, model.key().generateUUID(),TYPE,LEVEL,null, new OTTService(model, model.key().generateUUID()));
    	business_position.setValue(position.getId());
    	getParent().setOttSubscription(this);
    }
 
	public OTTService getParent(){
    	return (OTTService)super.getParent();
    }
    
    public BusinessPosition getPosition(){
		return BusinessPosition.create(business_position.getValue());
	}
}

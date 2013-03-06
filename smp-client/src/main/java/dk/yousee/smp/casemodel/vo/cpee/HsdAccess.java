package dk.yousee.smp.casemodel.vo.cpee;

import dk.yousee.smp.casemodel.SubscriberModel;
import dk.yousee.smp.casemodel.vo.ModemId;
import dk.yousee.smp.casemodel.vo.base.SubAddressSpec;
import dk.yousee.smp.casemodel.vo.helpers.AssociationHolder;
import dk.yousee.smp.casemodel.vo.helpers.BasicUnit;
import dk.yousee.smp.casemodel.vo.helpers.PropHolder;
import dk.yousee.smp.order.model.OrderDataLevel;
import dk.yousee.smp.order.model.OrderDataType;
import dk.yousee.smp.order.model.ServicePrefix;

/**
 * Created by IntelliJ IDEA.
 * User: m14857
 * Date: Oct 12, 2010
 * Time: 2:42:28 PM
 * Value object for eMTA Cable Modem
 * Data structure reference to YouSee Data Migration Requirements: 5.3.1 Cable Modem/SMP HSD Access
 */
public class HsdAccess extends BasicUnit {

    public static OrderDataLevel LEVEL = OrderDataLevel.CHILD_SERVICE;
    public static OrderDataType TYPE = new OrderDataType( ServicePrefix.SubSvcSpec,"smp_emta_cm_hsd_access");

    public HsdAccess(SubscriberModel model, String externalKey, CpeComposedService parent) {
        super(model, externalKey, TYPE, LEVEL,null, parent);
        parent.setHsdAccess(this);
        cm_service_id.updateValue(externalKey);
    }

    //Type.FEATURE  property_value all_ip_mapping
    public PropHolder cm_service_id = new PropHolder(this, "cm_service_id", true);
    public PropHolder cm_mac = new PropHolder(this, "cm_mac", true);
    public PropHolder docsis_3_capable = new PropHolder(this, "docsis_3_capable", true);

    /**
     * Key to customers equipment seen from an activation activity step
     * (NOT from CRM level - because CRM does not manage modem identity)
     */
    private PropHolder cm_ownership = new PropHolder(this, "cm_ownership", true);

    public ModemId getCmOwnership() {
        return ModemId.create(cm_ownership.getValue());
    }

    public void setCmOwnership(ModemId cmOwnership) {
        if(cmOwnership==null)throw new IllegalArgumentException("cm ownsership can never be null in Hsd-access");
        cm_ownership.setValue(cmOwnership.getId());
    }

    public PropHolder wifi_capable = new PropHolder(this, "wifi_capable");
    public PropHolder gi_address = new PropHolder(this, "gi_address");
    public PropHolder svc_provider_nm = new PropHolder(this, "svc_provider_nm");
    public PropHolder cm_technology = new PropHolder(this, "cm_technology");
    public PropHolder max_num_cpe = new PropHolder(this, "max_num_cpe");
    public PropHolder equipment_type = new PropHolder(this, "equipment_type");
    
    //M5 new asu can read this from bacc now
    public PropHolder cm_manufacturer = new PropHolder(this, "cm_manufacturer");
    public PropHolder cm_serial_number = new PropHolder(this, "cm_serial_number");
    public PropHolder cm_model = new PropHolder(this, "cm_model");
//    public PropHolder property_value = new PropHolder(this, "property_value", true);
//    public PropHolder all_ip_mapping = new PropHolder(this, "all_ip_mapping", true);
//    public PropHolder dflt_cm_dhcp_rules = new PropHolder(this, "dflt_cm_dhcp_rules");
//    public PropHolder dflt_cpe_dhcp_rules = new PropHolder(this, "dflt_cpe_dhcp_rules");
//    public PropHolder dflt_quality_of_svc = new PropHolder(this, "dflt_quality_of_svc");
    public PropHolder class_of_service = new PropHolder(this, "class_of_service",false);


    //Type.ASSOC
    public AssociationHolder service_on_address = new AssociationHolder(this, "service_on_address", SubAddressSpec.TYPE );

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("{");
        sb.append(" cm_ownership=").append(getCmOwnership());
        sb.append('}');
        return sb.toString();
    }
}

package dk.yousee.smp.cases;

import dk.yousee.smp.casemodel.SubscriberModel;
import dk.yousee.smp.casemodel.vo.ModemId;
import dk.yousee.smp.casemodel.vo.cbp.CableBBService;
import dk.yousee.smp.casemodel.vo.cbp.InetAccess;
import dk.yousee.smp.casemodel.vo.cpee.HsdAccess;
import dk.yousee.smp.order.model.Acct;
import dk.yousee.smp.order.model.BusinessException;
import dk.yousee.smp.order.model.OrderService;

/**
 *
 * @author m27236
 */
public class CableBBActivationCase extends MacAddressCase{
    
        
    public CableBBActivationCase(Acct acct, OrderService orderService){
        super(acct, orderService);
    }
    
    public CableBBActivationCase(SubscriberModel model, OrderService orderService){
        super(model, orderService);
    }
    
    public Boolean activateM5cmModem(MacAddressCase.HsdAccessData hsdAccessData, CableBBService cableBBService)throws BusinessException{
        Boolean hasImpact = null;
             
        HsdAccess hsdAccess = null;
        if(getModel().find().HsdAccess(cableBBService.getModemId()) !=null) {
            //cahnge modem
            if(getModel().find().HsdAccess(cableBBService.getModemId()).cm_mac.getValue() !=null){
                String oldmac = getModel().find().HsdAccess(cableBBService.getModemId()).cm_mac.getValue();
                
               if(!oldmac.equals(hsdAccessData.getCm_mac())){
                   hsdAccess = assignCMMacAddressForHsdAccess(hsdAccessData.getCm_mac(), hsdAccessData, cableBBService.getModemId());
                   hasImpact = true;
               }
                   }
        }else {
            //new customer no modem
            hsdAccess = addHsdAccess(cableBBService.getModemId(), hsdAccessData);
            hasImpact = true;
        }
        
        if(hsdAccess!=null){
            addAssocInternet_access_has_emta_cmForInetAccess(hsdAccess, cableBBService.getModemId());
        }      
        
        return hasImpact;
    }
        
    @Override
     public InetAccess addAssocInternet_access_has_emta_cmForInetAccess(HsdAccess hsdAccess, ModemId modemId) throws BusinessException {
        InetAccess inetAccess = getModel().find().InetAccess(modemId);
        if (inetAccess.internet_access_has_emta_cm.get() == null) {
            inetAccess.internet_access_has_emta_cm.add(hsdAccess);
        }
        return inetAccess;
    }
    
  /*  public static class HsdAccessData {
        private String cm_mac;
        private String wifi_capable;
        private String docsis_3_capable;
        private String gi_address;
        private String equipment_type;
        private String svc_provider_nm;
        private String cm_technology;
        private String max_num_cpe;
//        private String service_on_address;

        public String getCm_mac() {
            return cm_mac;
        }

        public void setCm_mac(String cm_mac) {
            this.cm_mac = cm_mac;
        }

        public String getWifi_capable() {
            return wifi_capable;
        }

        public void setWifi_capable(String wifi_capable) {
            this.wifi_capable = wifi_capable;
        }

        public String getDocsis_3_capable() {
            return docsis_3_capable;
        }

        public void setDocsis_3_capable(String docsis_3_capable) {
            this.docsis_3_capable = docsis_3_capable;
        }

        public String getGi_address() {
            return gi_address;
        }

        public void setGi_address(String gi_address) {
            this.gi_address = gi_address;
        }

        public String getEquipment_type() {
            return equipment_type;
        }

        public void setEquipment_type(String equipment_type) {
            this.equipment_type = equipment_type;
        }

        public String getSvc_provider_nm() {
            return svc_provider_nm;
        }

        public void setSvc_provider_nm(String svc_provider_nm) {
            this.svc_provider_nm = svc_provider_nm;
        }

        public String getCm_technology() {
            return cm_technology;
        }

        public void setCm_technology(String cm_technology) {
            this.cm_technology = cm_technology;
        }

        public String getMax_num_cpe() {
            return max_num_cpe;
        }

        public void setMax_num_cpe(String max_num_cpe) {
            this.max_num_cpe = max_num_cpe;
        }
//        public String getService_on_address() {
//            return service_on_address;
//        }
//        public void setService_on_address(String service_on_address) {
//            this.service_on_address = service_on_address;
//        }
    } */
}

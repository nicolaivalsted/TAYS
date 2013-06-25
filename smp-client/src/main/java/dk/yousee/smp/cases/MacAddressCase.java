package dk.yousee.smp.cases;

import dk.yousee.smp.casemodel.SubscriberModel;
import dk.yousee.smp.casemodel.vo.BusinessPosition;
import dk.yousee.smp.casemodel.vo.ModemId;
import dk.yousee.smp.casemodel.vo.cbp.AddnCpe;
import dk.yousee.smp.casemodel.vo.cbp.CableBBService;
import dk.yousee.smp.casemodel.vo.cbp.InetAccess;
import dk.yousee.smp.casemodel.vo.cbp.SMPWiFi;
import dk.yousee.smp.casemodel.vo.cbp.StdCpe;
import dk.yousee.smp.casemodel.vo.cpee.HsdAccess;
import dk.yousee.smp.casemodel.vo.cpee.VoipAccess;
import dk.yousee.smp.casemodel.vo.cvp.DialToneAccess;
import dk.yousee.smp.casemodel.vo.cwifi.CommunityWifi;
import dk.yousee.smp.order.model.Acct;
import dk.yousee.smp.order.model.Action;
import dk.yousee.smp.order.model.BusinessException;
import dk.yousee.smp.order.model.OrderService;
import dk.yousee.smp.order.model.ProvisionStateEnum;

/**
 * Created by IntelliJ IDEA.
 * User: m14857
 * Date: Oct 13, 2010
 * Time: 3:16:21 PM
 * The usecase used for change the existing customer's MacAddress.
 * https://yousee.jira.com/wiki/display/SMP/Environment+%28systems+overview%29
 * https://yousee.jira.com/wiki/display/KASIA/provisioning-development
 * https://yousee.jira.com/wiki/pages/viewpageattachments.action?pageId=12779807
 * mvn -f bss-adapter-war/pom.xml jetty:run -Dhttp.proxyHost=sltarray02.tdk.dk -Dhttp.proxyPort=8080
 * mvndebug -f bss-adapter-war/pom.xml jetty:run -Dhttp.proxyHost=sltarray02.tdk.dk -Dhttp.proxyPort=8080    (To debug)
 * mvndebug -f bss-adapter-war/pom.xml  -Djetty.port=9999 jetty:run -Dhttp.proxyHost=sltarray02.tdk.dk -Dhttp.proxyPort=8080    (To debug)
 * <activation>
 * <activeByDefault>true</activeByDefault>
 * </activation>
 * <p/>
 * mvn jetty:run -Djetty.port=9999 -Dhttp.proxyHost=sltarray02.tdk.dk -Dhttp.proxyPort=8080
 */
public class MacAddressCase extends AbstractCase {
    /**
     * Constructor with the customer account.
     *
     * @param acct    account id
     * @param service service to sigma
     */
    public MacAddressCase(Acct acct, OrderService service) {
        super(acct, service);
    }

    public MacAddressCase(SubscriberModel model, OrderService service) {
        super(model, service);
    }

    public CableBBService findServiceByActivationCode(String modemActivationCode){
        return getModel().find().firstCableBBService(modemActivationCode);
    }

    /**
     * change the customer's  Mac Address for HsdAccess, externalKey is the key of  service level plan.
     *
     * @param macAddress mac address to assign
     * @param hsdAccessData in
     * @param modemId    modem used
     * @return model instance
     * @throws BusinessException 
     */
    public HsdAccess assignCMMacAddressForHsdAccess(String macAddress, HsdAccessData hsdAccessData, ModemId modemId) throws BusinessException {
        HsdAccess ha = getModel().alloc().HsdAccess(modemId);
        if(ha.getServicePlanState()==ProvisionStateEnum.COURTESY_BLOCK){
            ha.sendAction(Action.SUSPEND);
        }
        ha.cm_mac.setValue(macAddress);
        if (hsdAccessData != null) {
            ha.wifi_capable.setValue(hsdAccessData.getWifi_capable());
            ha.docsis_3_capable.setValue(hsdAccessData.getDocsis_3_capable());
            ha.gi_address.setValue(hsdAccessData.getGi_address());
            ha.setCmOwnership(modemId);
            ha.equipment_type.setValue(hsdAccessData.getEquipment_type());
            ha.svc_provider_nm.setValue(hsdAccessData.getSvc_provider_nm());
            ha.cm_technology.setValue(hsdAccessData.getCm_technology());
            ha.max_num_cpe.setValue(hsdAccessData.getMax_num_cpe());
            ha.cm_manufacturer.setValue(hsdAccessData.getCm_manufacturer());
            ha.cm_model.setValue(hsdAccessData.getCm_model());
            ha.cm_serial_number.setValue(hsdAccessData.getCm_serial_number());
        }
        
        CommunityWifi cwifi = getModel().find().CommunityWifi(new BusinessPosition(modemId.getId()));
        if (cwifi != null && ha.community_wifi.isEmpty()) {
            ha.community_wifi.add(cwifi);
        }
        
        return ha;
    }

    /**
     * change the customer's  Mac Address for InetAccess, externalKey is the key of  service level plan.
     *
     * @param macAddress mac address to assign
     * @param modemId    modem used
     * @return model instance
     */
    public InetAccess assignCMMacAddressForInetAccess(String macAddress, ModemId modemId) {
        InetAccess inetAccess = getModel().alloc().InetAccess(modemId);
        inetAccess.cm_mac.setValue(macAddress);
        return inetAccess;
    }

    /**
     * change the customer's  Mac Address for StdCpe, externalKey is the key of  service level plan.
     *
     * @param macAddress mac address to assign
     * @param modemId    modem used
     * @return model instance
     */
    public StdCpe assignCPEMacAddressForStdCpe(String macAddress, ModemId modemId) {
        StdCpe stdCpe = getModel().alloc().StdCpe(modemId);
        if(stdCpe.getServicePlanState()==ProvisionStateEnum.COURTESY_BLOCK){
            stdCpe.sendAction(Action.SUSPEND);
        }
        stdCpe.cpe_mac.setValue(macAddress);
        return stdCpe;
    }

    /**
     * change the customer's  Mac Address for StdCpe, externalKey is the key of  service level plan.
     *
     * @param macAddress mac address to assign
     * @param modemId    modem used
     * @return model instance
     */
    public StdCpe assignCMMacAddressForStdCpe(String macAddress, ModemId modemId) {
        StdCpe stdCpe = getModel().alloc().StdCpe(modemId);
        if(stdCpe.getServicePlanState()==ProvisionStateEnum.COURTESY_BLOCK){
            stdCpe.sendAction(Action.SUSPEND);
        }
        stdCpe.cm_mac.setValue(macAddress);
        return stdCpe;
    }

    /**
     * change the customer's  Mac Address for VoipAccess, externalKey is the key of  service level plan.
     *
     * @param macAddress mac address to assign
     * @param modemId    modem used
     * @return model instance
     */
    public VoipAccess assignMTAMacAddressForVoipAccess(String macAddress, ModemId modemId) {
        VoipAccess voipAccess = getModel().alloc().VoipAccess(modemId);
        voipAccess.mta_mac.setValue(macAddress);
        return voipAccess;
    }

    /**
     * add association for InetAccess
     *
     * @param hsdAccess service
     * @param modemId   modem used
     * @return model instance
     * @throws dk.yousee.smp.order.model.BusinessException
     *          when some problem with link.
     */
    public InetAccess addAssocInternet_access_has_emta_cmForInetAccess(HsdAccess hsdAccess, ModemId modemId) throws BusinessException {
        InetAccess inetAccess = getModel().alloc().InetAccess(modemId);
        if (inetAccess.internet_access_has_emta_cm.get() == null) {
            inetAccess.internet_access_has_emta_cm.add(hsdAccess);
        }
        return inetAccess;
    }

    /**
     * update mac for AddnCpe
     *
     * @param modemId  modem used
     * @param child_id ,     externalkey for AddnCpe
     * @param cm_mac   ,       cm_mac for AddnCpe
     * @return model instance
     * @throws dk.yousee.smp.order.model.BusinessException
     *          when relation not exists
     */
    public AddnCpe updateCm_macForAddnCpe(ModemId modemId, String child_id, String cm_mac) throws BusinessException {
        AddnCpe addnCpe = getModel().find().AddnCpe(modemId, child_id);
        addnCpe.cm_mac.setValue(cm_mac);
        return addnCpe;
    }

    /**
     * update mac for AddnCpe
     *
     * @param modemId  modem used
     * @param child_id ,     externalkey for AddnCpe
     * @param cpe_mac  ,       cpe_mac for AddnCpe
     * @return model instance
     * @throws dk.yousee.smp.order.model.BusinessException
     *          when relation not exists
     */
    public AddnCpe updateCpe_macForAddnCpe(ModemId modemId, String child_id, String cpe_mac) throws BusinessException {
        AddnCpe addnCpe = null;
        if (child_id == null || child_id.length() == 0) {
            if (getModel().find().AddnCpe(modemId) != null && getModel().find().AddnCpe(modemId).size() > 0)
                addnCpe = getModel().find().AddnCpe(modemId).get(0);
        } else {
            addnCpe = getModel().find().AddnCpe(modemId, child_id);
        }
        if (addnCpe != null) {
            addnCpe.cpe_mac.setValue(cpe_mac);
        }
        return addnCpe;
    }

    /**
     * add childservice 'AddnCpe'
     *
     * @param modemId       modem used
     * @param cpe_mac,      cpe_mac for  AddnCpe
     * @param product_code, product_code for AddnCpe
     * @param cm_mac,       cm_mac for AddnCpe
     * @return model instance
     */
    public AddnCpe addAddnCpe(ModemId modemId, String cpe_mac, String product_code, String cm_mac) {
        AddnCpe addnCpe = getModel().add().AddnCpe(modemId);
        addnCpe.cpe_mac.setValue(cpe_mac);
        addnCpe.cpe_product_code.setValue(product_code);
        addnCpe.cpe_service_id.setValue(addnCpe.getExternalKey());
        addnCpe.cm_mac.setValue(cm_mac);
        return addnCpe;
    }

    /**
     * delete childservice 'AddnCpe'
     *
     * @param modemId  modem used
     * @param child_id in
     * @return model instance
     */
    public AddnCpe deleteAddnCpe(ModemId modemId, String child_id) {
        AddnCpe addnCpe = getModel().find().AddnCpe(modemId, child_id);
        addnCpe.getDefaultOrderData().getParams().clear();
        addnCpe.delete();
        return addnCpe;
    }

    /**
     * delete childservice 'VoipAccess'/'MTA'
     *
     * @param modemId modem used
     * @return model instance, if return null, that means there is no voipAccess.
     */
    public VoipAccess deleteVoipAccess(ModemId modemId) {
        VoipAccess voipAccess = getModel().find().VoipAccess(modemId);
        if (voipAccess != null) {
            voipAccess.getDefaultOrderData().getParams().clear();
            voipAccess.delete();
        }
        return voipAccess;
    }

    /**
     * add childservice 'VoipAccess'
     *
     * @param modemId modem used
     * @param mta_id  in
     * @param mta_mac in
     * @return model instance
     * @throws dk.yousee.smp.order.model.BusinessException when
     * association cannot be established (internal error in model ... should be refactored)
     */
    public VoipAccess addVoipAccess(ModemId modemId, String mta_id, String mta_mac) throws BusinessException {
        VoipAccess voipAccess = getModel().add().VoipAccess(modemId);
        voipAccess.mta_id.setValue(mta_id);                                  //   "12345678903"    ** unique
        voipAccess.mta_mac.setValue(mta_mac);                                //   "33885aa32503"   ** unique
        DialToneAccess dialToneAccess = getModel().find().DialToneAccess(modemId);
        if (dialToneAccess != null && dialToneAccess.dt_has_access.get() == null) {
            dialToneAccess.dt_has_access.add(voipAccess);
        }
        return voipAccess;
    }

    /**
     * add childservice 'StdCpe'
     * THIS IS A SAMPLE !!!!
     *
     * @param modemId       modem used
     * @param hsdAccessData the params data for creating cable modem
     * @return model instance
     * @throws BusinessException 
     */
    public HsdAccess addHsdAccess(ModemId modemId, HsdAccessData hsdAccessData) throws BusinessException {
        HsdAccess ha = getModel().add().HsdAccess(modemId);
        ha.cm_mac.setValue(hsdAccessData.getCm_mac());                         //"134345464578"              ** unique
        ha.wifi_capable.setValue(hsdAccessData.getWifi_capable());                              //"N"
        ha.docsis_3_capable.setValue(hsdAccessData.getDocsis_3_capable());                          //"N"
        ha.gi_address.setValue(hsdAccessData.getGi_address());                        //"10.50.0.1"
        ha.setCmOwnership(modemId);                     //"1024102022"                 ** unique
        ha.equipment_type.setValue(hsdAccessData.getEquipment_type());                         //"emta"
        ha.svc_provider_nm.setValue(hsdAccessData.getSvc_provider_nm());                      //"YouSee"
        ha.cm_technology.setValue(hsdAccessData.getCm_technology());            //"DOCSIS VERSION 1.1"
        ha.max_num_cpe.setValue(hsdAccessData.getMax_num_cpe());                               //"5"
//        ha.service_on_address.add(hsdAccessData.getService_on_address());
//        ha.property_value.setValue(modemId.toString());
//        ha.all_ip_mapping.setValue("12test");
        
        ha.cm_manufacturer.setValue(hsdAccessData.getCm_manufacturer());
        ha.cm_model.setValue(hsdAccessData.getCm_model());
        ha.cm_serial_number.setValue(hsdAccessData.getCm_serial_number());
        
        CommunityWifi cwifi = getModel().find().CommunityWifi(new BusinessPosition(modemId.getId()));
        if (cwifi != null) {
        	ha.community_wifi.add(cwifi);
        }
        
        return ha;
    }

    public static class HsdAccessData {
        private String cm_mac;
        private String wifi_capable;
        private String docsis_3_capable;
        private String gi_address;
        private String equipment_type;
        private String svc_provider_nm;
        private String cm_technology;
        private String max_num_cpe;
        //new bacc read fields for m5
        private String cm_manufacturer;
        private String cm_serial_number;
        private String cm_model;
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

        public String getCm_manufacturer() {
            return cm_manufacturer;
        }

        public void setCm_manufacturer(String cm_manufacturer) {
            this.cm_manufacturer = cm_manufacturer;
        }

        public String getCm_model() {
            return cm_model;
        }

        public void setCm_model(String cm_model) {
            this.cm_model = cm_model;
        }

        public String getCm_serial_number() {
            return cm_serial_number;
        }

        public void setCm_serial_number(String cm_serial_number) {
            this.cm_serial_number = cm_serial_number;
        }
    }


//********************************************* some methods, we don't use currently.    
//    /**
//     * add childservice 'StdCpe'
//     *
//     * @param modemId modem used
//     * @param cpe_mac in
//     * @param cm_mac  in
//     * @return model instance
//     */
//
//    public StdCpe addStdCpe(ModemId modemId, String cpe_mac, String cm_mac) {
//        StdCpe stdCpe = getModel().add().StdCpe(modemId);
//        stdCpe.cpe_mac.setValue(cpe_mac);
//        stdCpe.cm_mac.setValue(cm_mac);
//        return stdCpe;
//    }
//        /**
//     * add childservice 'InetAccess'
//     *
//     * @param modemId             modem used
//     * @param cisco_sm_package_id in
//     * @param rate_codes          in
//     * @param macAddress          in
//     * @return model instance
//     */
//    public InetAccess addInetAccess(ModemId modemId, String cisco_sm_package_id, String rate_codes, String macAddress) {
//        InetAccess inetAccess = getModel().add().InetAccess(modemId);
//        inetAccess.cm_mac.setValue(macAddress);                                    //12 digit
//        inetAccess.cisco_sm_package_id.setValue(cisco_sm_package_id);              //'2'
//        inetAccess.rate_codes.setValue(rate_codes);                                //'HSD01'
//        return inetAccess;
//    }
//        /**
//     * delete association for InetAccess
//     *
//     * @param hsdAccessKey key to InetAccess
//     * @param modemId      modem used
//     * @return model instance
//     * @throws dk.yousee.smp.order.model.BusinessException
//     *          when relation not exists
//     */
//    public InetAccess deleteAssocInternet_access_has_emta_cmForInetAccess(String hsdAccessKey, ModemId modemId) throws BusinessException {
//        InetAccess inetAccess = getModel().alloc().InetAccess(modemId);
//        if (inetAccess.internet_access_has_emta_cm.get() != null) {
//            inetAccess.internet_access_has_emta_cm.delete(hsdAccessKey);
//        }
//        return inetAccess;
//    }
//    /**
//     * add association for InetAccess
//     *
//     * @param hsdAccessKey key to InetAccess
//     * @param modemId      modem used
//     * @return model instance
//     */
//    /**
//     * @deprecated
//     */
//    public InetAccess addAssocInternet_access_has_emta_cmForInetAccess(String hsdAccessKey, ModemId modemId) {
//        InetAccess inetAccess = getModel().alloc().InetAccess(modemId);
//        if (inetAccess.internet_access_has_emta_cm.get() == null) {
//            inetAccess.internet_access_has_emta_cm.add(hsdAccessKey);
//        }
//        return inetAccess;
//    }
    /**
     * Update the customer's WiFi settings such as gw.channel.id, psk, ss_id - See PCR13.
     * Copied from CableBBCase (JLo 2011-10-03).<p>
     * 
     * @param modemId      modem used
     * @param gw_ch_id     gw_ch_id, null means unchanged value and $generate will autogenerate a new value 
     * @param ss_id        ss_id, null means unchanged value and $generate will autogenerate a new value
     * @param psk          psk , null means unchanged value and $generate will autogenerate a new value
     * @return model instance or null if customer does not have wifi service
     */
    public SMPWiFi updateSMPWiFi(ModemId modemId, String gw_ch_id, String psk, String ss_id, String gw_ch_5g) {
        SMPWiFi smpWiFi = getModel().find().SMPWiFi(modemId);
        if (smpWiFi != null) {
            if (gw_ch_id != null) {
                smpWiFi.gw_channel_id.setValue(gw_ch_id);
            }
            if (psk != null) {
                smpWiFi.psk.setValue(psk);
            }
            if (ss_id != null) {
                smpWiFi.ss_id.setValue(ss_id);
            }
            if (gw_ch_5g != null) {
                smpWiFi.gw_channel_id_5g.setValue(gw_ch_5g);
            }
        }
        return smpWiFi;
    }
}

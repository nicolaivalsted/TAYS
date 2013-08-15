package dk.yousee.smp.casemodel.vo.helpers;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import dk.yousee.smp.casemodel.SubscriberModel;
import dk.yousee.smp.casemodel.vo.BusinessPosition;
import dk.yousee.smp.casemodel.vo.ModemId;
import dk.yousee.smp.casemodel.vo.PhoneNumber;
import dk.yousee.smp.casemodel.vo.base.SampSub;
import dk.yousee.smp.casemodel.vo.base.SubAddressSpec;
import dk.yousee.smp.casemodel.vo.base.SubContactSpec;
import dk.yousee.smp.casemodel.vo.cbp.AddnCpe;
import dk.yousee.smp.casemodel.vo.cbp.CableBBService;
import dk.yousee.smp.casemodel.vo.cbp.InetAccess;
import dk.yousee.smp.casemodel.vo.cbp.SMPEmail;
import dk.yousee.smp.casemodel.vo.cbp.SMPStaticIP;
import dk.yousee.smp.casemodel.vo.cbp.SMPWiFi;
import dk.yousee.smp.casemodel.vo.cbp.StdCpe;
import dk.yousee.smp.casemodel.vo.cpee.CpeComposedService;
import dk.yousee.smp.casemodel.vo.cpee.HsdAccess;
import dk.yousee.smp.casemodel.vo.cpee.VoipAccess;
import dk.yousee.smp.casemodel.vo.cvp.CableVoiceService;
import dk.yousee.smp.casemodel.vo.cvp.DialToneAccess;
import dk.yousee.smp.casemodel.vo.cvp.SwitchFeature;
import dk.yousee.smp.casemodel.vo.cvp.VoiceMail;
import dk.yousee.smp.casemodel.vo.cwifi.CommunityWifi;
import dk.yousee.smp.casemodel.vo.cwifi.CommunityWifiService;
import dk.yousee.smp.casemodel.vo.mail.ForeningsMailService;
import dk.yousee.smp.casemodel.vo.mail.Mail;
import dk.yousee.smp.casemodel.vo.mbs.MobileBBService;
import dk.yousee.smp.casemodel.vo.mbs.SMPMobileBroadbandAttributes;
import dk.yousee.smp.casemodel.vo.mbs.SMPMobileBroadbandDEF;
import dk.yousee.smp.casemodel.vo.mbs.SMPSIMCard;
import dk.yousee.smp.casemodel.vo.play.Play;
import dk.yousee.smp.casemodel.vo.play.PlayService;
import dk.yousee.smp.casemodel.vo.tdcmail.TdcMail;
import dk.yousee.smp.casemodel.vo.tdcmail.TdcMailService;
import dk.yousee.smp.order.model.OrderDataType;

/**
 * Created by IntelliJ IDEA.
 * User: aka
 * Date: Oct 15, 2010
 * Time: 8:36:40 PM<br/>
 * Find various methods.
 */
public class Find {
    private static final Logger logger = Logger.getLogger(Find.class);

    private List<BasicUnit> serviceLevelUnit;
    private Key key;

    public Find(SubscriberModel model, List<BasicUnit> serviceLevelUnit) {
        this.serviceLevelUnit = serviceLevelUnit;
        this.key = model.key();
    }

    /**
     * @return the subscribers Contact information
     */
    public SampSub SampSub() {
        return (SampSub) find(SampSub.TYPE);
    }

    /**
     * @return the subscribers Contact information
     */
    public SubContactSpec SubContactSpec() {
        return (SubContactSpec) find(SubContactSpec.TYPE);
    }

    /**
     * @return the subscribes Address, null if not exists
     */
    public SubAddressSpec SubAddressSpec() {
        return (SubAddressSpec) find(SubAddressSpec.TYPE);
    }

    BasicUnit find(OrderDataType type) {
        for (BasicUnit plan : serviceLevelUnit) {
            if (plan.getType().equals(type)) {
                return plan;
            }
        }
        return null;
    }

    BasicUnit find(OrderDataType type, String externalKey) {
        for (BasicUnit plan : serviceLevelUnit) {
            if (plan.getType().equals(type) && plan.getExternalKey().equals(externalKey)) {
                return plan;
            }
        }
        return null;
    }


/** CableBBServices find ***************************************************************************************************/

    /**
     * @return the CableBBServices the subscriber has
     */
    public List<CableBBService> CableBBService() {
        List<CableBBService> res = new ArrayList<CableBBService>();
        for (BasicUnit plan : serviceLevelUnit) {
            if (plan.getType().equals(CableBBService.TYPE)) {
                res.add((CableBBService) plan);
            }
        }
        return res;
    }


    /**
     * First Cable Modem matching the activation code
     *
     * @param modemActivationCode code to search for (case sensitive etc)
     * @return instance if it exists
     */
    public CableBBService firstCableBBService(String modemActivationCode) {
        if (modemActivationCode == null)
            throw new IllegalArgumentException("modemActivationCode is mandatory in query");
        for (BasicUnit plan : serviceLevelUnit) {
            if (plan.getType().equals(CableBBService.TYPE)) {
                CableBBService bb = (CableBBService) plan;
                if (modemActivationCode.equals(bb.getInetAccess().getModemActivationCode())) {
                    return bb;
                }
            }
        }
        return null;
    }


    /**
     * @param externalKey the key composed from modem id
     * @return instance if it exists
     */
    protected CableBBService CableBBService(String externalKey) {
        return (CableBBService) find(CableBBService.TYPE, externalKey);
    }

    /**
     * @param modemId the key composed from modem id
     * @return instance if it exists
     */
    public CableBBService CableBBService(ModemId modemId) {

        List<CableBBService> services=CableBBService();
        for(CableBBService service:services){
            InetAccess access=service.getInetAccess();
            if(access==null){
                logger.warn("Service has no ModemId assigned, "+service);
                return CableBBService(key.CableBBService(modemId));
            } else {
                ModemId one=access.getModemId();
                if(modemId.equals(one))return service;
            }
        }
        return null;
    }

    /**
     * @param modemId to modem
     * @return instance if it exists
     */
    public StdCpe StdCpe(ModemId modemId) {
        CableBBService service=CableBBService(modemId);
        if(service!=null){
            return service.getStdCpe();
        } else {
            return null;
        }
//        return StdCpe(key.CableBBService(modemId));
    }

    /**
     * @param parentKey to the CableBBService
     * @return instance if it exists
     */
    protected StdCpe StdCpe(String parentKey) {
        CableBBService parent = CableBBService(parentKey);
        if (parent == null) return null;
        return parent.getStdCpe();
    }

    /**
     * @param modemId to the modem
     * @return instance if it exists
     */
    public InetAccess InetAccess(ModemId modemId) {
        return InetAccess(key.CableBBService(modemId));
    }

    /**
     * @param parentKey to the CableBBService
     * @return instance if it exists
     */
    protected InetAccess InetAccess(String parentKey) {
        CableBBService parent = CableBBService(parentKey);
        if (parent == null) return null;
        return parent.getInetAccess();
    }

    /**
     * @param parentKey to the CableBBService
     * @return instance list if it exists
     */
    public List<AddnCpe> AddnCpe(String parentKey) {
        CableBBService parent = CableBBService(parentKey);
        if (parent == null) return null;
        return parent.getAddnCpe();
    }

    /**
     * @param parentKey to the CableBBService
     * @param childKey  what is this ???
     * @return instance if it exists
     */
    public AddnCpe AddnCpeAndChildKey(String parentKey, String childKey) {
        CableBBService parent = CableBBService(parentKey);
        if (parent == null) return null;
        for (AddnCpe addnCpe : parent.getAddnCpe()) {
            if (addnCpe.getExternalKey().equalsIgnoreCase(childKey)) {
                return addnCpe;
            }
        }
        return null;
    }

    /**
     * @param modemId  to the modem
     * @param childKey what is this ???
     * @return instance list if it exists
     * @deprecated - it is next to unusable.
     *             Additional CPE's are identificed by different MAC addresses. Therefore the correct behaviour is to
     *             collect all AddnCpe's for a modem and for each of these to find the correct MAC address and run from there.
     */
    public AddnCpe AddnCpe(ModemId modemId, String childKey) {
        return AddnCpeAndChildKey(key.CableBBService(modemId), childKey);
    }

    /**
     * @param modemId to the modem
     * @return instance list if it exists
     */
    public List<AddnCpe> AddnCpe(ModemId modemId) {
        return AddnCpe(key.CableBBService(modemId));
    }

    public AddnCpe theAddnCpe(String parentKey) {
        CableBBService parent = CableBBService(parentKey);
        if (parent == null) return null;
        return parent.getTheAddnCpe();
    }

    /**
     * @param modemId to the modem
     * @return instance list if it exists
     */
    public AddnCpe theAddnCpe(ModemId modemId) {
        return theAddnCpe(key.CableBBService(modemId));
    }

    /**
     * @param modemId to the modem
     * @return instance if it exists
     */
    public SMPEmail SMPEmail(ModemId modemId) {
        CableBBService parent = CableBBService(modemId);
        if (parent == null) return null;
        return parent.getSmpEmail();
    }

    /**
     * @param modemId to the modem
     * @return instance if it exists
     */
    public SMPStaticIP SMPStaticIP(ModemId modemId) {
        CableBBService parent = CableBBService(modemId);
        if (parent == null) return null;
        return parent.getSmpStaticIP();
    }

    /**
     * @param modemId to the modem
     * @return instance if it exists
     */
    public SMPWiFi SMPWiFi(ModemId modemId) {
        CableBBService parent = CableBBService(modemId);
        if (parent == null) return null;
        return parent.getSmpWiFi();
    }

/** CpeComposedService find ***************************************************************************************************/

    /**
     * @return the CpeComposedServices the subscriber has
     */
    public List<CpeComposedService> CpeComposedService() {
        List<CpeComposedService> res = new ArrayList<CpeComposedService>();
        for (BasicUnit plan : serviceLevelUnit) {
            if (plan.getType().equals(CpeComposedService.TYPE)) {
                res.add((CpeComposedService) plan);
            }
        }
        return res;
    }

    /**
     * @param cmOwnership key to the composed service (currently modem id)
     * @return instance if it exists
     */
    public CpeComposedService CpeComposedService(ModemId cmOwnership) {
        List<CpeComposedService> services = CpeComposedService();
        for(CpeComposedService service:services){
            if(cmOwnership.equals(service.getCmOwnership())){
                return service;
            }
        }
        return null;
    }

    /**
     * @param cmOwnership key to the composed service (currently modem id)
     * @return instance if it exists
     */
    public HsdAccess HsdAccess(ModemId cmOwnership) {
        CpeComposedService parent = CpeComposedService(cmOwnership);
        if (parent == null) return null;
        return parent.getHsdAccess();
    }

    /**
     * @param cmOwnership key to the composed service (currently modem id)
     * @return instance if it exists
     */
    public VoipAccess VoipAccess(ModemId cmOwnership) {
        CpeComposedService parent = CpeComposedService(cmOwnership);
        if (parent == null) return null;
        return parent.getVoipAccess();
    }


/** CableVoiceService find ***************************************************************************************************/

    /**
     * @return the CableVoiceService the subscriber has
     */
    public List<CableVoiceService> CableVoiceService() {
        List<CableVoiceService> res = new ArrayList<CableVoiceService>();
        for (BasicUnit plan : serviceLevelUnit) {
            if (plan.getType().equals(CableVoiceService.TYPE)) {
                res.add((CableVoiceService) plan);
            }
        }
        return res;
    }

    /**
     * @param phoneNumber to the modem
     * @return instance if it exists
     */
    public CableVoiceService CableVoiceService(PhoneNumber phoneNumber) {
        List<CableVoiceService> services = CableVoiceService();
        for(CableVoiceService service:services){
            if(phoneNumber.equals(service.getPhoneNumber())){
                return service;
            }
        }
        return null;
    }

    /**
     * @param modemId to the modem
     * @return instance if it exists
     * TODO: use telephone number to identify
     */
    public CableVoiceService CableVoiceService(ModemId modemId) {
		List<CableVoiceService> services = CableVoiceService();
		for (CableVoiceService service : services) {
			if (modemId.equals(service.getModemId())) {
				return service;
			}
		}
    	
        return null;
    }

    /**
     * @param modemId to the modem
     * @return instance if it exists
     * TODO: use telephone number to identify
     */
    public CableVoiceService CableVoiceService(BusinessPosition position) {
		List<CableVoiceService> services = CableVoiceService();
		for (CableVoiceService service : services) {
			if (position.equals(service.getPosition())) {
				return service;
			}
		}
    	
        return null;
    }

    /**
     * @param BusinessPosition to the modem
     * @return instance if it exists
     */
    public DialToneAccess DialToneAccess(BusinessPosition position) {
        CableVoiceService parent = CableVoiceService(position);
        if (parent == null) return null;
        return parent.getDialToneAccess();
    }

    /**
     * @param modemId to the modem
     * @return instance if it exists
     */
    public DialToneAccess DialToneAccess(ModemId modemId) {
        CableVoiceService parent = CableVoiceService(modemId);
        if (parent == null) return null;
        return parent.getDialToneAccess();
    }

    
    /**
     * @param modemId to the modem
     * @return instance list if it exists
     */
    public List<SwitchFeature> SwitchFeature(BusinessPosition position) {
        CableVoiceService parent = CableVoiceService(position);
        if (parent == null) return null;
        return parent.getSwitchFeatureList();
    }

    /**
     * @param modemId to the modem
     * @return instance if it exists
     */
    public VoiceMail VoiceMail(BusinessPosition position) {
        CableVoiceService parent = CableVoiceService(position);
        if (parent == null) return null;
        return parent.getVoiceMail();
    }

/** MobileBBService find ***************************************************************************************************/

    /**
     * @return the MobileBBService the subscriber has
     */
    public List<MobileBBService> MobileBBService() {
        List<MobileBBService> res = new ArrayList<MobileBBService>();
        for (BasicUnit plan : serviceLevelUnit) {
            if (plan.getType().equals(MobileBBService.TYPE)) {
                res.add((MobileBBService) plan);
            }
        }
        return res;
    }

    /**
     * @param modemId to the modem
     * @return instance if it exists
     */
    public MobileBBService MobileBBService(ModemId modemId) {
        return (MobileBBService) find(MobileBBService.TYPE, key.MobileBBService(modemId));
    }

    /**
     * @param modemId to the modem
     * @return instance list if it exists
     */
    public List<SMPMobileBroadbandAttributes> SMPMobileBroadbandAttributes(ModemId modemId) {
        MobileBBService parent = MobileBBService(modemId);
        if (parent == null) return null;
        return parent.getSmpMobileBroadbandAttributes();
    }

//    /**
//     * @param modemId to the modem
//     * @param childKey what is this ???
//     * @return instance if it exists
//     */
//    public SMPMobileBroadbandAttributes SMPMobileBroadbandAttributesAndChildKey(ModemId modemId, String childKey) {
//        return SMPMobileBroadbandAttributesAndChildKey(key.MobileBBService(modemId) , childKey);
//    }


//    /**
//     * @param parentKey to the MobileBBService
//     * @param childKey what is this ???
//     * @return instance if it exists
//     */
//    public SMPMobileBroadbandAttributes SMPMobileBroadbandAttributesAndChildKey(String parentKey, String childKey) {
//        MobileBBService parent = MobileBBService(parentKey);
//        if (parent == null) return null;
//        for (SMPMobileBroadbandAttributes smpMobileBroadbandAttributes : parent.getSmpMobileBroadbandAttributes()) {
//            if (smpMobileBroadbandAttributes.getExternalKey().equalsIgnoreCase(childKey)) {
//                return smpMobileBroadbandAttributes;
//            }
//        }
//        return null;
//    }

    /**
     * @param modemId to the modem
     * @return instance if it exists
     */
    public SMPMobileBroadbandDEF SMPMobileBroadbandDEF(ModemId modemId) {
        MobileBBService parent = MobileBBService(modemId);
        if (parent == null) return null;
        return parent.getSmpMobileBroadbandDEF();
    }


    public SMPSIMCard SMPSIMCard(ModemId modemId) {
        return (SMPSIMCard) find(SMPSIMCard.TYPE, key.SMPSIMCard(modemId));
    }

    public List<SMPSIMCard> smpSimCards() {
        List<SMPSIMCard> res = new ArrayList<SMPSIMCard>();
        for (BasicUnit plan : serviceLevelUnit) {
            if (plan.getType().equals(SMPSIMCard.TYPE)) {
                res.add((SMPSIMCard) plan);
            }
        }
        return res;
    }

//    /**
//     * find service level unit
//     * @param externalKey key to unit (it is a global unique key)
//     * @return the first unit matching this key. Might be null. Cardinality 0:1
//     */
//
//    public BasicUnit findServiceLevelUnitByExternalID(String externalKey) {
//        for (BasicUnit unit : model.getServiceLevelUnit()) {
//            if (unit.getExternalKey().equalsIgnoreCase(externalKey)) {
//                return unit;
//            }
//        }
//        return null;
//    }

    // ======= forenings mail =======

    /**
     * @return the MobileBBService the subscriber has
     */
    public List<ForeningsMailService> ForeningsMailService() {
        List<ForeningsMailService> res = new ArrayList<ForeningsMailService>();
        for (BasicUnit plan : serviceLevelUnit) {
            if (plan.getType().equals(ForeningsMailService.TYPE)) {
                res.add((ForeningsMailService) plan);
            }
        }
        return res;
    }

    /**
     * @param position identifier for specific instance of the service plan
     * @return instance if it exists
     */
    public ForeningsMailService ForeningsMailService(BusinessPosition position) {
        List<ForeningsMailService> plans = ForeningsMailService();
        for (ForeningsMailService plan : plans) {
            if(position.equals(plan.getPosition())) {
                return plan;
            }
        }
        return null;
    }

    /**
     * @param position to service
     * @return new instance
     */
    public Mail ForeningsMail(BusinessPosition position) {
        ForeningsMailService parent=ForeningsMailService(position);
        return parent==null?null:parent.getMail();
    }

    // ======= play =======

    /**
     * @return the MobileBBService the subscriber has
     */
    public List<PlayService> PlayService() {
        List<PlayService> res = new ArrayList<PlayService>();
        for (BasicUnit plan : serviceLevelUnit) {
            if (plan.getType().equals(PlayService.TYPE)) {
                res.add((PlayService) plan);
            }
        }
        return res;
    }

    /**
     * @param position identifier for specific instance of the service plan
     * @return instance if it exists
     */
    public PlayService PlayService(BusinessPosition position) {
        List<PlayService> plans = PlayService();
        for (PlayService plan : plans) {
            if(position.equals(plan.getPosition())) {
                return plan;
            }
        }
        return null;
    }
    /**
     * @param position to service
     * @return new instance
     */
    public Play Play(BusinessPosition position) {
        PlayService parent=PlayService(position);
        return parent==null?null:parent.getPlay();
    }

    // ======= CommunityWifi =======

    /**
     * @return the MobileBBService the subscriber has
     */
    public List<CommunityWifiService> CommunityWifiService() {
        List<CommunityWifiService> res = new ArrayList<CommunityWifiService>();
        for (BasicUnit plan : serviceLevelUnit) {
            if (plan.getType().equals(CommunityWifiService.TYPE)) {
                res.add((CommunityWifiService) plan);
            }
        }
        return res;
    }

    /**
     * @param position identifier for specific instance of the service plan
     * @return instance if it exists
     */
    public CommunityWifiService CommunityWifiService(BusinessPosition position) {
        List<CommunityWifiService> plans = CommunityWifiService();
        for (CommunityWifiService plan : plans) {
            if(position.equals(plan.getPosition())) {
                return plan;
            }
        }
        return null;
    }
    /**
     * @param position to service
     * @return new instance
     */
    public CommunityWifi CommunityWifi(BusinessPosition position) {
        CommunityWifiService parent=CommunityWifiService(position);
        return parent==null?null:parent.getCommunityWifi();
    }

    // ======= TdcMail =======

    
    public List<TdcMailService> tdcMailServices() {
        List<TdcMailService> res = new ArrayList<TdcMailService>();
        for(BasicUnit plan : serviceLevelUnit) {
            if(plan.getType().equals(TdcMailService.TYPE)) {
                res.add((TdcMailService) plan);
            }
        }
        
        return res;
    }
    
    public TdcMailService tdcMailService(BusinessPosition businessPosition) {
        List<TdcMailService> plans = tdcMailServices();
        for(TdcMailService plan : plans) {
            if(businessPosition.equals(plan.getPosition())) {
                return plan;
            }
        }        
        return null;
    }
    
    public TdcMail tdcMail(BusinessPosition businessPosition) {
        TdcMailService parent = tdcMailService(businessPosition);
        return parent!=null?parent.getTdcMail():null;
    }
}

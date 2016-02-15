package dk.yousee.smp.casemodel.vo.helpers;

import dk.yousee.smp.casemodel.SubscriberModel;
import dk.yousee.smp.casemodel.vo.BusinessPosition;
import dk.yousee.smp.casemodel.vo.ModemId;
import dk.yousee.smp.casemodel.vo.cbp.BSA;
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
import dk.yousee.smp.casemodel.vo.cvp.VoiceMail;
import dk.yousee.smp.casemodel.vo.mail.Mail;
import dk.yousee.smp.casemodel.vo.mbs.MobileBBService;
import dk.yousee.smp.casemodel.vo.mbs.SMPMobileBroadbandDEF;
import dk.yousee.smp.casemodel.vo.mbs.SMPSIMCard;
import dk.yousee.smp.casemodel.vo.mofibo.Mofibo;
import dk.yousee.smp.casemodel.vo.play.Play;
import dk.yousee.smp.casemodel.vo.sikpakke.Sikkerhedspakke;
import dk.yousee.smp.casemodel.vo.tdcmail.TdcMail;
import dk.yousee.smp.casemodel.vo.tdcmail.TdcMailResource;
import dk.yousee.smp.casemodel.vo.tdcmail.TdcMailService;

/**
 * Created by IntelliJ IDEA.
 * User: aka
 * Date: Oct 16, 2010
 * Time: 11:47:25 AM<br/>
 * Mastering allocation of Service Plans especially from use-cases
 */
public class Alloc {
    private Find find;
    private Add add;

    public Alloc(SubscriberModel model) {
        find = model.find();
        add = model.add();
    }

//    /**
//     * @return instance either an existing plan or a new plan ready for fill in data
//     */
//    public SubContactSpec SubContactSpec() {
//        SubContactSpec res = find.SubContactSpec();
//        if (res == null) {
//            res = add.SubContactSpec();
//        }
//        return res;
//    }
//
//    /**
//     * @return instance either an existing plan or a new plan ready for fill in data
//     */
//    public SubAddressSpec SubAddressSpec() {
//        SubAddressSpec res = find.SubAddressSpec();
//        if (res == null) {
//            res = add.SubAddressSpec();
//        }
//        return res;
//    }

/** CableBBService Alloc ***************************************************************************************************/

    /**
     * @param modemId to modem
     * @return instance either an existing plan or a new plan ready for fill in data
     */
    public CableBBService CableBBService(ModemId modemId) {
        CableBBService res = find.CableBBService(modemId);
        if (res == null) {
            res = add.CableBBService(modemId);
        }
        return res;

//        return CableBBService(key.CableBBService(modemId));
    }

//    /**
//     * @param externalKey the key composed from modem id
//     * @return instance either an existing plan or a new plan ready for fill in data
//     */
//    public CableBBService CableBBService(String externalKey) {
//        CableBBService res = find.CableBBService(externalKey);
//        if (res == null) {
//            res = add.CableBBService(externalKey);
//        }
//        return res;
//    }

    /**
     * @param modemId to modem
     * @return instance either an existing child-service or a new child-service ready for fill in data
     */
    public InetAccess InetAccess(ModemId modemId) {
        InetAccess res = find.InetAccess(modemId);
        if (res == null) {
            res = add.InetAccess(modemId);
        }
        return res;
    }

    /**
     * @param modemId to modem
     * @return instance either an existing child-service or a new child-service ready for fill in data
     */
    public StdCpe StdCpe(ModemId modemId) {
        StdCpe res = find.StdCpe(modemId);
        if (res == null) {
            res = add.StdCpe(modemId);
        }
        return res;
    }

//    /**
//     * @param modemId to modem
//     * @param childKey ??? what is that ???
//     * @return instance either an existing child-service or a new child-service ready for fill in data
//     */
//    public AddnCpe AddnCpe(ModemId modemId, String childKey) {
//        AddnCpe res = find.AddnCpe(modemId, childKey);
//        if (res == null) {
//            return add.AddnCpe(modemId);
//        }
//        return res;
//    }

    /**
     * @param modemId to modem
     * @return instance either an existing child-service or a new child-service ready for fill in data
     */
    public SMPEmail SMPEmail(ModemId modemId) {
        SMPEmail res = find.SMPEmail(modemId);
        if (res == null) {
            return add.SMPEmail(modemId);
        }
        return res;
    }

    /**
     * @param modemId to modem
     * @return instance either an existing child-service or a new child-service ready for fill in data
     */
    public SMPStaticIP SMPStaticIP(ModemId modemId) {
        SMPStaticIP res = find.SMPStaticIP(modemId);
        if (res == null) {
            return add.SMPStaticIP(modemId);
        }
        return res;
    }


    /**
     * @param modemId to modem
     * @return instance either an existing child-service or a new child-service ready for fill in data
     */
    public SMPWiFi SMPWiFi(ModemId modemId) {
        SMPWiFi res = find.SMPWiFi(modemId);
        if (res == null) {
            return add.SMPWiFi(modemId);
        }
        return res;
    }

    /**
     * @param modemId to modem
     * @return instance either an existing child-service or a new child-service ready for fill in data
     */
    public BSA BSA(ModemId modemId) {
        BSA res = find.BSA(modemId);
        if (res == null) {
            return add.BSA(modemId);
        }
        return res;
    }


/** CpeComposedService Alloc ***************************************************************************************************/

    /**
     * @param cmOwnership to modem
     * @return instance either an existing plan or a new plan ready for fill in data
     */
    public CpeComposedService CpeComposedService(ModemId cmOwnership) {
        CpeComposedService res = find.CpeComposedService(cmOwnership);
        if (res == null) {
            res = add.CpeComposedService(cmOwnership);
        }
        return res;
    }


    /**
     * @param modemId to modem
     * @return instance either an existing child-service or a new child-service ready for fill in data
     */
    public HsdAccess HsdAccess(ModemId modemId) {
        HsdAccess res = find.HsdAccess(modemId);
        if (res == null) {
            res = add.HsdAccess(modemId);
        } else
            res.getDefaultOrderData();
        return res;
    }

    /**
     * @param modemId to modem
     * @return instance either an existing child-service or a new child-service ready for fill in data
     */
    public VoipAccess VoipAccess(ModemId modemId) {
        VoipAccess res = find.VoipAccess(modemId);
        if (res == null) {
            res = add.VoipAccess(modemId);
        }
        return res;
    }

/** CableVoiceService Alloc ***************************************************************************************************/

    /**
     * @param modemId to modem
     * @return instance either an existing plan or a new plan ready for fill in data
     */
    public CableVoiceService CableVoiceService() {
        return add.CableVoiceService();
    }

    /**
     * @param modemId to modem
     * @return instance either an existing plan or a new plan ready for fill in data
     */
    public CableVoiceService CableVoiceService(BusinessPosition position) {
    	CableVoiceService res = find.CableVoiceService(position);

    	if (res == null) {
            res = add.CableVoiceService();
        }
        return res;
    }

    
    /**
     * @param modemId to modem
     * @return instance either an existing child-service or a new child-service ready for fill in data
     */
    public DialToneAccess DialToneAccess(BusinessPosition position) {
        DialToneAccess res = add.DialToneAccess();
        res.setPosition(position);
        return res;
    }

    /**
     * @param modemId to modem
     * @return instance either an existing child-service or a new child-service ready for fill in data
     */
    public VoiceMail VoiceMail(BusinessPosition position, CableVoiceService parent) {
    	VoiceMail res = parent.getVoiceMail();
        if (res == null) {
            res = add.VoiceMail(position);
        }
        return res;
    }

/** MobileBBService Alloc ***************************************************************************************************/

    /**
     * @param modemId to modem
     * @return instance either an existing plan or a new plan ready for fill in data
     */
    public MobileBBService MobileBBService(ModemId modemId) {
        MobileBBService res = find.MobileBBService(modemId);
        if (res == null) {
            res = add.MobileBBService(modemId);
        }
        return res;
    }

//    /**
//     * @param modemId to modem
//     * @param childKey ?? what is that ??
//     * @return instance either an existing child-service or a new child-service ready for fill in data
//     */
//    public SMPMobileBroadbandAttributes SMPMobileBroadbandAttributes(ModemId modemId, String childKey) {
//        SMPMobileBroadbandAttributes res = find.SMPMobileBroadbandAttributesAndChildKey(modemId, childKey);
//        if (res == null) {
//            res = add.SMPMobileBroadbandAttributes(modemId);
//        }
//        return res;
//    }

    /**
     * @param modemId to modem
     * @return instance either an existing child-service or a new child-service ready for fill in data
     */
    public SMPMobileBroadbandDEF SMPMobileBroadbandDEF(ModemId modemId) {
//        return SMPMobileBroadbandDEF(key.MobileBBService(modemId));
        SMPMobileBroadbandDEF res = find.SMPMobileBroadbandDEF(modemId);
        if (res == null) {
            res = add.SMPMobileBroadbandDEF(modemId);
        }
        return res;
    }

    /**
     * @param modemId to modem
     * @return instance either an existing sim card or a new sim card ready for fill in data
     */
    public SMPSIMCard SMPSIMCard(ModemId modemId){
        SMPSIMCard res = find.SMPSIMCard(modemId);
        if (res == null) {
            res = add.SMPSIMCard(modemId);
        }
        return res;
    }
/** ForeningsMailService Alloc ***************************************************************************************************/

    /**
     * @param position to service
     * @return new instance
     */
    public Mail ForeningsMail(BusinessPosition position) {
        Mail sub=find.ForeningsMail(position);
        return sub==null?add.ForeningsMail(position):sub;
    }

/** ForeningsMailService Alloc ***************************************************************************************************/

    /**
     * @param position to service
     * @return new instance
     */
    public Play Play(BusinessPosition position) {
        Play sub=find.Play(position);
        return sub==null?add.Play(position):sub;
    }

    /**
     * @param position to service
     * @return new instance
     */
    public Sikkerhedspakke Sikkerhedspakke(BusinessPosition position) {
        Sikkerhedspakke sub=find.Sikkerhedspakke(position);
        return sub==null?add.Sikkerhedspakke(position):sub;
    }


    public TdcMail tdcMail(BusinessPosition businessPosition) {
        TdcMail sub = find.tdcMail(businessPosition);
        return sub==null?add.tdcMail(businessPosition):sub;
    }
    
    public TdcMailResource tdcMailResource(TdcMailService service) {
       return service.getTdcMailResource()==null ? add.tdcMailResource(service) : service.getTdcMailResource();
    }
    
    /**
     * @param position to service
     * @return new instance
     */
    public Mofibo Mofibo(BusinessPosition position) {
        Mofibo sub=find.mofibo(position);
        return sub==null?add.Mofibo(position):sub;
    }
}

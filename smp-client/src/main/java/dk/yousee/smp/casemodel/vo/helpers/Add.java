package dk.yousee.smp.casemodel.vo.helpers;

import java.util.UUID;

import dk.yousee.smp.casemodel.SubscriberModel;
import dk.yousee.smp.casemodel.vo.BusinessPosition;
import dk.yousee.smp.casemodel.vo.ModemId;
import dk.yousee.smp.casemodel.vo.base.SubAddressSpec;
import dk.yousee.smp.casemodel.vo.base.SubContactSpec;
import dk.yousee.smp.casemodel.vo.base.SubSpec;
import dk.yousee.smp.casemodel.vo.cbp.AddnCpe;
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
import dk.yousee.smp.casemodel.vo.cvp.SwitchFeature;
import dk.yousee.smp.casemodel.vo.cvp.VoiceMail;
import dk.yousee.smp.casemodel.vo.mail.Mail;
import dk.yousee.smp.casemodel.vo.mbs.MobileBBService;
import dk.yousee.smp.casemodel.vo.mbs.SMPMobileBroadbandAttributes;
import dk.yousee.smp.casemodel.vo.mbs.SMPMobileBroadbandDEF;
import dk.yousee.smp.casemodel.vo.mbs.SMPSIMCard;
import dk.yousee.smp.casemodel.vo.mofibo.Mofibo;
import dk.yousee.smp.casemodel.vo.tdcmail.TdcMail;
import dk.yousee.smp.casemodel.vo.tdcmail.TdcMailResource;
import dk.yousee.smp.casemodel.vo.tdcmail.TdcMailService;

/**
 * Created by IntelliJ IDEA.
 * User: aka
 * Date: Oct 16, 2010
 * Time: 12:03:36 PM<br/>
 * Handle creation of service plans
 */
public class Add {
    private SubscriberModel model;
    private Key key;

    public Add(SubscriberModel model) {
        this.model = model;
        this.key=model.key();
    }

    public SubSpec SubSpec(String externalKey){
        SubSpec res = new SubSpec(model, externalKey);
        if (res.getEntity() == null) {
            res.getDefaultOrderData();
        }
        return res;
    }


    /**
     * @return the subscribers Contact information
     */
    public SubContactSpec SubContactSpec() {
        SubContactSpec res = new SubContactSpec(model);
        if (res.getEntity() == null) {
            res.getDefaultOrderData();
        }
        return res;
    }

    /**
     * @return the subscribes Address
     */
    public SubAddressSpec SubAddressSpec() {
        SubAddressSpec res = new SubAddressSpec(model);
        if (res.getEntity() == null) {
            res.getDefaultOrderData();
        }
        return res;
    }

/** CableBBServices add ***************************************************************************************************/


    /**
     * @param modemId the key composed from modem id
     * @return new instance
     */
    public CableBBService CableBBService(ModemId modemId) {
        CableBBService res = new CableBBService(model, modemId);
        if (res.getEntity() == null) {
            res.getDefaultOrderData();
        }
        return res;
    }

    /**
     * @param modemId to modem
     * @return new instance
     */
    public StdCpe StdCpe(ModemId modemId) {
        CableBBService parent = model.alloc().CableBBService(modemId);
        StdCpe res = new StdCpe(model, key.generateUUID(), parent);
        if (res.getEntity() == null) {
            res.getDefaultOrderData();
        }
        return res;
    }

    /**
     * @param modemId to the modem
     * @return new instance
     */
    public InetAccess InetAccess(ModemId modemId) {
//        return InetAccess(key.CableBBService(modemId));
        CableBBService parent = model.alloc().CableBBService(modemId);
        InetAccess res = new InetAccess(model, key.generateUUID(), parent);
        if (res.getEntity() == null) {
            res.getDefaultOrderData();
        }
        return res;
    }

    /**
     * @param modemId to the modem
     * @return new instance
     */
    public AddnCpe AddnCpe(ModemId modemId) {
//        return AddnCpe(key.CableBBService(modemId));
        CableBBService parent = model.alloc().CableBBService(modemId);
        AddnCpe res = new AddnCpe(model, key.generateUUID(), parent);
        if (res.getEntity() == null) {
            res.getDefaultOrderData();
        }
        return res;
    }


    /**
     * @param modemId to the modem
     * @return new instance
     */
    public SMPEmail SMPEmail(ModemId modemId) {
        CableBBService parent = model.alloc().CableBBService(modemId);
        SMPEmail res = new SMPEmail(model, key.generateUUID(), parent);
        if (res.getEntity() == null) {
            res.getDefaultOrderData();
        }
        return res;
    }

    /**
     * @param modemId to the modem
     * @return new instance
     */
    public SMPStaticIP SMPStaticIP(ModemId modemId) {
        CableBBService parent = model.alloc().CableBBService(modemId);
        SMPStaticIP res = new SMPStaticIP(model, key.generateUUID(), parent);
        if (res.getEntity() == null) {
            res.getDefaultOrderData();
        }
        return res;
    }

    /**
     * @param modemId to the modem
     * @return new instance
     */
    public SMPWiFi SMPWiFi(ModemId modemId) {
        CableBBService parent = model.alloc().CableBBService(modemId);
        SMPWiFi res = new SMPWiFi(model, key.generateUUID(), parent);
        if (res.getEntity() == null) {
            res.getDefaultOrderData();
        }
        return res;
    }

    /**
     * @param modemId to the modem
     * @return new instance
     */
    public BSA BSA(ModemId modemId) {
        CableBBService parent = model.alloc().CableBBService(modemId);
        BSA res = new BSA(model, key.generateUUID(), parent);
        if (res.getEntity() == null) {
            res.getDefaultOrderData();
        }
        return res;
    }

/** CpeComposedService add ***************************************************************************************************/

    /**
     * @param cmOwnership to the modem
     * @return new instance
     */
    public CpeComposedService CpeComposedService(ModemId cmOwnership) {
        CpeComposedService res = new CpeComposedService(model, cmOwnership);
        if (res.getEntity() == null) {
            res.getDefaultOrderData();
        }
        return res;
    }

    /**
     * @param modemId to modem
     * @return new instance
     */
    public HsdAccess HsdAccess(ModemId modemId) {
        CpeComposedService parent = model.alloc().CpeComposedService(modemId);
        HsdAccess res = new HsdAccess(model, key.generateUUID(), parent);
        if (res.getEntity() == null) {
            res.getDefaultOrderData();
        }
        return res;
    }

    /**
     * @param modemId to the modem
     * @return new instance
     */
    public VoipAccess VoipAccess(ModemId modemId) {
        CpeComposedService parent = model.alloc().CpeComposedService(modemId);
        VoipAccess res = new VoipAccess(model, key.generateUUID(), parent);
        if (res.getEntity() == null) {
            res.getDefaultOrderData();
        }
        return res;
    }


/** CableVoiceService add ***************************************************************************************************/

    /**
     * @param modemId to the modem
     * @return new instance
     */
    public CableVoiceService CableVoiceService() {
        CableVoiceService res = new CableVoiceService(model, key.CableVoiceService(UUID.randomUUID().toString()));
        if (res.getEntity() == null) {
            res.getDefaultOrderData();
        }
        return res;
    }

    /**
     * @param modemId to the modem
     * @return new instance
     */
    public DialToneAccess DialToneAccess() {
        CableVoiceService parent = model.alloc().CableVoiceService();
        DialToneAccess res = new DialToneAccess(model, key.generateUUID(), parent);
        if (res.getEntity() == null) {
            res.getDefaultOrderData();
        }
        return res;
    }

    /**
     * @param modemId to the modem
     * @return new instance
     */
    public SwitchFeature SwitchFeature(BusinessPosition position, CableVoiceService parent) {
        SwitchFeature res = new SwitchFeature(model, key.generateUUID(), parent);
        if (res.getEntity() == null) {
            res.getDefaultOrderData();
        }
        return res;
    }

    /**
     * @param modemId to the modem
     * @return new instance
     */
    public VoiceMail VoiceMail(BusinessPosition position) {
        CableVoiceService parent = model.alloc().CableVoiceService(position);
        VoiceMail res = new VoiceMail(model, key.generateUUID(), parent);
        if (res.getEntity() == null) {
            res.getDefaultOrderData();
        }
        return res;
    }


/** MobileBBService add ***************************************************************************************************/

    /**
     * @param modemId to the modem
     * @return new instance
     */
    public MobileBBService MobileBBService(ModemId modemId) {
        return MobileBBService(key.MobileBBService(modemId));
    }

    /**
     * @param externalKey the key
     * @return new instance
     */
    public MobileBBService MobileBBService(String externalKey) {
        MobileBBService res = new MobileBBService(model, externalKey);
        if (res.getEntity() == null) {
            res.getDefaultOrderData();
        }
        return res;
    }

    /**
     * @param modemId to the modem (key to parent)
     * @return new instance
     */
    public SMPMobileBroadbandAttributes SMPMobileBroadbandAttributes(ModemId modemId) {
        MobileBBService parent = model.alloc().MobileBBService(modemId);
        SMPMobileBroadbandAttributes res = new SMPMobileBroadbandAttributes(model, key.generateUUID(), parent);
        if (res.getEntity() == null) {
            res.getDefaultOrderData();
        }
        return res;
    }

    /**
     * @param modemId to the modem
     * @return new instance
     */
    public SMPMobileBroadbandDEF SMPMobileBroadbandDEF(ModemId modemId) {
//        return SMPMobileBroadbandDEF(key.MobileBBService(modemId));
        MobileBBService parent = model.alloc().MobileBBService(modemId);
        SMPMobileBroadbandDEF res = new SMPMobileBroadbandDEF(model, key.generateUUID(), parent);
        if (res.getEntity() == null) {
            res.getDefaultOrderData();
        }
        return res;
    }

    /**
     * @param modemId to the modem
     * @return new instance
     */
    public SMPSIMCard SMPSIMCard(ModemId modemId) {
        return new SMPSIMCard(model,key.SMPSIMCard(modemId));
    }

/** ForeningsMailService add ***************************************************************************************************/
    /**
     * @param position to service
     * @return new instance
     */
    public Mail ForeningsMail(BusinessPosition position) {
        return new Mail(model,position);
    }

    public TdcMail tdcMail(BusinessPosition businessPosition) {
        return new TdcMail(model, businessPosition);
    }
    
    public TdcMailResource tdcMailResource(TdcMailService tdcMailService) {
        return new TdcMailResource(model, model.key().generateUUID(), tdcMailService);
    }
    
     public Mofibo Mofibo(BusinessPosition position) {
        return new Mofibo(model,position);
    }
}

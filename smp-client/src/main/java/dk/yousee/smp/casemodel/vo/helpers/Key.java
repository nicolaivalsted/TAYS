package dk.yousee.smp.casemodel.vo.helpers;

import dk.yousee.smp.casemodel.SubscriberModel;
import dk.yousee.smp.casemodel.vo.BusinessPosition;
import dk.yousee.smp.casemodel.vo.ModemId;
import dk.yousee.smp.order.model.Acct;


/**
 * Created by IntelliJ IDEA.
 * User: aka
 * Date: Oct 15, 2010
 * Time: 8:36:40 PM<br/>
 * Generate external keys to service plans
 * TAYS External Keys name space registry.doc      4.	Service definition
 */
public class Key {
    private SubscriberModel model;

    public Key(SubscriberModel model) {
        this.model = model;
    }

    public String getProvider() {
        if (model.getProvider().getProvider() != null) {
            return model.getProvider().getProvider();
        } else return "";
    }

//    /**
//     * @return the subscribers Contact information external key
//     */
//    public String SubContactSpec() {
//        return SubContactSpec.EXTERNAL_KEY;
//    }
//
//    /**
//     * @return the subscribes Address external key
//     */
//    public String SubAddressSpec() {
//        return SubAddressSpec.EXTERNAL_KEY;
//    }

    /**
     * @param modemId to modem
     * @return external Key
     */
    public String CableBBService(ModemId modemId) {            //"cable_broadband_"    cbb_
        return getProvider() + ":cbb_" + modemId;
    }

    
     /**
     * @param cmOwnership to modem
     * @return external Key
     */
    public String CpeComposedService(ModemId cmOwnership) {    //"cpe_composed_"       cpe_
        return getProvider() + ":cpe_" + cmOwnership;
    }

    /**
     * @param modemId to modem
     * @return external Key
     */
    public String CableVoiceService(String uuid) {         //                      voice_
        return getProvider() + ":voice_" + uuid;
    }

    /**
     * @param modemId to modem
     * @return external Key
     */
    public String MobileBBService(ModemId modemId) {          //"mobile_broadband"      mbb_
        return getProvider() + ":mbb_" + modemId;
    }

    /**
     * @param modemId to modem
     * @return external Key
     */
    public String SMPSIMCard(ModemId modemId) {
        return getProvider() + ":sim_" + modemId;
    }

    /**
     * @param acct - customer 9 digit number
     * @return external Key
     */
    public String SubscriberExternalKey(Acct acct) {         
        return getProvider() + ":user_" + acct;
    }


    /**
     * <p>
     * Get a unique key to become key in sigma at various places
     * </p>
     * <p>
     * When we introduced UUID it was decided not to set "YouSee:" as prefix.<br/>
     * This was decided by Jacob 2010.10.25.
     * </p>
     * @return generated Key
     */
    public String generateUUID(){
          return UUIDGenerater.generateKey();
        //return getProvider() + ":" +UUIDGenerater.generateKey();
    }

}

package dk.yousee.smp.casemodel.vo.mbs;

import java.util.ArrayList;
import java.util.List;

import dk.yousee.smp.casemodel.SubscriberModel;
import dk.yousee.smp.casemodel.vo.ModemId;
import dk.yousee.smp.casemodel.vo.helpers.BasicUnit;
import dk.yousee.smp.order.model.NickName;
import dk.yousee.smp.order.model.OrderDataLevel;
import dk.yousee.smp.order.model.OrderDataType;
import dk.yousee.smp.order.model.ServicePrefix;

/**
 * Created by IntelliJ IDEA.
 * User: m14857
 * Date: Oct 21, 2010
 * Time: 2:18:37 PM
 * Data structure reference to YouSee Data Migration Requirements: 5.7	Mobile Broadband Services Definition
 */
public class MobileBBService extends BasicUnit {
    public static OrderDataLevel LEVEL = OrderDataLevel.SERVICE;
    public static OrderDataType TYPE = new OrderDataType(ServicePrefix.SubSvcSpec,"mobile_broadband");
    public static NickName NAME = new NickName("mobb");


    public MobileBBService(SubscriberModel model, String externalKey) {
        super(model, externalKey, TYPE, LEVEL, NAME,null);
        model.getServiceLevelUnit().add(this);
    }

    //children

    private SMPMobileBroadbandDEF smpMobileBroadbandDEF;
    private List<SMPMobileBroadbandAttributes> smpMobileBroadbandAttributes = new ArrayList<SMPMobileBroadbandAttributes>();

    /**
     * Cardinality 0:1
     * @return null or instance
     */
    public SMPMobileBroadbandDEF getSmpMobileBroadbandDEF() {
        return smpMobileBroadbandDEF;
    }

    public void setSmpMobileBroadbandDEF(SMPMobileBroadbandDEF smpMobileBroadbandDEF) {
        this.smpMobileBroadbandDEF = smpMobileBroadbandDEF;
    }

    public List<SMPMobileBroadbandAttributes> getSmpMobileBroadbandAttributes() {
        return smpMobileBroadbandAttributes;
    }

    /**
     * The model around SMPMobileBroadbandAttributes specify 1:n<br/>
     * All known code cannot pass cardinality 1, there seems no way to specify next element and what it should mean.<br/>
     * To deal with this "special" situation we have made this method.
     * @return the first element if it exists or null.
     */
    public SMPMobileBroadbandAttributes first() {
        SMPMobileBroadbandAttributes res=null;
        if(!smpMobileBroadbandAttributes.isEmpty()){
            res=smpMobileBroadbandAttributes.get(0);
        }
        return res;
    }

    public ModemId getModemId() {
        return ModemId.extract(getExternalKey());
    }
}

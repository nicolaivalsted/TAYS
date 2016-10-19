package dk.yousee.smp.casemodel.vo.cbp;

import dk.yousee.smp.casemodel.SubscriberModel;
import dk.yousee.smp.casemodel.vo.BusinessPosition;
import dk.yousee.smp.casemodel.vo.ModemId;
import dk.yousee.smp.casemodel.vo.helpers.BasicUnit;
import dk.yousee.smp.order.model.NickName;
import dk.yousee.smp.order.model.OrderDataLevel;
import dk.yousee.smp.order.model.OrderDataType;
import dk.yousee.smp.order.model.ServicePrefix;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.                                                         
 * User: m14857
 * Date: Oct 12, 2010
 * Time: 4:27:10 PM
 * The object to contain the InetAccess object and StdCpe object.
 * Data structure reference to YouSee Data Migration Requirements: 5.5 Cable Broadband Services Definition
 */
public class CableBBService extends BasicUnit {
    public static OrderDataLevel LEVEL = OrderDataLevel.SERVICE;
    public static OrderDataType TYPE = new OrderDataType(ServicePrefix.SubSvcSpec,"cable_broadband");
    public static NickName NAME = new NickName("broadband");

    /**
     * Contains modemId as long as the service is under construction
     */
    private ModemId tempModemId;

    /**
     * Constructs service -- mostly used by parser (when constructing model)
     * @param model to use
     * @param externalKey external key
     */
    public CableBBService(SubscriberModel model, String externalKey) {
        super(model, externalKey, TYPE, LEVEL, NAME,null);
        model.getServiceLevelUnit().add(this);
    }

    /**
     * Constructs service based on new data from CRM
     * @param model in
     * @param modemId in
     */
    public CableBBService(SubscriberModel model, ModemId modemId) {
        this(model,model.key().CableBBService(modemId));
        this.tempModemId=modemId;
    }
    //children
    private InetAccess inetAccess;

    private StdCpe stdCpe;

    private List<AddnCpe> addnCpe = new ArrayList<AddnCpe>();

    private SMPStaticIP smpStaticIP;

    private SMPWiFi smpWiFi;

    private SMPEmail smpEmail;
    
    private BSA bsa;

    public InetAccess getInetAccess() {
        return inetAccess;
    }

    public void setInetAccess(InetAccess inetAccess) {
        this.inetAccess = inetAccess;
    }

    public StdCpe getStdCpe() {
        return stdCpe;
    }

    public void setStdCpe(StdCpe stdCpe) {
        this.stdCpe = stdCpe;
    }

    public List<AddnCpe> getAddnCpe() {
        return addnCpe;
    }

    public AddnCpe getTheAddnCpe() {
        if (getAddnCpe().size() == 0) return null;
        else return getAddnCpe().get(0);
    }


    public SMPStaticIP getSmpStaticIP() {
        return smpStaticIP;
    }

    public void setSmpStaticIP(SMPStaticIP smpStaticIP) {
        this.smpStaticIP = smpStaticIP;
    }

    public SMPWiFi getSmpWiFi() {
        return smpWiFi;
    }

    public void setSmpWiFi(SMPWiFi smpWiFi) {
        this.smpWiFi = smpWiFi;
    }

    public BSA getBsa() {
		return bsa;
	}
    
    public void setBsa(BSA bsa) {
		this.bsa = bsa;
	}
    
    public SMPEmail getSmpEmail() {
        return smpEmail;
    }

    public void setSmpEmail(SMPEmail smpEmail) {
        this.smpEmail = smpEmail;
    }

    /**
     * This is the business position of this cable service will always work
     * @return the true thing
     */
    public BusinessPosition getPosition() {
        BusinessPosition bp=getInetAccess()==null?null:getInetAccess().getPosition();
        if(bp==null){
            ModemId md=getModemId();
            if(md!=null){
                bp=BusinessPosition.create(md.getId());
            }
        }
        return bp;
    }
    /**
     * @deprecated This way is deprecated, use the getModemId2()
     * @return modemId extracted from external key
     */
    ModemId getModemIdOld() {
        return ModemId.extract(getExternalKey());
    }

    /**
     * @return modemId based on modemId on Internet Access
     */
    public ModemId getModemId() {
        InetAccess access=getInetAccess();
        return access==null?tempModemId:access.getModemId();
    }
}

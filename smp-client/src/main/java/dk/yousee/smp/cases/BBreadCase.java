package dk.yousee.smp.cases;

import dk.yousee.smp.casemodel.vo.base.SubAddressSpec;
import dk.yousee.smp.casemodel.vo.base.SubContactSpec;
import dk.yousee.smp.casemodel.vo.cbp.CableBBService;
import dk.yousee.smp.casemodel.vo.helpers.BasicUnit;
import dk.yousee.smp.order.model.Acct;
import dk.yousee.smp.order.model.OrderDataType;
import dk.yousee.smp.order.model.OrderService;
import java.util.List;

/**
 *
 * @author m27236
 */
public class BBreadCase extends AbstractCase{
    
    /**
     * Read case for BB-services
     * @param acct subscriber
     * @param orderService the orderservice for smp
     */
    public BBreadCase(Acct acct, OrderService orderService){
        super(acct, orderService);
    }
    
    public BBreadCase(SubscriberCase subscriberCase){
        super(subscriberCase.getModel(), subscriberCase.getService());
    }
    /**
     * Works as login
     * @param modemActivationCode
     * @return null or object
     */
    public CableBBService findServiceByActivationCode(String modemActivationCode){
        return getModel().find().firstCableBBService(modemActivationCode);
    }
    
    /**
     * Fetches all the CableBBService for the subscriber.
     * @return empty list if none.
     */
    public List<CableBBService> fetchAllCableBBServices(){
        return getModel().find().CableBBService();
    }
    
    /**
     * Address for subscriber
     * @return 
     */
    public SubAddressSpec fetchSubAddressSpec(){
        return getModel().find().SubAddressSpec();
    }
    
    /**
     * Contact information for subscriber
     * @return 
     */
    public SubContactSpec fetchSubContactSpec(){
        return getModel().find().SubContactSpec();
    }
    
    public BasicUnit findServiceFromExternalKey(OrderDataType type, String externalKey){
        
        if(type.equals(CableBBService.TYPE)){
            List<CableBBService> cableBBServices = fetchAllCableBBServices();
            for(CableBBService cableBBService : cableBBServices){
                for(BasicUnit basicUnit : cableBBService.getChildrenServices()){
                    if(basicUnit.getExternalKey().equalsIgnoreCase(externalKey))
                        return cableBBService;
                }
            }           
        }else{
            return null;
        } 
        
        return null;
    }
}

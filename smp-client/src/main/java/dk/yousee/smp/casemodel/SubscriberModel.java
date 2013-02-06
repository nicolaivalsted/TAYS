package dk.yousee.smp.casemodel;

import dk.yousee.smp.casemodel.vo.helpers.Add;
import dk.yousee.smp.casemodel.vo.helpers.Alloc;
import dk.yousee.smp.casemodel.vo.helpers.BasicUnit;
import dk.yousee.smp.casemodel.vo.helpers.Find;
import dk.yousee.smp.casemodel.vo.helpers.Key;
import dk.yousee.smp.casemodel.vo.helpers.Parse;
import dk.yousee.smp.order.model.Acct;
import dk.yousee.smp.order.model.Order;
import dk.yousee.smp.order.model.Response;
import dk.yousee.smp.order.model.ServiceProviderEnum;
import dk.yousee.smp.order.model.Subscriber;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: aka
 * Date: Oct 12, 2010
 * Time: 12:20:19 PM
 * This model contains the data for a subscriber<br/>
 * <p>
 * This model will define all service plans, child services and their attributes.
 *
 * </p>
 * <p>
 * USAGE:<br/>
 * 1) Create the model based on nothing or a read subscription response<br/>
 *    This is MUST BE done by a use case<br/>
 * 2) query the model for elements<br/>
 *    This is can be done by a use case<br/>
 * 3) update the model elements<br/>
 *    This is can be done by a use case<br/>
 * 4) Send order using send method<br/>
 *    This MUST be done by a use case<br/>
 * </p>
 * <p>
 * The model does NOT describe the activities - this is a task for use-cases.<br/>
 * The models objective is to keep data as consistent as possible - to ensure the customers data is valid.
 * </p>
 * <p>
 * The model is a "service" it takes no action it self. It is not a client.<br/>
 * The shooting-book (drejebog in danish) is a use-case task. See {@link dk.yousee.smp.cases.AbstractCase} and all
 * its siblings.
 * So all activities - timings - when to do that and that - and what data to provide in each step,<br/>
 * is a use-case level information.<br/>
 * At the same time use-cases is responsible for putting the correct values into the model at any time.
 * <p>
 */
public class SubscriberModel implements Serializable {

//    private static final Logger logger = Logger.getLogger(SubscriberModel.class);
    private Subscriber subscriber;

    private Response response = null;
    private static final long serialVersionUID = -8734416875851306130L;

    //By default
    private ServiceProviderEnum provider = ServiceProviderEnum.YouSee;

    public ServiceProviderEnum getProvider() {
        return provider;
    }

    /**
     * Assign the provider if it hs not YouSee
     *
     * @param provider another provider than YouSee
     */
    public void setProvider(ServiceProviderEnum provider) {
        this.provider = provider;
    }

    /**
     * Constructs the model based on an account number, ready for addSubscription and ready for getting response
     *
     * @param acct account to create model for
     */
    public SubscriberModel(Acct acct) {
        subscriber = new Subscriber();
        subscriber.setFornavn("");
        subscriber.setEfternavn("");
        subscriber.setKundeId(acct);
    }


    /**
     * Constructs the model based on a response
     *
     * @param response - should be directly return value from a service call.
     */
    public SubscriberModel(Response response) {
        this(response.getAcct());
        this.response = response;
        new Parse(this).buildSubscriberModel(response.getSmp());
    }


    /**
     * Constructs the model based on a response
     *
     * @param response - should be directly return value from a service call.
     * @param acct     the account
     * @deprecated use constructor SubscriberModel(Response response) - acct is inside it.
     */
    public SubscriberModel(Response response, Acct acct) {
        this(response);
        assert (acct.equals(response.getAcct()));
    }


    public Response getResponse() {
        return response;
    }

    /**
     * The account this response was made from
     *
     * @return account - customers 9 digit account number
     */
    public Acct getAcct() {
        return subscriber.getKundeId();
    }

    /**
     * Does the customer exist in Sigma yet
     *
     * @return true means the customer is registered
     */
    public boolean customerExists() {
        return getResponse().getSmp() != null;
    }

    private List<BasicUnit> serviceLevelUnit = new ArrayList<BasicUnit>();

    public List<BasicUnit> getServiceLevelUnit() {
        return serviceLevelUnit;
    }
    public List<BasicUnit> filterProgress() {
        List<BasicUnit> res=null;
        for(BasicUnit unit:getServiceLevelUnit()){
            List<BasicUnit> progress=unit.filterProgress();
            if(progress!=null){
                if(res==null)res=new ArrayList<BasicUnit>();
                res.addAll(progress);
            }
        }
        return res;
    }

    private Key key = null;

    /**
     * Helper for generating keys to plans etc
     *
     * @return the key helper
     */
    public Key key() {
        if (key == null) key = new Key(this);
        return key;
    }

    private Find find = null;

    /**
     * Helper for finding Plans etc
     *
     * @return the find helper
     */
    public Find find() {
        if (find == null) find = new Find(this, serviceLevelUnit);
        return find;
    }

    private Alloc alloc = null;

    /**
     * Helper for allocating ServicePlans etc
     *
     * @return the allocation helper
     */
    public Alloc alloc() {
        if (alloc == null) alloc = new Alloc(this);
        return alloc;
    }

    private Add add = null;

    /**
     * Helper for making adding ServicePlans etc
     *
     * @return the allocation helper
     */
    public Add add() {
        if (add == null) add = new Add(this);
        return add;
    }

    private Order order = null;

    public Order getOrder() {
        if (order == null) {
            order = new Order();
            order.setAsynchronous(false);
            order.setType(Order.TYPE_PROVISIONING);
            order.setSubscriber(subscriber);
        }
        if (order.getExternalKey() == null && response != null && response.getSmp() != null) {
            order.setExternalKey(response.getSmp().getExternalKey());
        }
        if (order.getExternalKey() == null) {
            order.setExternalKey(key().SubscriberExternalKey(subscriber.getKundeId()));
        }
        return order;
    }

    /**
     * Does the model contain an order to send to sigma ?
     * @return true means yes
     */
    public boolean hasOrder() {
        return order!=null;
    }
}
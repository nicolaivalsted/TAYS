package dk.yousee.smp.cases;

import dk.yousee.smp.casemodel.SubscriberModel;
import dk.yousee.smp.casemodel.vo.base.SampSub;
import dk.yousee.smp.casemodel.vo.base.SubAddressSpec;
import dk.yousee.smp.casemodel.vo.base.SubContactSpec;
import dk.yousee.smp.casemodel.vo.base.SubSpec;
import dk.yousee.smp.order.model.Acct;
import dk.yousee.smp.order.model.Action;
import dk.yousee.smp.order.model.BusinessException;
import dk.yousee.smp.order.model.Order;
import dk.yousee.smp.order.model.OrderService;
import dk.yousee.smp.order.model.Subscriber;

/**
 * Created by IntelliJ IDEA.
 * User: aka
 * Date: Oct 14, 2010
 * Time: 5:51:20 PM<br/>
 * Can perform subscriber tasks, exists?, create, update, delete<br/>
 * Is often a starting point for other use-cases
 */
public class SubscriberCase extends AbstractCase {

//    private static final Logger logger = Logger.getLogger(SubscriberCase.class);

    /**
     * @param service the service where data should be read / written
     * @param acct    the customers 9 digit number
     */
    public SubscriberCase(OrderService service, Acct acct) {
        super(acct, service);
    }

    public SubscriberCase(OrderService service, SubscriberModel model) {
        super(model, service);
    }


    /**
     * Does the customer exist in Sigma yet
     *
     * @return true means the customer is registered
     */
    public boolean customerExists() {
        return getModel().customerExists();
    }


    /**
     * USE-CASE-1: Erling: Lav et smpOrder objekt med ordre om at oprette/opdatere en kunde givet i customer
     *
     * @param customer input for customer objecg
     *                 Produces an order object which can be sent to the BSS adapter to create or update the customer
     * @param address  address input for Add Subscription 
     * @return usecase
     */
    public Order buildAddSubscriptionOrder(CustomerInfo customer, AddressInfo address) {

        setModel(new SubscriberModel(getAcct()));

        Order smpOrder = getModel().getOrder();
        // Populate Subscriber object, contact and address objects
        Subscriber subscriber = smpOrder.getSubscriber();
        subscriber.setFornavn(customer.getFirstName());
        subscriber.setEfternavn(customer.getLastName());
        subscriber.setKundeId(new Acct(customer.getAcct()));


        SubContactSpec mc = getModel().add().SubContactSpec();
        mc.isp.setValue(customer.getIsp());
        mc.phone_cell_number.setValue(customer.getMobiltlf());
        mc.emails_home_address.setValue(customer.getEmail());
        mc.phones_business_number.setValue(customer.getArbejdstlf());
        mc.phones_home_number.setValue(customer.getPrivattlf());

        SubAddressSpec ma = getModel().add().SubAddressSpec();
        ma.ams_id.setValue(address.getAms());
        ma.zipcode.setValue(zip4ch(address.getZipcode()));
        String address1 = address.getAddress1();
        if (address1 == null || address1.length() == 0)
            address1 = "-";
        ma.address1.setValue(address1);
        ma.address2.setValue(address.getAddress2());
        ma.district.setValue(address.getDistrict());
        ma.city.setValue(address.getCity());
        ma.country.setValue(address.getCountry());
        ma.ntd_return_segment_nm.setValue(address.getNtd_return_segment_nm());
        return getModel().getOrder();                        
    }

    /**
     * Will pad a string "0" in front of zip codes less than 4 digits
     * @param zip raw value with potentical only 3 digits
     * @return a four digit zip code 
     */
    public String zip4ch(String zip){
        if(zip==null || zip.trim().length()==0)return null;
        if(zip.length()<4){
            return zip4ch("0"+zip);
        } else {
            return zip;
        }
    }

    public boolean justCreated=false;

    /**
     * Flow state for the subscriber
     * <p>
     * The situation is that WHEN a subscriber has been created (SMP add)
     * then the subscriber engagement (model) must be read again.
     * The creation was done as "snapshot" order, this means the creation
     * was performed before SMP returned the creation order.
     * In other words the processing in SMP has been performed to the end.
     * Therefore it is possible to ask for the engagement right away.
     * </p>
     * @return true when subscriber has been created in this case
     */
    public boolean isJustCreated() {
        return justCreated;
    }

    /**
     * Adds subscription to sigma. It means that a new customer is created
     *
     * @param order the order to process
     * @return the new model starting the customer's content
     * @throws dk.yousee.smp.order.model.BusinessException
     *          when add cannot be performed
     */
    public SubscriberModel addSubscription(Order order) throws BusinessException {
        setErrorMessage(null);
        try {
        setResponse(getService().addSubscription(order));
        } catch (Exception e) {
            e.printStackTrace();
            setErrorMessage(e.getMessage());
            return null;
        }
        if (getResponse().getSmp() == null) setErrorMessage(getResponse().getErrorMessage());
        setModel(new SubscriberModel(getResponse()));
        if (getErrorMessage() != null) {
            throw new BusinessException("could not create subscription, for customer %s, got message: %s"
                    , order.getSubscriber().getKundeId(), getErrorMessage());
        }
        justCreated=true;
        return getModel();
    }

    /**
     * Adds subscription to sigma. It means that a new customer is created
     *
     * @return the new model starting the customer's content
     * @throws dk.yousee.smp.order.model.BusinessException
     *          when add cannot be performed
     */
    public SubscriberModel addSubscription() throws BusinessException {
        return this.addSubscription(getModel().getOrder());
    }

    public SubAddressSpec updateAddress(SubscriberCase.AddressInfo address) throws BusinessException {
        SubAddressSpec subAddressSpec = getModel().find().SubAddressSpec();
        subAddressSpec.ams_id.setValue(address.getAms());
        subAddressSpec.address1.setValue(address.getAddress1());
        subAddressSpec.address2.setValue(address.getAddress2());
        subAddressSpec.zipcode.setValue(zip4ch(address.getZipcode()));
        subAddressSpec.district.setValue(address.getDistrict());
        subAddressSpec.city.setValue(address.getCity());
        subAddressSpec.ntd_return_segment_nm.setValue(address.getNtd_return_segment_nm());
        return subAddressSpec;
    }

    public static class AddressInfo {

        private String ams;

        public String getAms() {
            return ams;
        }

        public void setAms(String ams) {
            this.ams = ams;
        }

        private String address1;
        private String address2;
        private String zipcode;
        private String city;
        private String district;
        private String country;
        private String ntd_return_segment_nm;

        public String getZipcode() {
            return zipcode;
        }

        public void setZipcode(String zipcode) {
            this.zipcode = zipcode;
        }

        public String getAddress1() {
            return address1;
        }

        public void setAddress1(String address1) {
            this.address1 = address1;
        }

        public String getAddress2() {
            return address2;
        }

        public void setAddress2(String address2) {
            this.address2 = address2;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getDistrict() {
            return district;
        }

        public void setDistrict(String district) {
            this.district = district;
        }

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        public String getNtd_return_segment_nm() {
            return ntd_return_segment_nm;
        }

        public void setNtd_return_segment_nm(String ntd_return_segment_nm) {
            this.ntd_return_segment_nm = ntd_return_segment_nm;
        }
    }

    public CustomerInfo readContact() throws BusinessException {
        SubContactSpec plan = getModel().find().SubContactSpec();
        CustomerInfo customer=new CustomerInfo();
        customer.setFirstName(plan.first_name.getValue());
        customer.setLastName(plan.last_name.getValue());
        customer.setIsp(plan.isp.getValue());
        customer.setMobiltlf(plan.phone_cell_number.getValue());
        customer.setEmail(plan.emails_home_address.getValue());
        customer.setArbejdstlf(plan.phones_business_number.getValue());
        customer.setPrivattlf(plan.phones_home_number.getValue());
        return customer;
    }

    public SubContactSpec updateContact(SubscriberCase.CustomerInfo customer) throws BusinessException {
        SubContactSpec plan = getModel().find().SubContactSpec();

        plan.first_name.setValue(customer.getFirstName());
        plan.last_name.setValue(customer.getLastName());
        plan.isp.setValue(customer.getIsp());
        plan.phone_cell_number.setValue(customer.getMobiltlf());
        plan.emails_home_address.setValue(customer.getEmail());
        plan.phones_business_number.setValue(customer.getArbejdstlf());
        plan.phones_home_number.setValue(customer.getPrivattlf());
        return plan;
    }
   /**
     * Customer data to fill in mostly from casper table data.
     */
    public static class CustomerInfo {
        private String firstName;
        private String lastName;
        private String acct;
        private String mobiltlf;
        private String email;
        private String arbejdstlf;
        private String privattlf;

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        public void setAcct(String acct) {
            this.acct = acct;
        }

        public String getFirstName() {
            return firstName;
        }

        public String getLastName() {
            return lastName;
        }

        /**
         * @return Customer ID
         */
        public String getAcct() {
            return acct;
        }

        public String getMobiltlf() {
            return mobiltlf;
        }

        public void setMobiltlf(String mobiltlf) {
            this.mobiltlf = mobiltlf;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getArbejdstlf() {
            return arbejdstlf;
        }

        public void setArbejdstlf(String arbejdstlf) {
            this.arbejdstlf = arbejdstlf;
        }

        public String getPrivattlf() {
            return privattlf;
        }

        public void setPrivattlf(String privattlf) {
            this.privattlf = privattlf;
        }

        private String isp;

        public String getIsp() {
            return isp;
        }

        public void setIsp(String isp) {
            this.isp = isp;
        }
    }

    public Order deleteSubscription() throws BusinessException {
        // D01_Deactivate_Subscriber_Account_XML.xml
        SubscriberModel model = getModel();
        if (getResponse().getSmp() == null) setErrorMessage(getResponse().getErrorMessage());
        if (getErrorMessage() != null) {
            throw new BusinessException("could not create subscription, for customer %s, got message: %s"
                    , getAcct(), getErrorMessage());
        }

        SubSpec subSpec = model.add().SubSpec(model.getOrder().getExternalKey());
        SubAddressSpec subAddressSpec = model.find().SubAddressSpec();
        SubContactSpec subContactSpec = model.find().SubContactSpec();
        SampSub sampSub = model.find().SampSub();

        subSpec.getDefaultOrderData().addChild(subContactSpec.getDefaultOrderData());
        subSpec.getDefaultOrderData().addChild(subAddressSpec.getDefaultOrderData());
        subSpec.getDefaultOrderData().addChild(sampSub.getDefaultOrderData());

        model.getOrder().getOrderData().remove(1);
        model.getOrder().getOrderData().remove(1);
        model.getOrder().getOrderData().get(1).setAction(Action.DELETE);

        subSpec.delete();
        return model.getOrder();
    }

}

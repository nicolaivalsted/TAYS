package dk.yousee.smp.cases;

import dk.yousee.smp.casemodel.vo.BusinessPosition;
import dk.yousee.smp.casemodel.vo.mail.ForeningsMailService;
import dk.yousee.smp.casemodel.vo.mail.Mail;
import dk.yousee.smp.order.model.Acct;
import dk.yousee.smp.order.model.Action;
import dk.yousee.smp.order.model.BusinessException;
import dk.yousee.smp.order.model.Order;
import dk.yousee.smp.order.model.OrderService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Case for forenings mail update in SMP
 */
public class ForeningsMailCase extends AbstractCase {
    public ForeningsMailCase(OrderService service, Acct acct) {
        super(acct, service);
    }

    /**
     * Construct this case based on existing Subscriber Case<br/>
     * This is a kind of chaining of use-cases. <br/>
     *
     * @param customerCase subscriber case
     * @param keepModel true to use the model from subscriber case, false to
     * start a new model (default originally false)
     *
     */
    public ForeningsMailCase(SubscriberCase customerCase, boolean keepModel) {
        super(selectModel(customerCase.getModel(), keepModel), customerCase.getService());
    }

    /**
     * Inner class that holds the contract between CRM and SMP
     */
    public static class ForeningsData {
        /**
         * Key to forenings mail - mandatory field
         */
        private String product;
        
        public String getProduct() {
            return product;
        }
        
        public void setProduct(String product) {
            this.product = product;
        }
        /**
         * Description of forenings mail
         */
        private String name;
        
        public String getName() {
            return name;
        }
        
        public void setName(String name) {
            this.name = name;
        }
    }

    /**
     * Create ForeningsMail
     *
     * @param position where?
     * @param lineItem data to add
     * @return order for this
     * @throws dk.yousee.smp.order.model.BusinessException on error like no
     * subscriber
     */
    public Order createProvisioning(BusinessPosition position, ForeningsData lineItem) throws BusinessException {
        ensureAcct();
        
        Mail def = getModel().alloc().ForeningsMail(position);
        
        def.product_code.setValue(lineItem.getProduct());
        def.product_name.setValue(lineItem.getName());
        return getModel().getOrder();
    }
    
    public ForeningsData readProvisioning(BusinessPosition position) throws BusinessException {
        ForeningsMailService mailService = getModel().find().ForeningsMailService(position);
        ForeningsData res;
        if (mailService == null) {
            res = null;
        } else {
            res = new ForeningsData();
            res.setProduct(mailService.getMail().product_code.getValue());
            res.setName(mailService.getMail().product_name.getValue());
        }
        return res;
    }

//    public Order updateProvisioning(BusinessPosition position, ForeningsData lineItem) throws BusinessException {
//        ensureAcct();
//        Mail def = getModel().find().ForeningsMail(position);
//        if(lineItem.getProduct()!=null){
//            def.product_code.setValue(lineItem.getProduct());
//        }
//        if(lineItem.getName()!=null){
//            def.product_name.setValue(lineItem.getName());
//        }
//        return getModel().getOrder();
//    }
    /**
     * Delete function
     *
     * @param position selected service plan instance
     * @return true if anything was marked for delete, false if nothing marked
     * for delete.<br/>
     * Hereby the client can decide if anything needs to be send to Sigma
     * @throws dk.yousee.smp.order.model.BusinessException when <br/>
     * 1) The customer does not exist<br/>
     */
    public boolean delete(BusinessPosition position)
            throws BusinessException {
        ensureAcct();
        boolean res;
        res = buildOrderFromAction(position, Action.DELETE);
        return res;
    }

    /**
     * Constructs an order from action change
     *
     * @param position selected service plan instance (key is modemId)
     * @param action the action to send to the subscription
     * @return true if anything to do
     */
    private boolean buildOrderFromAction(BusinessPosition position, Action action) {
        boolean doAnything = false;
        {
            ForeningsMailService service = getModel().find().ForeningsMailService(position);
            if (service != null) {
                doAnything = true;

                /**
                 * It was proven from tests that delete on top level works.<br/>
                 * But suspend/resume must be performed at each child-service
                 * <p>
                 * Tests shows that marking elements for something the element
                 * already is results in no order line in Sigma. This might be
                 * use full when sending commands to Sigma - so sending too much
                 * does not really matter in Sigma !!
                 * </p>
                 *
                 */
                if (action == Action.DELETE) {
                    service.sendAction(action);
                } else {
                    service.cascadeSendAction(action);
                }
            }
        }
        return doAnything;
    }
    
    public class ForeningsMailActivationData {
        BusinessPosition position;
        String externalKey;
        
        public ForeningsMailActivationData(BusinessPosition position) {
            this.position = position;
        }
        private String product;
        
        public String getProduct() {
            return product;
        }
        
        public BusinessPosition getPosition() {
            return position;
        }
        
        public void setProduct(String product) {
            this.product = product;
        }
        private String name;
        
        public String getName() {
            return name;
        }
        
        public void setName(String name) {
            this.name = name;
        }
        private String customerId;
        
        public String getCustomerId() {
            return customerId;
        }
        
        public void setCustomerId(String customerId) {
            this.customerId = customerId;
        }
        private Date conversationDate;
        
        public Date getConversationDate() {
            return conversationDate;
        }
        
        public void setConversationDate(Date conversationDate) {
            this.conversationDate = conversationDate;
        }
        private Boolean conversation;
        
        public Boolean getConversation() {
            return conversation;
        }
        
        public void setConversation(Boolean conversation) {
            this.conversation = conversation;
        }
        
        public String getExternalKey() {
            return externalKey;
        }
        
        public void setExternalKey(String externalKey) {
            this.externalKey = externalKey;
        }
        
        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder();
            sb.append("{position=").append(position);
            sb.append(", product='").append(product).append('\'');
            sb.append(", name='").append(getName()).append('\'');
            if (getCustomerId() != null)
                sb.append(", customerId='").append(customerId).append('\'');
            if (getConversationDate() != null)
                sb.append(", conversationDate=").append(conversationDate);
            if (getConversation() != null)
                sb.append(", conversation=").append(conversation);
            sb.append('}');
            return sb.toString();
        }
    }

    /**
     * Read all activation data for this subscriber
     *
     * @return list of activation data
     * @throws BusinessException when subscriber is missing
     */
    public List<ForeningsMailActivationData> readAll() throws BusinessException {
        ensureAcct();
        
        List<ForeningsMailActivationData> res = new ArrayList<ForeningsMailActivationData>();
        for (ForeningsMailService service : getModel().find().ForeningsMailService()) {
            ForeningsMailActivationData row = new ForeningsMailActivationData(service.getPosition());
            Mail mail = service.getMail();
            if (mail != null) { // should never be null
                row.setProduct(mail.product_code.getValue());
                row.setName(mail.product_name.getValue());
                row.setCustomerId(mail.customer_id.getValue());
                row.setConversation(mail.getConversation());
                row.setConversationDate(mail.getConversationDate());
                row.setExternalKey(mail.getExternalKey());
            } else {
                row.setProduct("error");
                row.setName("error");
            }
            res.add(row);
        }
        return res;
    }

    /**
     * @param position that identifies forenings mail
     * @param useMailAccount is the mail used by subscriber (assign true). If
     * user dismiss then assign false.
     * @return order to pass to SMP
     * @throws BusinessException when subscriber is missing
     */
    public Order activate(BusinessPosition position, boolean useMailAccount) throws BusinessException {
        ensureAcct();
        Mail mail = getModel().find().ForeningsMail(position);
        mail.setConversation(useMailAccount);
        mail.setConversationDate(new Date());
        
        return getModel().getOrder();
    }

    /**
     * Used to migrate a subscribers forenings mails. It is assumed that the
     * customer says "YES" to having the mail - otherwise he/she should not
     * migrate ...
     *
     * @param position that identifies forenings mail
     * @param customerId - the customer in Dansk Kabel TV original system.
     * @return order to pass to SMP
     * @throws BusinessException when subscriber is missing or position not found
     */
    public Order activateOnMigration(BusinessPosition position, String customerId) throws BusinessException {
        ensureAcct();
        Mail mail = getModel().find().ForeningsMail(position);
        if (mail == null)
            throw new BusinessException("No mail found on business: %s", position);
        mail.customer_id.setValue(customerId);
        mail.setConversation(true);
        mail.setConversationDate(new Date());
        
        return getModel().getOrder();
    }
}

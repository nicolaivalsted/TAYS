package dk.yousee.smp.casemodel.vo.mail;

import dk.yousee.smp.casemodel.SubscriberModel;
import dk.yousee.smp.casemodel.vo.BusinessPosition;
import dk.yousee.smp.casemodel.vo.helpers.BasicUnit;
import dk.yousee.smp.casemodel.vo.helpers.PropHolder;
import dk.yousee.smp.order.model.OrderDataLevel;
import dk.yousee.smp.order.model.OrderDataType;
import dk.yousee.smp.order.model.ServicePrefix;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Definition of mail service
 */
public class Mail extends BasicUnit {

    public static OrderDataLevel LEVEL = OrderDataLevel.CHILD_SERVICE;
    public static OrderDataType TYPE = new OrderDataType(ServicePrefix.SubSvcSpec,"foreningsmail");


    public Mail(SubscriberModel model, String externalKey, ForeningsMailService parent) {
        super(model, externalKey, TYPE, LEVEL,null, parent);
        parent.setMail(this);
    }

    public Mail(SubscriberModel model, BusinessPosition position) {
        super(model, model.key().generateUUID(), TYPE, LEVEL, null,new ForeningsMailService(model,model.key().generateUUID()));
        business_position.setValue(position.getId());
        getParent().setMail(this);
    }
    
    public ForeningsMailService getParent() {
        return (ForeningsMailService) super.getParent();
    }

    /**
     * Name of field
     */
    public static final String BUSINESS_POSITION = "business_position";
    /**
     * identifier that identify the subscribers modem among all the modems the subscriber has.
     * Field value can be "1", "2" etc. It is only required to be unique for the subscriber.
     * So two different subscribers can both have position called "1"
     * The objective is to manage relation to CRM subscription.
     * This is an instance key to service plan. It is normally never modified.
     * YouSee will fill in modem_id / aftale Nr / ... tbd.
     */
    protected PropHolder business_position = new PropHolder(this, BUSINESS_POSITION);

    BusinessPosition getPosition() {
        return BusinessPosition.create(business_position.getValue());
    }

    public static final String PRODUCT_CODE ="product_code";
    public PropHolder product_code = new PropHolder(this, PRODUCT_CODE,true);

    public static final String PRODUCT_NAME="product_name";
    public PropHolder product_name = new PropHolder(this, PRODUCT_NAME);

    public static final String CUSTOMER_ID="customer_id";
    public PropHolder customer_id = new PropHolder(this, CUSTOMER_ID);


    public static final String CONVERSATION="conversation";
    public PropHolder conversation = new PropHolder(this, CONVERSATION);

    public Boolean getConversation() {
        Boolean res;
        if(conversation.hasValue()){
            res=Boolean.parseBoolean(conversation.getValue());
        } else {
            res=null;
        }
        return res;
    }

    public void setConversation(Boolean conversation) {
        if(conversation==null){
            this.conversation.clearValue();
        }else {
            this.conversation.setValue(conversation.toString());
        }

    }
    public static final SimpleDateFormat DATE_FORMAT=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static final String CONVERSATION_DATE="conversation_date";
    protected PropHolder conversation_date = new PropHolder(this, CONVERSATION_DATE);

    public Date getConversationDate() {
        Date res;
        if(conversation_date.hasValue()){
            try {
                res=DATE_FORMAT.parse(conversation_date.getValue());
            } catch (ParseException e) {
                res=null;
            }
        } else {
            res=null;
        }
        return res;
    }

    public void setConversationDate(Date conversationDate) {
        if(conversationDate==null){
            this.conversation_date.clearValue();
        }else {
            this.conversation_date.setValue(DATE_FORMAT.format(conversationDate));
        }
    }
}

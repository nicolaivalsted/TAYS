package dk.yousee.tays.bbservice.restbase.asucase;

import java.util.Date;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author jablo
 */
@XmlRootElement
public class SimplifiedSubscriberService {
    private String externalKey;
    private String type;
    private String modemId;
    private String position;
    private String suspendAbuseState;
    // BB
    private String rateCode;
    private String activationReference;
    private String productCode;
    // wifi
    private String ssid, psk, channel;
    private String ss_id_5g, gw_channel_id_5g, psk_5g;
    // foreningsmail
    private String product;
    private String name;
    private String customerId;
    private Date conversationDate;
    private Boolean conversation;
    private String ip;

    public SimplifiedSubscriberService() {
    }

    public String getActivationReference() {
        return activationReference;
    }

    public void setActivationReference(String activationReference) {
        this.activationReference = activationReference;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getExternalKey() {
        return externalKey;
    }

    public void setExternalKey(String externalKey) {
        this.externalKey = externalKey;
    }

    public String getProductCode() {
        return productCode;
    }

    public void setProductCode(String productCode) {
        this.productCode = productCode;
    }

    public String getPsk() {
        return psk;
    }

    public void setPsk(String psk) {
        this.psk = psk;
    }
    
    public String getPsk_5g() {
        return psk_5g;
    }

    public void setPsk_5g(String psk_5g) {
        this.psk_5g = psk_5g;
    }

    public String getRateCode() {
        return rateCode;
    }

    public void setRateCode(String rateCode) {
        this.rateCode = rateCode;
    }

    public String getSsid() {
        return ssid;
    }

    public void setSsid(String ssid) {
        this.ssid = ssid;
    }

    public String getSs_id_5g() {
        return ss_id_5g;
    }

    public void setSs_id_5g(String ss_id_5g) {
        this.ss_id_5g = ss_id_5g;
    }

    public String getGw_channel_id_5g() {
        return gw_channel_id_5g;
    }

    public void setGw_channel_id_5g(String gw_channel_id_5g) {
        this.gw_channel_id_5g = gw_channel_id_5g;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Boolean getConversation() {
        return conversation;
    }

    public void setConversation(Boolean conversation) {
        this.conversation = conversation;
    }

    public Date getConversationDate() {
        return conversationDate;
    }

    public void setConversationDate(Date conversationDate) {
        this.conversationDate = conversationDate;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProduct() {
        return product;
    }

    public void setProduct(String product) {
        this.product = product;
    }

    public String getModemId() {
        return modemId;
    }

    public void setModemId(String modemId) {
        this.modemId = modemId;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String businessPosition) {
        this.position = businessPosition;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getSuspendAbuseState() {
        return suspendAbuseState;
    }

    public void setSuspendAbuseState(String suspendStateCode) {
        this.suspendAbuseState = suspendStateCode;
    }
}

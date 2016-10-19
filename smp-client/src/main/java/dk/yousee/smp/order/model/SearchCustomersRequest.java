package dk.yousee.smp.order.model;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: m14857
 * Date: Mar 3, 2011
 * Time: 10:24:51 AM
 * To search Customers by this request.
 * <p>
 *     This value object is in a special way function overloaded.
 *     You might only fill in part of the fields.
 *     The strategy for this assignment is..
 * </p>
 */
public class SearchCustomersRequest implements Serializable {

    private static final long serialVersionUID = -8811963453205534141L;
    private Acct kundeId;


    /**
     * @return the 9 digit customer key
     */
    public Acct getKundeId() {
        return kundeId;
    }

    /**
     * Assign customer id
     * @param kundeId 9 digit string
     */
    public void setKundeId(Acct kundeId) {
        this.kundeId = kundeId;
    }

    private String internId;
    private String fornavn;
    private String efternavn;
    private String city;
    private String zipcode;
    private String address1;
    private String address2;
    private String district;
    private String cm_mac;
    private String cpe_mac;


    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

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

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getCm_mac() {
        return cm_mac;
    }

    public void setCm_mac(String cm_mac) {
        this.cm_mac = cm_mac;
    }

    public String getCpe_mac() {
        return cpe_mac;
    }

    public void setCpe_mac(String cpe_mac) {
        this.cpe_mac = cpe_mac;
    }

    public void setInternId(String id){
        this.internId = id;
    }
    public String getInternId(){
        return this.internId;
    }

    public String getFornavn() {
        return fornavn;
    }

    public void setFornavn(String fornavn) {
        this.fornavn = fornavn;
    }

    public String getEfternavn() {
        return efternavn;
    }

    public void setEfternavn(String efternavn) {
        this.efternavn = efternavn;
    }

    /**
     * The telephone number for customer VOIP service plan
     */
    private String voipPhoneNumber;


    /**
     *
     * @return query for VOIP phone number, null means no qyery for this
     */
    public String getVoipPhoneNumber() {
        return voipPhoneNumber;
    }

    /**
     * Assign VOIP phone number to query for
     * @param voipPhoneNumber to query for, let it unassigned if no query is wanted
     */
    public void setVoipPhoneNumber(String voipPhoneNumber) {
        this.voipPhoneNumber = voipPhoneNumber;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("{");
        sb.append("\"internId\":\"").append(kundeId==null?"not specified":getInternId()).append('"');
        if (fornavn != null) sb.append(", \"fornavn\":\"").append(getFornavn()).append('"');
        if (efternavn != null) sb.append(", \"efternavn\":\"").append(getEfternavn()).append('"');
        if (address1 != null) sb.append(", \"address1\":\"").append(getAddress1()).append('"');
        if (address2 != null) sb.append(", \"address2\":\"").append(getAddress2()).append('"');
        if (city != null) sb.append(", \"city\":\"").append(getCity()).append('"');
        if (zipcode != null) sb.append(", \"zipcode\":\"").append(getZipcode()).append('"');
        if (district != null) sb.append(", \"district\":\"").append(getDistrict()).append('"');
        if (cm_mac != null) sb.append(", \"cm_mac\":\"").append(getCm_mac()).append('"');
        if (cpe_mac != null) sb.append(", \"cpe_mac\":\"").append(getCpe_mac()).append('"');
        if (voipPhoneNumber != null) sb.append(", \"voipPhoneNumber\":\"").append(getVoipPhoneNumber()).append('"');
        sb.append('}');
        return sb.toString();
    }
}


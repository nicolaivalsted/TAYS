package dk.yousee.smp.order.model;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: m14857
 * Date: Mar 7, 2011
 * Time: 12:53:21 PM
 * The customer info return by search customer.
 */
public final class CustomerInfo implements Serializable {
    private static final long serialVersionUID = 1521257064570975454L;

    private String acct;
    private String zipcode;
    private String district;
    private String ntd_return_segment;
    private String first_name;
    private String last_name;
    private String city;
    private String address1;
    private String address2;

    /**
     * Subscriber that matches this search
     * @return mandatory field
     */
    public String getAcct() {
        return acct;
    }

    public void setAcct(String acct) {
        this.acct = acct;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getNtd_return_segment() {
        return ntd_return_segment;
    }

    public void setNtd_return_segment(String ntd_return_segment) {
        this.ntd_return_segment = ntd_return_segment;
    }

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
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
    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * Compare the row with a row of same type. They are identical if same account (same subscriberId)
     * @param obj another CustomerInfo
     * @return true when identical
     */
    @Override
    public boolean equals(Object obj) {
        if (getAcct() == null)
            return false;
        if (!(obj instanceof CustomerInfo))
            return false;
        CustomerInfo ci = (CustomerInfo) obj;
        return getAcct().equals(ci.getAcct());
    }

    @Override
    public int hashCode() {
        if (getAcct() == null)
            return 0;
        return getAcct().hashCode();
    }


    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("{\"acct\":\"").append(acct).append('"');
        if (first_name != null) sb.append(", \"first_name\":\"").append(getFirst_name()).append('"');
        if (last_name != null) sb.append(", \"last_name\":\"").append(getLast_name()).append('"');
        if (address1 != null) sb.append(", \"address1\":\"").append(getAddress1()).append('"');
        if (address2 != null) sb.append(", \"address2\":\"").append(getAddress2()).append('"');
        if (zipcode != null) sb.append(", \"zipcode\":\"").append(getZipcode()).append('"');
        if (city != null) sb.append(", \"city\":\"").append(getCity()).append('"');
        if (district != null) sb.append(", \"district\":\"").append(getDistrict()).append('"');
        if (ntd_return_segment != null) sb.append(", \"ntd_return_segment\":\"").append(getNtd_return_segment()).append('"');
        if (status != null) sb.append(", \"status\":\"").append(getStatus()).append('"');
        sb.append('}');
        return sb.toString();
    }

}

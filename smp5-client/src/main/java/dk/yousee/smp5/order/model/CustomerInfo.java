package dk.yousee.smp5.order.model;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA. User: m14857 Date: Mar 7, 2011 Time: 12:53:21 PM
 * The customer info return by search customer.
 */
public final class CustomerInfo implements Serializable {
	private static final long serialVersionUID = 1521257064570975454L;

	private String acct;
	private String zipcode;
	private String district;
	private String ntd_return_segment;
	private String email;
	private String first_name;
	private String last_name;
	private String city;
	private String floor;
	private String streetNm;
	private String streetNum;
	private String side;
	private String phoneNumber;
	private String address2;
	private String isp;

	/**
	 * Subscriber that matches this search
	 * 
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

	public String getFloor() {
		return floor;
	}

	public void setFloor(String floor) {
		this.floor = floor;
	}

	public String getStreetNm() {
		return streetNm;
	}

	public void setStreetNm(String streetNm) {
		this.streetNm = streetNm;
	}

	public String getStreetNum() {
		return streetNum;
	}

	public void setStreetNum(String streetNum) {
		this.streetNum = streetNum;
	}

	public String getSide() {
		return side;
	}

	public void setSide(String side) {
		this.side = side;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public String getIsp() {
		return isp;
	}

	public void setIsp(String isp) {
		this.isp = isp;
	}

	/**
	 * Compare the row with a row of same type. They are identical if same
	 * account (same subscriberId)
	 * 
	 * @param obj
	 *            another CustomerInfo
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
		return "CustomerInfo [acct=" + acct + ", zipcode=" + zipcode + ", district=" + district + ", ntd_return_segment=" + ntd_return_segment + ", email="
				+ email + ", first_name=" + first_name + ", last_name=" + last_name + ", city=" + city + ", floor=" + floor + ", streetNm=" + streetNm
				+ ", streetNum=" + streetNum + ", side=" + side + ", phoneNumber=" + phoneNumber + ", address2=" + address2 + ", isp=" + isp + ", status="
				+ status + "]";
	}

}

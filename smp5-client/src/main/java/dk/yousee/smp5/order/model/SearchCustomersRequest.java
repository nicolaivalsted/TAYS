package dk.yousee.smp5.order.model;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA. User: m14857 Date: Mar 3, 2011 Time: 10:24:51 AM To
 * search Customers by this request.
 * <p>
 * This value object is in a special way function overloaded. You might only
 * fill in part of the fields. The strategy for this assignment is..
 * </p>
 */
public class SearchCustomersRequest implements Serializable {

	private static final long serialVersionUID = -8811963453205534141L;
	private Acct kundeId;

	private String subscriber;
	private String fornavn;
	private String efternavn;
	private String privattelefon;
	private String email;
	private String postnr;
	private String kommune;
	private String bynavn;
	private String stednavn;
	private String amsid;
	private String chipid;
	private String serialNumber;
	private String cableUnit;
	private String smartcardSerial;
	private String cm_mac;
	private String cpe_mac;
	private String voipPhoneNumber;
	private String street_nm;
	private String street;
	private String husnr;
	private String side;
	private String floor;

	/**
	 * @return the 9 digit customer key
	 */
	public Acct getKundeId() {
		return kundeId;
	}

	/**
	 * Assign customer id
	 * 
	 * @param kundeId
	 *            9 digit string
	 */
	public void setKundeId(Acct kundeId) {
		this.kundeId = kundeId;
	}

	public String getSubscriber() {
		return subscriber;
	}

	public void setSubscriber(String subscriber) {
		this.subscriber = subscriber;
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

	public String getPrivattelefon() {
		return privattelefon;
	}

	public void setPrivattelefon(String privattelefon) {
		this.privattelefon = privattelefon;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPostnr() {
		return postnr;
	}

	public void setPostnr(String postnr) {
		this.postnr = postnr;
	}

	public String getKommune() {
		return kommune;
	}

	public void setKommune(String kommune) {
		this.kommune = kommune;
	}

	public String getStednavn() {
		return stednavn;
	}

	public void setStednavn(String stednavn) {
		this.stednavn = stednavn;
	}

	public String getAmsid() {
		return amsid;
	}

	public void setAmsid(String amsid) {
		this.amsid = amsid;
	}

	public String getBynavn() {
		return bynavn;
	}

	public void setBynavn(String bynavn) {
		this.bynavn = bynavn;
	}

	public String getChipid() {
		return chipid;
	}

	public void setChipid(String chipid) {
		this.chipid = chipid;
	}

	public String getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

	public String getCableUnit() {
		return cableUnit;
	}

	public void setCableUnit(String cableUnit) {
		this.cableUnit = cableUnit;
	}

	public String getSmartcardSerial() {
		return smartcardSerial;
	}

	public void setSmartcardSerial(String smartcardSerial) {
		this.smartcardSerial = smartcardSerial;
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

	public String getVoipPhoneNumber() {
		return voipPhoneNumber;
	}

	public void setVoipPhoneNumber(String voipPhoneNumber) {
		this.voipPhoneNumber = voipPhoneNumber;
	}

	public String getStreet_nm() {
		return street_nm;
	}

	public void setStreet_nm(String street_nm) {
		this.street_nm = street_nm;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getHusnr() {
		return husnr;
	}

	public void setHusnr(String husnr) {
		this.husnr = husnr;
	}

	public String getSide() {
		return side;
	}

	public void setSide(String side) {
		this.side = side;
	}

	public String getFloor() {
		return floor;
	}

	public void setFloor(String floor) {
		this.floor = floor;
	}

}

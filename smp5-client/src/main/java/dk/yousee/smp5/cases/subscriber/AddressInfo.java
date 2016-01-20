package dk.yousee.smp5.cases.subscriber;

/**
 * @author m64746
 *
 *         Date: 16/10/2015 Time: 09:46:13
 */
public class AddressInfo {
	private String ams;
	private String streetName;
	private String zipcode;
	private String city;
	private String district;
	private String floor;
	private String geographicName;
	private String streetNumber;
	private String doorCode;

	public String getDoorCode() {
		return doorCode;
	}

	public void setDoorCode(String doorCode) {
		this.doorCode = doorCode;
	}

	public String getStreetNumber() {
		return streetNumber;
	}

	public void setStreetNumber(String streetNumber) {
		this.streetNumber = streetNumber;
	}

	public String getAms() {
		return ams;
	}

	public void setAms(String ams) {
		this.ams = ams;
	}

	public String getGeographicName() {
		return geographicName;
	}

	public void setGeographicName(String geographicName) {
		this.geographicName = geographicName;
	}

	public String getStreetName() {
		return streetName;
	}

	public void setStreetName(String streetName) {
		this.streetName = streetName;
	}

	public String getZipcode() {
		return zipcode;
	}

	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
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

	public String getFloor() {
		return floor;
	}

	public void setFloor(String floor) {
		this.floor = floor;
	}

}

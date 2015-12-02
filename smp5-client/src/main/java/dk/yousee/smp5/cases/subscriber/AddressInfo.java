/**
 * 
 */
package dk.yousee.smp5.cases.subscriber;

/**
 * @author m64746
 *
 *         Date: 16/10/2015 Time: 09:46:13
 */
public class AddressInfo {
	private String ams;

	public String getAms() {
		return ams;
	}

	public void setAms(String ams) {
		this.ams = ams;
	}

	private String streetName;
	private String zipcode;
	private String city;
	private String district;
	private String country;
	private String floor;
	private String ntd_return_segment_nm;
	private String cableUnit;

	public String getStreetName() {
		return streetName;
	}

	public void setStreetName(String streetName) {
		this.streetName = streetName;
	}

	public String getCableUnit() {
		return cableUnit;
	}

	public void setCableUnit(String cableUnit) {
		this.cableUnit = cableUnit;
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

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getNtd_return_segment_nm() {
		return ntd_return_segment_nm;
	}

	public String getFloor() {
		return floor;
	}

	public void setFloor(String floor) {
		this.floor = floor;
	}

	public void setNtd_return_segment_nm(String ntd_return_segment_nm) {
		this.ntd_return_segment_nm = ntd_return_segment_nm;
	}

}

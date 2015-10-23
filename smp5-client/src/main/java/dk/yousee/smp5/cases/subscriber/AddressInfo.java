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

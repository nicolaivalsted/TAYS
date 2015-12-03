package dk.yousee.smp5.cases.subscriber;

/**
 * @author m64746
 *
 *         Date: 16/10/2015 Time: 09:46:05
 */
public class ContactInfo {
	private String firstName;
	private String lastName;
	private String acct;
	private String email;
	private String privattlf;

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public void setAcct(String acct) {
		this.acct = acct;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	/**
	 * @return Customer ID
	 */
	public String getAcct() {
		return acct;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPrivattlf() {
		return privattlf;
	}

	public void setPrivattlf(String privattlf) {
		this.privattlf = privattlf;
	}

	private String isp;

	public String getIsp() {
		return isp;
	}

	public void setIsp(String isp) {
		this.isp = isp;
	}

}

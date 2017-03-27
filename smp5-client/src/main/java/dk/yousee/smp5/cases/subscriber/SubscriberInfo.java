package dk.yousee.smp5.cases.subscriber;

/**
 * @author m64746
 *
 *         Date: Sep 8, 2016 Time: 9:24:12 AM
 */
public class SubscriberInfo {
	private String lid;
	private String acct;
	private String linkid;
	private String segment;
	private String cuAccount;
	private String customerNo;

	public String getSegment() {
		return segment;
	}

	public void setSegment(String segment) {
		this.segment = segment;
	}

	public String getCuAccount() {
		return cuAccount;
	}

	public void setCuAccount(String cuAccount) {
		this.cuAccount = cuAccount;
	}

	public String getCustomerNo() {
		return customerNo;
	}

	public void setCustomerNo(String customerNo) {
		this.customerNo = customerNo;
	}

	public String getLid() {
		return lid;
	}

	public void setLid(String lid) {
		this.lid = lid;
	}

	public String getAcct() {
		return acct;
	}

	public void setAcct(String acct) {
		this.acct = acct;
	}

	public String getLinkid() {
		return linkid;
	}

	public void setLinkid(String linkid) {
		this.linkid = linkid;
	}

}

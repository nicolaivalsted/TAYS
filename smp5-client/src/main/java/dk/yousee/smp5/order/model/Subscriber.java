package dk.yousee.smp5.order.model;

import java.io.Serializable;

public final class Subscriber implements Serializable {

	private static final long serialVersionUID = 7796787876769906891L;

	private Acct kundeId;
	private String internId;
	private String lid;
	private String linkid;
	private String customerNo;
	private String segment;
	private String cuAccount;
	private boolean eksisterendeKunde = true;

	public Acct getKundeId() {
		return kundeId;
	}

	public void setKundeId(Acct kundeId) {
		this.kundeId = kundeId;
	}

	public String getInternId() {
		return internId;
	}

	public void setInternId(String internId) {
		this.internId = internId;
	}

	public String getLid() {
		return lid;
	}

	public void setLid(String lid) {
		this.lid = lid;
	}

	public boolean getEksisterendeKunde() {
		return eksisterendeKunde;
	}

	public void setEksisterendeKunde(boolean eksisterendeKunde) {
		this.eksisterendeKunde = eksisterendeKunde;
	}

	public String getLinkid() {
		return linkid;
	}

	public void setLinkid(String linkid) {
		this.linkid = linkid;
	}

	public String getCustomerNo() {
		return customerNo;
	}

	public void setCustomerNo(String customerNo) {
		this.customerNo = customerNo;
	}

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

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append("{kundeId=").append(kundeId);
		sb.append(", internId='").append(internId).append('\'');
		sb.append(", eksisterendeKunde=").append(eksisterendeKunde);
		sb.append('}');
		return sb.toString();
	}

}

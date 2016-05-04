package dk.yousee.smp5.order.model;

import java.io.Serializable;

public final class Subscriber implements Serializable {

	private static final long serialVersionUID = 7796787876769906891L;

	private Acct kundeId;
	private String internId;
	private String lid;
	private String fornavn;
	private String efternavn;
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

	public String getFornavn() {
		return fornavn;
	}
	
	public String getLid() {
		return lid;
	}

	public void setLid(String lid) {
		this.lid = lid;
	}

	/**
	 * @deprecated use SubContactSpec instead of
	 * @param fornavn
	 *            first name of subscriber
	 */
	public void setFornavn(String fornavn) {
		this.fornavn = fornavn;
	}

	public String getEfternavn() {
		return efternavn;
	}

	/**
	 * @deprecated use SubContactSpec instead of
	 * @param efternavn
	 *            last name of subscriber
	 */
	public void setEfternavn(String efternavn) {
		this.efternavn = efternavn;
	}

	public boolean getEksisterendeKunde() {
		return eksisterendeKunde;
	}

	public void setEksisterendeKunde(boolean eksisterendeKunde) {
		this.eksisterendeKunde = eksisterendeKunde;
	}

	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append("{kundeId=").append(kundeId);
		sb.append(", internId='").append(internId).append('\'');
		sb.append(", fornavn='").append(fornavn).append('\'');
		sb.append(", efternavn='").append(efternavn).append('\'');
		sb.append(", eksisterendeKunde=").append(eksisterendeKunde);
		sb.append('}');
		return sb.toString();
	}

}

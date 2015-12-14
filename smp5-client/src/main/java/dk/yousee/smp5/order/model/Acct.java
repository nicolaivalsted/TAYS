package dk.yousee.smp5.order.model;

import java.io.Serializable;

public final class Acct implements Serializable {

	static final private long serialVersionUID = -8509563099046342596L;

	private String accountId;

	/**
	 * Constructs through hessian
	 */
	Acct() {
	}

	public Acct(String accountId) {
		if (accountId == null || accountId.trim().length() == 0) {
			throw new IllegalArgumentException("Key to account cannot be null");
		}
		this.accountId = accountId.trim();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		Acct acct = (Acct) o;
		return accountId.equals(acct.accountId);
	}

	@Override
	public int hashCode() {
		return accountId.hashCode();
	}

	@Override
	public String toString() {
		return accountId;
	}

	public static Acct create(String name) {
		if (blank(name)) {
			return null;
		} else {
			return new Acct(name);
		}
	}

	private static boolean blank(String name) {
		return name == null || name.trim().length() == 0;
	}

}

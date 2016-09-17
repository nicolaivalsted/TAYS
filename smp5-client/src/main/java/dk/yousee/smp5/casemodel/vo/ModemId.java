package dk.yousee.smp5.casemodel.vo;

/**
 * Created by IntelliJ IDEA. User: aka Date: Oct 20, 2010 Time: 10:11:07 AM
 * Value object containing the modem Id
 */
public final class ModemId {

	private String id;

	public ModemId(String id) {
		if (id == null || id.trim().length() == 0)
			throw new IllegalArgumentException("ModemId cannot be null");
		this.id = id;
	}

	public String getId() {
		return id;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		ModemId modemId = (ModemId) o;
		return id.equals(modemId.id);
	}

	@Override
	public int hashCode() {
		return id.hashCode();
	}

	@Override
	public String toString() {
		return getId();
	}

	public static ModemId create(String value) {
		if (value != null && value.trim().length() != 0) {
			return new ModemId(value);
		} else {
			return null;
		}
	}

	public static ModemId extract(String externalKey) {
		String tmp = externalKey.substring(externalKey.indexOf("_") + 1);
		return new ModemId(tmp);
	}
}

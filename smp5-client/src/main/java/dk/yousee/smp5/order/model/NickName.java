/**
 * 
 */
package dk.yousee.smp5.order.model;

/**
 * @author m64746
 *
 *         Date: 14/10/2015 Time: 12:07:40
 *         
 *         Service nick name - this is the service name used from a sync/customer view
 */
public class NickName {
	private String name;

	public NickName(String name) {
		if (name == null || name.trim().length() == 0) {
			throw new IllegalArgumentException("Nickname cannot be null");
		}
		this.name = name;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		NickName nickName = (NickName) o;

		return name.equals(nickName.name);

	}

	@Override
	public int hashCode() {
		return name.hashCode();
	}

	@Override
	public String toString() {
		return name;
	}

	public static NickName create(String name) {
		if (blank(name)) {
			return null;
		} else {
			return new NickName(name);
		}
	}

	private static boolean blank(String name) {
		return name == null || name.trim().length() == 0;
	}

}

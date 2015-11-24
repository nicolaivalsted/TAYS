package dk.yousee.smp5.order.model;

import java.util.HashMap;
import java.util.Map;

/**
 * @author m64746
 *
 *         Date: 14/10/2015 Time: 11:42:27
 */
public enum ServiceProviderEnum {
	YouSee("YouSee");

	private String provider;
	private static final Map<String, ServiceProviderEnum> lookup = new HashMap<String, ServiceProviderEnum>();

	static {
		for (ServiceProviderEnum s : ServiceProviderEnum.values()) {
			lookup.put(s.getProvider(), s);
		}
	}

	ServiceProviderEnum(String provider) {
		this.provider = provider;
	}

	public String getProvider() {
		return provider;
	}

	public static ServiceProviderEnum find(String provider) {
		return lookup.get(provider);
	}

	@Override
	public String toString() {
		return getProvider();
	}
}

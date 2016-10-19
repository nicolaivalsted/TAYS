package dk.yousee.smp.order.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: m14857
 * Date: Oct 26, 2010
 * Time: 11:08:52 AM
 * here we have all the provider.
 */
public enum ServiceProviderEnum {

    YouSee("YouSee"),
    Telia("Telia");

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

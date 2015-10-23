package dk.yousee.smp5.order.model;

/**
 * Created by IntelliJ IDEA.
 * User: aka
 * Date: Oct 27, 2010
 * Time: 4:03:33 PM
 * The service prefixes for xml generation of type with service names
 */
public enum ServicePrefix {
    SubSvc("SubSvc"),
    SubSvcSpec("SubSvcSpec"),
    SubContactSpec("SubContactSpec"),
    SubAddressSpec("SubAddressSpec"),
    smp("smp")
    ;



    private String value;

    ServicePrefix(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }
}

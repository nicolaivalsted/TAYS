package dk.yousee.smp.order.model;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: aka
 * Date: Oct 14, 2010
 * Time: 7:58:32 AM
 * Strong type for OrderDataType, like an enum but can be extended, therefore BSS-adapter sees this strong type
 */
public final class OrderDataType implements Serializable {

    static final private long serialVersionUID = -756990801379990454L;

    public static final OrderDataType SERVICE_TYPE_PARENT_SERVICE_KEY = new OrderDataType(ServicePrefix.SubSvc);

    private String type;

    /**
     * For hessian only
     */
    OrderDataType() {
    }

    /**
     * Constructs a type<br/>
     * See: Sigma / Service Profile Manager / Configuration / Service Catalog<br/>
     * Look for value "Service Name" 
     *
     * @param type The value is == SubSvcSpec + "Service Name". <br/>
     * This string is therefore made up of  "stringSpec + "name"<br/>
     * StringSpec is:<br>
     * 1) SubSvcSpec - for most of them See: {link #SUB_SVC_SPEC_PREFIX} <br/>
     * 2) SubContactSpec - for the contact<br/>
     * 3) SubAddressSpec - for the address<br/>
     * 4) (and other values) ...
     */
    public OrderDataType(String type) {
        setType(type);
    }

    /**
     * A constructor that takes the specification and service name in seperate fields
     * @param spec the spec
     * @param serviceName service name
     */
    public OrderDataType(ServicePrefix spec,String serviceName) {
        this(spec+":"+serviceName);
    }

    /**
     * A constructor that takes the specification and service name in seperate fields
     * @param spec the spec
     */
    public OrderDataType(ServicePrefix spec) {
        this(spec.toString());
    }

    public String getType() {
        return type;
    }

    void setType(String type) {
        if(type==null || type.trim().length()==0)throw new IllegalArgumentException("cannot have a null type");
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OrderDataType that = (OrderDataType) o;

        return type.equals(that.type);

    }

    @Override
    public int hashCode() {
        return type.hashCode();
    }

    @Override
    public String toString() {
        return type;
    }

    public static OrderDataType create(String type) {
        if(type==null || type.trim().length()==0)return null;
        return new OrderDataType(type);
    }
}

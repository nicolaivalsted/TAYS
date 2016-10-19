package dk.yousee.smp.order.model;

/**
 * Created by IntelliJ IDEA.
 * User: aka
 * Date: Oct 10, 2010
 * Time: 7:19:10 PM<br/>
 * In XML entityValue has a type=XXXX. This enum is value for XXXX <br/>
 * This value is used under XML generation to decide what XML sub class to use in the xml generation.
 */
public enum OrderDataLevel {
    /**
     * Service plan type "address"
     */
    ADDRESS,
    /**
     * Node for describing contact element.
     */
    CONTACT,
    /**
     * Node describing a service plan
     */
    SERVICE,
    /**
     * Node describing a service plan , but will not show in the orderrequest XML.
     */
    SERVICE_HIDDEN,
    /**
     * Node describing the level under service-plan
     */
    CHILD_SERVICE,
    /**
     * Association between service plans
     */
    SUBSPEC,
    DEVICE,
    QUERY
}

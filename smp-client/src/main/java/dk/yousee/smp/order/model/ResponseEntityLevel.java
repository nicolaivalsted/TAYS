package dk.yousee.smp.order.model;

/**
 * Created by IntelliJ IDEA.
 * User: aka
 * Date: Oct 13, 2010
 * Time: 10:59:28 AM<br/>
 * In XML entityValue has a type=XXXX. This enum is value for XXXX
 */
public enum ResponseEntityLevel {
    /**
     * The top level for a response
     */
    TOP,
    CONTACT,
    ADDRESS,
    /**
     * Node describing a service plan
     */
    SERVICE,
    /**
     * Node describing the level under service-plan
     */
    CHILD_SERVICE
}

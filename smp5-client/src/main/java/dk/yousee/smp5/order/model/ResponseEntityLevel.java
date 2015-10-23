/**
 * 
 */
package dk.yousee.smp5.order.model;

/**
 * @author m64746
 *
 * Date: 13/10/2015
 * Time: 13:50:03
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

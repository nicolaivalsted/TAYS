/**
 * 
 */
package dk.yousee.smp5.order.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import dk.yousee.smp5.order.model.Action;
import dk.yousee.smp5.order.model.OrderDataAssociation;
import dk.yousee.smp5.order.model.OrderDataLevel;
import dk.yousee.smp5.order.model.OrderDataType;
import dk.yousee.smp5.order.model.ProvisionStateEnum;

/**
 * @author m64746
 *
 *         Date: 14/10/2015 Time: 12:13:30
 */
public class OrderData implements Serializable {

	private static final long serialVersionUID = 5097814180866564269L;
	private OrderDataLevel level;
	/**
	 * Action to be performed on service
	 */
	private Action action;
	/**
	 * State returned from SMP
	 */
	private ProvisionStateEnum state;
	/**
	 * If instance key in smp is known, put it here.
	 */
	private String externalKey;
	/**
	 * Value of the parameter, parameter name is put in externalkey , for
	 * services value contains service type as defined in Constants
	 */
	private OrderDataType type;

	private OrderData Parent;
	
	private List<OrderData> children = new ArrayList<OrderData>();

	private List<OrderDataAssociation> associations = null;
	private Map<String, String> params;

	/**
	 * If the feature that is to be created using the data stored in the
	 * orderData element the external key is the name of the feature and the
	 * value is the value of the feature. E.g. externalkey=phones.cell.number
	 * and value=9090909090
	 *
	 * @param level
	 *            DEVICE, SERVICE, QUERY or ADDRESS
	 * @param action
	 *            , provisioning action (activate, deactivate, suspend or
	 *            resume))
	 * @param externalKey
	 *            (Id with which service or device is identified in systems
	 *            external to SMP)
	 * @param type
	 *            , the type of the element.
	 */
	public OrderData(OrderDataLevel level, Action action, String externalKey,
			OrderDataType type) {
		this();
		setLevel(level);
		setAction(action);
		setExternalKey(externalKey);
		setType(type);
	}

	public List<OrderDataAssociation> getAssociations() {
		if (associations == null) {
			associations = new ArrayList<OrderDataAssociation>();
		}
		return associations;
	}

	public void setAssociations(List<OrderDataAssociation> associations) {
		this.associations = associations;
	}

	public OrderData getParent() {
		return Parent;
	}

	public void setParent(OrderData parent) {
		Parent = parent;
	}

	/**
	 * Constructor only of hessian etc.
	 */
	public OrderData() {
	}

	public void setState(ProvisionStateEnum stat) {
		this.state = stat;
	}

	public ProvisionStateEnum getState() {
		return this.state;
	}

	public void setAction(Action anAction) {
		this.action = anAction;
	}

	public Action getAction() {
		return this.action;
	}

	/**
	 * Set the type of the object, SERVICE, CHILD_SERVICE, FEATURE
	 * 
	 * @param level
	 *            the new type of the orderdata element
	 */
	public void setLevel(OrderDataLevel level) {
		this.level = level;
	}

	/**
	 * Get the type of the object, SERVICE, CHILD_SERVICE, FEATURE
	 * 
	 * @return type the type of the orderData element
	 */
	public OrderDataLevel getLevel() {
		return level;
	}

	/**
	 * If instance key in smp is known, put it here. For features put the
	 * feature name in here.
	 *
	 * @param externalKey
	 *            Feature name or service externalKey. If null in order a new
	 *            external ID is generated.
	 */
	public void setExternalKey(String externalKey) {
		this.externalKey = externalKey;
	}

	public String getExternalKey() {
		return externalKey;
	}

	public Map<String, String> getParams() {
		if (params == null)
			params = new TreeMap<String, String>();
		return params;
	}

	public void setParams(Map<String, String> params) {
		this.params = params;
	}

	/**
	 * Sets the orderdata children below this object. Could be child services or
	 * service parameters.
	 *
	 * @param children
	 *            new list of children to replace current list of children
	 */
	public void setChildren(List<OrderData> children) {
		this.children = children;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see dk.yousee.smp.order.model.OrderDataInterface#getChildren()
	 */
	public List<OrderData> getChildren() {
		return children;
	}

	/**
	 * Get the value of the parameter. If the orderData object represents a
	 * service, the value contains the service type. eg. mobile broadband,
	 * mobile broadband service attributes etc.
	 * 
	 * @return value of OrderData element
	 */
	public OrderDataType getType() {
		return type;
	}

	/**
	 * Sets the Value of a feature or the service type as defined in Constants.
	 * The value attribute is to be used in conjunction with the type atttribute
	 * to determine which level in an order it applies to and what actions to be
	 * taken when processing the OrderData-item.
	 *
	 * @param type
	 *            the avlue to be set
	 */
	public void setType(OrderDataType type) {
		this.type = type;
	}

	/**
	 * Append a single child to the existing child service list. The action of
	 * the additional child will inherit the action of this element if it does
	 * not have an action itself.
	 *
	 * @param child
	 *            The new child element to be added to the children list.
	 */
	public void addChild(OrderData child) {
		if (child != null) {
			if (children == null) {
				children = new ArrayList<OrderData>();
			}
			children.add(child);
		}
	}

	/**
	 * Append the elements in the list to the items in the children list. If
	 * newChildren is null nothing happens, if children is null it is
	 * overwritten with newChildren. The new children will inherit the action of
	 * this element, but new "grandchildren" of this element will not inherit
	 * the action.
	 *
	 * @param newChildren
	 *            List of elements to be added to this OrderData elements
	 *            children list.
	 */
	public void addChildren(List<OrderData> newChildren) {
		if ((getChildren() != null) && (newChildren != null)) {
			for (OrderData o : newChildren) {
				getChildren().add(o);
			}
		} else if (newChildren != null) {
			children = newChildren;
		}
	}
}

/**
 * 
 */
package dk.yousee.smp5.order.model;

import java.io.Serializable;

import dk.yousee.smp5.order.model.OrderDataType;

/**
 * @author m64746
 *
 *         Date: 13/10/2015 Time: 15:25:04
 */
public class ResponseAssociation implements Serializable {

	private static final long serialVersionUID = -5194818953202256746L;

	private String associationType;

	private OrderDataType type;

	private String primaryKey;

	public String getAssociationType() {
		return associationType;
	}

	public void setAssociationType(String associationType) {
		this.associationType = associationType;
	}

	public OrderDataType getType() {
		return type;
	}

	public void setType(OrderDataType type) {
		this.type = type;
	}

	public String getPrimaryKey() {
		return primaryKey;
	}

	public void setPrimaryKey(String primaryKey) {
		this.primaryKey = primaryKey;
	}

}

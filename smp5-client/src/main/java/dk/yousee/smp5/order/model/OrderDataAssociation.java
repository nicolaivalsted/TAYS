/**
 * 
 */
package dk.yousee.smp5.order.model;

import java.io.Serializable;

import dk.yousee.smp5.order.model.OrderDataType;
import dk.yousee.smp5.order.model.Smp5ManipulationAction;

/**
 * @author m64746
 *
 *         Date: 14/10/2015 Time: 12:17:02
 * 
 *         Association to be added / updated / deleted from child-service
 */
public class OrderDataAssociation implements Serializable {

	private static final long serialVersionUID = 8755997953140325604L;

	private String associationType;

	private OrderDataType type;

	private String externalKey;

	private Smp5ManipulationAction action;

	public Smp5ManipulationAction getAction() {
		return action;
	}

	public void setAction(Smp5ManipulationAction action) {
		this.action = action;
	}

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

	public String getExternalKey() {
		return externalKey;
	}

	public void setExternalKey(String externalKey) {
		this.externalKey = externalKey;
	}

	@Override
	public String toString() {
		final StringBuffer sb = new StringBuffer();
		sb.append("OrderDataAssociation");
		sb.append("{associationType='").append(associationType).append('\'');
		sb.append(", type=").append(type);
		sb.append(", externalKey='").append(externalKey).append('\'');
		sb.append(", action=").append(action);
		sb.append('}');
		return sb.toString();
	}

}

package dk.yousee.smp.order.model;

import java.io.Serializable;

/**
 * Created by IntelliJ IDEA.
 * User: aka
 * Date: Oct 22, 2010
 * Time: 2:09:00 PM
 * Association to be added / updated / deleted from child-service
 */
public class OrderDataAssociation implements Serializable {
    static final private long serialVersionUID = -756965011379990454L;

    private String associationType;

    private OrderDataType type;

    private String externalKey;

    private SmpManipulationAction action;

    public SmpManipulationAction getAction() {
        return action;
    }

    public void setAction(SmpManipulationAction action) {
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

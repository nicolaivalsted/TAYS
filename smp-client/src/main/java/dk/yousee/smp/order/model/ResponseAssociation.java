package dk.yousee.smp.order.model;

import java.io.Serializable;


/**
 * Created by IntelliJ IDEA.
 * User: aka
 * Date: Oct 13, 2010
 * Time: 10:44:51 AM<br/>
 * Assosiation in a response
 */
public final class ResponseAssociation implements Serializable {
    static final private long serialVersionUID = -756965011379990454L;


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

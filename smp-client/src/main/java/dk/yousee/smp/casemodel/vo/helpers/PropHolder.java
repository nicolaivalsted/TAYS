package dk.yousee.smp.casemodel.vo.helpers;

import dk.yousee.smp.order.model.SystemException;

/**
 * Created by IntelliJ IDEA.
 * User: aka
 * Date: Oct 15, 2010
 * Time: 4:59:45 PM
 * Class that hold properties
 */
public class PropHolder {

    private String key;
    private BasicUnit unit;
    private boolean mandatory = false;

    public PropHolder(BasicUnit unit, String key) {
        this.unit = unit;
        this.key = key;
    }

    public PropHolder(BasicUnit unit, String key, boolean mandatory) {
        this.unit = unit;
        this.key = key;
        this.mandatory = mandatory;
    }


    public String getValue() {
        String value = null;
        if (unit.isDefaultOrderDataExist()) {
            value = unit.getValueByKeyInDefaultOrderData(key);
        }
        if (value != null) {
            return value;
        } else {
            return unit.getValueByKeyInResponse(key);
        }
    }
    
    public boolean hasValue(){
        String value = getValue();
        return value != null && value.trim().length() != 0;
    }

    /**
     * Assigns a value to a parameter on the order
     * postcondition: OrderData created or updated, ready for sending to Sigma.
     *
     * @param value to assign
     */
    public void setValue(String value) {
        if (mandatory) {
            if (value == null) throw new SystemException("cannot assign null to mandatory field. Key=%s" + key);
        }
        unit.assignValueToKey(key, value);
    }

    /**
     * Clears the field.
     * There is a strategy for signaling clear to SMP ... this should be build in here.
     * A space for now ;-)
     */
    public void clearValue() {
        setValue(" ");
    }
    /**
     * Update the value of property, if the value is identical to existing value no order is produced
     *
     * @param value to assign to "param" - might be new might be identical to the existing value. NEVER null.
     * @return true when update resulted in order update
     */
    public boolean updateValue(String value) {
        if (value == null) throw new SystemException("Cannot update a value to null. Key=%s" + key);
        boolean res = false;
        String rv = getValue();
        if (!value.equals(rv)) {
            setValue(value);
            res = true;
        }
        return res;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("{key='").append(key).append('\'');
        String value = getValue();
        if(value!=null)sb.append(", value=").append(value);
        sb.append('}');
        return sb.toString();
    }
}

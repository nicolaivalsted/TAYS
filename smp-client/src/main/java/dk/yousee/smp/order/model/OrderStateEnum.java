package dk.yousee.smp.order.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: m14857
 * Date: Mar 31, 2011
 * Time: 11:19:47 AM
 * The Enum type for different state.
 */
public enum OrderStateEnum {

    OPEN("open", 1),
    OPEN_NOT_RUNNING("open.not_running", 2),
    OPEN_NOT_RUNNING_NOT_STARTED("open.not_running.not_started", 3),
    OPEN_NOT_RUNNING_SUSPENDED("open.not_running.suspended", 4),
    OPEN_RUNNING("open.running", 5),
    CLOSED("closed", 6),
    CLOSED_COMPLETED("closed.completed", 7),
    CLOSED_ABORTED("closed.aborted", 8),
    CLOSED_ABORTED_BYCLIENT("closed.aborted.byclient", 9),
    CLOSED_ABORTED_BYSERVER("closed.aborted.byserver", 10);

    private String type;
    private int typeInt;

    private static final Map<String, OrderStateEnum> lookuptype = new HashMap<String, OrderStateEnum>();

    static {
        for (OrderStateEnum s : OrderStateEnum.values()) {
            lookuptype.put(s.getType(), s);
        }
    }

    private static final Map<Integer, OrderStateEnum> lookuptypeInt = new HashMap<Integer, OrderStateEnum>();

    static {
        for (OrderStateEnum s : OrderStateEnum.values()) {
            lookuptypeInt.put(s.getTypeInt(), s);
        }
    }

    public String getType() {
        return type;
    }

    public int getTypeInt() {
        return typeInt;
    }

    OrderStateEnum(String type, int typeInt) {
        this.type = type;
        this.typeInt = typeInt;
    }

    public static OrderStateEnum findOrderStateEnumByType(String type) {
        return lookuptype.get(type);
    }

    public static OrderStateEnum findOrderStateEnumByType(int typeInt) {
        return lookuptypeInt.get(typeInt);
    }

}

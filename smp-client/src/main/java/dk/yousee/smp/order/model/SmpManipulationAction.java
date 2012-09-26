package dk.yousee.smp.order.model;

/**
 * Created by IntelliJ IDEA.
 * User: aka
 * Date: Oct 11, 2010
 * Time: 10:33:41 PM
 * These are the manipulations that can be in an action order to Sigma.
 * See Order Manager XML API guide page 30 "Action Order"
 */
public enum SmpManipulationAction {
    ADD("add"),
    DELETE("delete"),
    UPDATE("update");



    private String value;

    SmpManipulationAction(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }
    /*
    if (Action.ACTIVATE==action) return "add";
    if (Action.DEACTIVATE==action) return "delete";
    if (Action.RESUME==action) return "update";
    if (Action.UPDATE==action) return "update";
    if (Action.SUSPEND==action) return "update";
    if (Action.BLOCK==action) return "update";
    if (Action.SWAP==action) return "update";
    if (Action.DELETE==action) return "delete";
*/
}

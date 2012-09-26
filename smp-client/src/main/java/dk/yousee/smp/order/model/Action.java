package dk.yousee.smp.order.model;

/**
 * Created by IntelliJ IDEA.
 * User: aka
 * Date: Oct 10, 2010
 * Time: 9:19:43 AM
 * Various different actions it is possible to perform on a node
 */
public enum Action {
    /**
     * Create New
     */
    ACTIVATE("ACTIVATE",SmpManipulationAction.ADD),

    DEACTIVATE("DEACTIVATE",SmpManipulationAction.DELETE),
    /**
     * Resume from suspend
     */
    RESUME("RESUME",SmpManipulationAction.UPDATE),
    UPDATE("UPDATE",SmpManipulationAction.UPDATE),
    /**
     * Suspend (courtesy block)
     */
    SUSPEND("SUSPEND",SmpManipulationAction.UPDATE),
    /**
     * Suspend (mso block)
     */
    BLOCK("BLOCK",SmpManipulationAction.UPDATE),
    /**
     *  Swap sim card for Mobb
     */
    SWAP("SWAP",SmpManipulationAction.UPDATE),
    /**
     * Delete service
     */
    DELETE("DELETE",SmpManipulationAction.DELETE),
    /**
     * State is changinginternally in smp
     */
    CHANGE_IN_PROGRESS("CHANGE_IN_PROGRESS",null),
    /**
     * Addresses are provisionied inactive on subscriber creation
     */
    INACTIVE("ACTION_INACTIVE",null)
    ,ACTION_FIND_SERVICE("ACTION_FIND_SERVICE",null)
    ;

    private String clientAction;
    private SmpManipulationAction smpAction;

    Action(String clientAction, SmpManipulationAction smpAction) {
        this.clientAction = clientAction;
        this.smpAction = smpAction;
    }

    public String getClientAction() {
        return clientAction;
    }

    public SmpManipulationAction getSmpAction() {
        return smpAction;
    }
}

package dk.yousee.smp.order.model;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: aka
 * Date: Oct 12, 2010
 * Time: 12:46:20 PM
 * Possible provisioning states<br/>
 * See: OrderManagerXMLAPIGuide.pdf page: 24
 *
 */
public enum ProvisionStateEnum {
    ACTIVE("active"),
    DELETED("deleted"),
    INACTIVE("inactive"),
    MSO_BLOCK("mso_block"),
    /**
     * Blocked is going on
     */
    COURTESY_BLOCK_IN_PROGRESS("courtesy_block_in_progress",true),
    /**
     * Blocked
     */
    COURTESY_BLOCK("courtesy_block"),
    /**
     * Internally state when action in progress
     */
    CHANGE_IN_PROGRESS("change_in_progress",true),
    /**
     * State used in subscriber service state
     */
    ADD_IN_PROGRESS("add_in_progress",true),
    DELETE_IN_PROGRESS("delete_in_progress",true),
    CLOSED_COMPLETED_ALL("closed.completed.all"),
    STATUS_CODE_FUTURE_ACTION("open.not_running.pre_queued.scheduled"),
    STATUS_CODE_OPEN_NOT_RUNNING("open.not_running.not_started"),
    OPEN_NOT_RUNNING_QUEUED("open.not_running.queued",true),
    OPEN_RUNNING("open.running",true),
    /**
     * This state is used when state could not be passed, there is also a warn logging on the server with content
     */
    BUG_IN_BSS_ADAPTER("bug_in_bss_adapter")
    ;

    private String state;
    private boolean progress=false;

    private static final Map<String, ProvisionStateEnum> lookup = new HashMap<String, ProvisionStateEnum>();

    static {
        for (ProvisionStateEnum s : ProvisionStateEnum.values()) {
            lookup.put(s.getState(), s);
        }
    }

    ProvisionStateEnum(String state) {
        this.state = state;
    }

    ProvisionStateEnum(String state, boolean progress) {
        this.state = state;
        this.progress = progress;
    }

    public String getState() {
        return state;
    }

    public static ProvisionStateEnum find(String state) {
        return lookup.get(state);
    }

    /**
     * Is this state of type "progress"
     * @return true means yes
     */
    public boolean isProgress() {
        return progress;
    }

    @Override
    public String toString() {
        return getState();
    }
}

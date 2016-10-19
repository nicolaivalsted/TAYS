package dk.yousee.smp.com;

import dk.yousee.smp.order.model.Action;
import dk.yousee.smp.order.model.SmpManipulationAction;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: aka
 * Date: Oct 12, 2010
 * Time: 9:03:45 AM
 * Convert to SmpActions
 */
public class SmpManipulationActionFactory {
    private static final Map<Action, SmpManipulationAction> table=new HashMap<Action, SmpManipulationAction>();

    static {
        table.put(Action.ACTIVATE, SmpManipulationAction.ADD);
        table.put(Action.DEACTIVATE, SmpManipulationAction.DELETE);
        table.put(Action.RESUME, SmpManipulationAction.UPDATE);
        table.put(Action.UPDATE, SmpManipulationAction.UPDATE);
        table.put(Action.SUSPEND, SmpManipulationAction.UPDATE);
        table.put(Action.BLOCK, SmpManipulationAction.UPDATE);
        table.put(Action.SWAP, SmpManipulationAction.UPDATE);
        table.put(Action.DELETE, SmpManipulationAction.DELETE);
    }

	public SmpManipulationAction toAction(Action action) {
        SmpManipulationAction res;
        res=table.get(action);
		if(res==null)throw new IllegalArgumentException("Unknown action: " + action);
        return res;
	}
}

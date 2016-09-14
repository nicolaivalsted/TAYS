package dk.yousee.smp5.com;

import java.util.HashMap;
import java.util.Map;

import dk.yousee.smp5.order.model.Smp5ManipulationAction;
import dk.yousee.smp.order.model.SmpManipulationAction;
import dk.yousee.smp5.order.model.Action;

/**
 * @author m64746
 *
 *         Date: 20/10/2015 Time: 09:59:12
 */
public class Smp5ManipulationActionFactory {
	private static final Map<Action, Smp5ManipulationAction> table = new HashMap<Action, Smp5ManipulationAction>();

	static {
		table.put(Action.ACTIVATE, Smp5ManipulationAction.ADD);
		table.put(Action.DEACTIVATE, Smp5ManipulationAction.DELETE);
		table.put(Action.RESUME, Smp5ManipulationAction.UPDATE);
		table.put(Action.UPDATE, Smp5ManipulationAction.UPDATE);
		table.put(Action.SUSPEND, Smp5ManipulationAction.UPDATE);
	    table.put(Action.BLOCK, Smp5ManipulationAction.UPDATE);
		table.put(Action.REPROV, Smp5ManipulationAction.REPROV);
		table.put(Action.SWAP, Smp5ManipulationAction.UPDATE);
        table.put(Action.DELETE, Smp5ManipulationAction.DELETE);
	}

	public Smp5ManipulationAction toAction(Action action) {
		Smp5ManipulationAction res;
		res = table.get(action);
		if (res == null) {
			throw new IllegalArgumentException("Unknown action: " + action);
		}
		return res;
	}
}

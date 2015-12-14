package dk.yousee.smp5.order.model;

/**
 * @author m64746
 *
 *         Date: 13/10/2015 Time: 15:19:47
 */
public enum Action {
	/**
	 * Create New
	 */
	ACTIVATE("ACTIVATE", Smp5ManipulationAction.ADD),

	DEACTIVATE("DEACTIVATE", Smp5ManipulationAction.DELETE),
	/**
	 * Resume from suspend
	 */
	RESUME("RESUME", Smp5ManipulationAction.UPDATE), 
	UPDATE("UPDATE", Smp5ManipulationAction.UPDATE),
	/**
	 * Suspend (courtesy block)
	 */
	SUSPEND("SUSPEND", Smp5ManipulationAction.UPDATE),
	/**
	 * Suspend (mso block)
	 */
	BLOCK("BLOCK", Smp5ManipulationAction.UPDATE),
	/**
	 * Swap sim card for Mobb
	 */
	SWAP("SWAP", Smp5ManipulationAction.UPDATE),
	/**
	 * Delete service
	 */
	DELETE("DELETE", Smp5ManipulationAction.DELETE),
	/**
	 * State is changinginternally in smp
	 */
	CHANGE_IN_PROGRESS("CHANGE_IN_PROGRESS", null),
	/**
	 * Addresses are provisionied inactive on subscriber creation
	 */
	INACTIVE("ACTION_INACTIVE", null), 
	REPROV("refresh_stb", Smp5ManipulationAction.REPROV),
	ACTION_FIND_SERVICE("ACTION_FIND_SERVICE", null);

	private String clientAction;
	private Smp5ManipulationAction smpAction;

	Action(String clientAction, Smp5ManipulationAction smpAction) {
		this.clientAction = clientAction;
		this.smpAction = smpAction;
	}

	public String getClientAction() {
		return clientAction;
	}

	public Smp5ManipulationAction getSmpAction() {
		return smpAction;
	}
}

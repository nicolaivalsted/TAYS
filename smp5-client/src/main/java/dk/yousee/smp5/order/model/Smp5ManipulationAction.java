package dk.yousee.smp5.order.model;

/**
 * Created by IntelliJ IDEA. User: aka Date: Oct 11, 2010 Time: 10:33:41 PM
 * These are the manipulations that can be in an action order to Sigma. See
 * Order Manager XML API guide page 30 "Action Order"
 */
public enum Smp5ManipulationAction {
	ADD("add"), 
	DELETE("delete"), 
	UPDATE("update");

	private String value;

	Smp5ManipulationAction(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}

	@Override
	public String toString() {
		return value;
	}

}
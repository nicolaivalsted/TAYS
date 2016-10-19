package dk.yousee.smp.order.model;

/**
 * Created by IntelliJ IDEA. User: aka Date: Oct 25, 2010 Time: 2:44:46 PM You
 * got an error that is related to the access to the system,.down services, etc
 */
public class SystemException extends RuntimeException {
	private static final long serialVersionUID = 727221627646651624L;

	public SystemException(String message, Object... args) {
		super(String.format(message, args));
	}

	public SystemException(String message) {
		super(message);
	}
}

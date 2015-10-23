package dk.yousee.smp5.order.model;


public class SystemException extends RuntimeException {
	public SystemException(String message, Object... args) {
		super(String.format(message, args));
	}

	public SystemException(String message) {
		super(message);
	}
}

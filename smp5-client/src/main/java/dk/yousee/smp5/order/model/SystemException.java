package dk.yousee.smp5.order.model;

public class SystemException extends RuntimeException {
	private static final long serialVersionUID = 2055310894539420424L;

	public SystemException(String message, Object... args) {
		super(String.format(message, args));
	}

	public SystemException(String message) {
		super(message);
	}
}

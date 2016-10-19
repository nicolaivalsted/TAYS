package dk.yousee.smp.order.model;

/**
 * Created by IntelliJ IDEA.
 * User: aka
 * Date: Oct 25, 2010
 * Time: 2:42:34 PM
 * A business exception is an exception that is related to your action, You have wrong keys etc.
 */
public class BusinessException extends Exception {

	private static final long serialVersionUID = 6795869993018836479L;
	public BusinessException(String message, Object ... args) {
        super(String.format(message,args));
    }
    public BusinessException(String message) {
        super(message);
    }
}

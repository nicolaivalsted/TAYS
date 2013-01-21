package dk.yousee.randy.messaging;

/**
 *
 * @author m27236
 */
public class MessagingException extends Exception{

    public MessagingException() {
    }

    public MessagingException(String message) {
        super(message);
    }

    public MessagingException(String message, Throwable cause) {
        super(message, cause);
    }
    
}

package dk.yousee.randy.jobmonclient;

/**
 *
 * @author jablo
 */
public class RestException extends Exception {

    public RestException(String reasonPhrase) {
        super(reasonPhrase);
    }

    public RestException(Throwable thrwbl) {
        super(thrwbl);
    }

    public RestException(String string, Throwable thrwbl) {
        super(string, thrwbl);
    }

    public RestException() {
    }
    
}

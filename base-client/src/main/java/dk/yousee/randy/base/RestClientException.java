/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.yousee.randy.base;

/**
 *
 * @author jablo
 */
public class RestClientException extends Exception {
    private int httpStatusCode;

    public RestClientException() {
    }

    public RestClientException(String string) {
        super(string);
    }

    public RestClientException(String string, Throwable thrwbl) {
        super(string, thrwbl);
    }

    public RestClientException(Throwable thrwbl) {
        super(thrwbl);
    }

    public RestClientException(int httpStatusCode) {
        this.httpStatusCode = httpStatusCode;
    }

    public RestClientException(int httpStatusCode, String string) {
        super(string);
        this.httpStatusCode = httpStatusCode;
    }

    public RestClientException(int httpStatusCode, String string, Throwable thrwbl) {
        super(string, thrwbl);
        this.httpStatusCode = httpStatusCode;
    }

    public RestClientException(int httpStatusCode, Throwable thrwbl) {
        super(thrwbl);
        this.httpStatusCode = httpStatusCode;
    }

    public int getHttpStatusCode() {
        return httpStatusCode;
    }

    public void setHttpStatusCode(int httpStatusCode) {
        this.httpStatusCode = httpStatusCode;
    }
}

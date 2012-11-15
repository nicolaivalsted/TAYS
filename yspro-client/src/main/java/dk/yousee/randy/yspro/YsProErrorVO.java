package dk.yousee.randy.yspro;

/**
 *
 * @author m27236
 */
public class YsProErrorVO {
    
    private int httpStatus;
    
    private String body;

    public YsProErrorVO(int httpStatus, String body) {
        this.httpStatus = httpStatus;
        this.body = body;
    }

    public int getHttpStatus() {
        return httpStatus;
    }

    public String getBody() {
        return body;
    }
}

package dk.yousee.randy.yspro;

/**
 *
 * @author m27236
 */
public class YsProException extends Exception {

    private YsProErrorVO errorVo;

    public YsProException(YsProErrorVO errorVo) {
        this.errorVo = errorVo;
    }

    public YsProException(String message) {
        super(message);
    }

    public YsProException(String message, Throwable cause) {
        super(message, cause);
    }

    public YsProErrorVO getErrorVo() {
        return errorVo;
    }
    
    public boolean isYsProError(){
        return errorVo !=null;
    }

}

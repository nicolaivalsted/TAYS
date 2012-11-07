package dk.yousee.randy.voucher;

/**
 * User: aka
 * Date: 07/11/12
 * Time: 09.50
 * Value object containing a voucher
 */
public class Voucher {

    String voucher;

    public Voucher(String voucher) {
        if(voucher==null || voucher.trim().length()==0){
            throw new IllegalArgumentException("voucher cannot be null");
        }
        this.voucher = voucher;
    }

    /**
     * Verifies that voucher is syntax correct
     * @return null means success, a string means the error description
     */
    public String verify(){
        int len=voucher.length();
        if(len!=12){
            return String.format("forkert laengde %s",len);
        } else {
            boolean val=Verhoeff.validateVerhoeff(voucher);
            return val?null:"syntax fejl";
        }
    }

    @Override
    public String toString() {
        return voucher;
    }

    public static Voucher create(String txt) {
        return new Voucher(txt);
    }
}

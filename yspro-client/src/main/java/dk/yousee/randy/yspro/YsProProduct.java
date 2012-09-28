package dk.yousee.randy.yspro;

/**
 * Created with IntelliJ IDEA.
 * User: aka
 * Date: 07/09/12
 * Time: 11.27
 * A product that YS pro store service plans by. I.e: 7000
 */
public class YsProProduct {

    private String id;

    public YsProProduct(String id) {
        if(blank(id))throw new IllegalArgumentException("YsProProduct is never blank");
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        YsProProduct that = (YsProProduct) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return id;
    }

    public static YsProProduct create(String product){
        if(blank(product)){
           return null;
        } else {
            return new YsProProduct(product);
        }
    }

    private static boolean blank(String product) {
        return product==null || product.trim().length()==0;
    }
}

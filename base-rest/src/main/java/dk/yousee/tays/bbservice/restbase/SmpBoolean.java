package dk.yousee.tays.bbservice.restbase;

/**
 * Class for converting smp y / n values to the real deal.
 * @author m27236
 */
public class SmpBoolean {
    
    
    public static boolean parse(String str) {
        return ((str != null) && str.equalsIgnoreCase("y"));
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.yousee.randy.logging;

/**
 *
 * @author Jacob Lorensen, TDC, December 2013.
 */
public class ObscureFormatter implements ValueFormatter {
    @Override
    public String format(String arg) {
        return arg != null ? "********" : arg ;
    }
}

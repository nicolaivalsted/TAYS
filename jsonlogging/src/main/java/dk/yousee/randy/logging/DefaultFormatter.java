/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.yousee.randy.logging;

/**
 *
 * @author jablo
 */
public class DefaultFormatter implements ValueFormatter {
    @Override
    public String format(String arg) {
        return arg;
    }
}

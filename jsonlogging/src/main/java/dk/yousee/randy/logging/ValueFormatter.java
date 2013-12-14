/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.yousee.randy.logging;

/**
 * Format discrete atomic values in log file for consistency in log files.
 * Examples include
 * <ul>
 * <li>Formatting IP and MAC addresses consistently
 * <li>Floating point numbers
 * <li>Subscriber numbers
 * <li>&hellip;
 * </ul>
 *
 * @author Jacob Lorensen, TDC, December 2013
 */
public interface ValueFormatter {
    String format(String arg);
}

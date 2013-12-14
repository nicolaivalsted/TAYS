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
 * This formatter is not for structured data. Structured data would be present
 * in the http request or response entity, both of which are handled separately
 * and passed unchanged directly through to the log formatter, thus ultimately
 * being passed through the war project's standard object serialization process.
 * <p>
 * Specifically, changing the return type to Object&mdash;thus producing a
 * multivariate method&mdash;means the resulting json will not be acccepted into
 * Kibana/logstash<p>
 * Enquiries are proceeding as to what the actual formatting problem <i>is</i>
 *
 * @author Jacob Lorensen, TDC, December 2013
 */
public interface ValueFormatter {
    String format(String arg);
}

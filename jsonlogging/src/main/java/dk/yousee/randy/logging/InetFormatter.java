/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.yousee.randy.logging;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Parse and re-format ip addresses so they appear in a consistent format 
 * in all logs.
 * @author Jacob Lorensen, TDC, December 2013.
 */
public class InetFormatter implements ValueFormatter {
    @Override
    public String format(String arg) {
        if (arg == null)
            return null;
        InetAddress ip;
        try {
            ip = InetAddress.getByName(arg);
        } catch (UnknownHostException ex) {
            return arg;
        }
        if (ip == null)
            return arg;
        return ip.getHostAddress();
    }
}

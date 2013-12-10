/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.yousee.randy.logging;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 *
 * @author jablo
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

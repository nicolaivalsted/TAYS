/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.yousee.randy.logging;

import dk.yousee.randy.macaddress.MACaddress;
import org.springframework.stereotype.Component;

/**
 * Parse and re-format MAC addresses so they appear in a consistent format throughout the json log files.
 * @author Jacob Lorensen, TDC, December 2013.
 */
@Component
public class MACformatter implements ValueFormatter {
    @Override
    public String format(String arg) {
        if (arg == null)
            return null;
        MACaddress mac = MACaddress.parseMACaddress(arg);
        if (mac == null)
            return arg;
        return mac.toString(MACaddress.fmtCOLON);
    }
}

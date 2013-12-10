/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.yousee.randy.logging;

import dk.yousee.randy.macaddress.MACaddress;
import org.springframework.stereotype.Component;

/**
 *
 * @author jablo
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

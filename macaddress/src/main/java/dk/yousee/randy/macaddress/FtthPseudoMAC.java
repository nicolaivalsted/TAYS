/*******
 * $Id: FtthPseudoMAC.java,v 1.2 2009-05-04 13:51:27 jablo Exp $
 * $Name: not supported by cvs2svn $
 *
 * Jacob Lorensen, TDC KabelTV, 2007
 */
package dk.yousee.randy.macaddress;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * FTTH Pseudo MAC address. Port number and module number is encoded in pseudo
 * MAC addresses.
 * 
 * @see "CNR Extension for Fiber To The Home solution in KTV, FTTH.tcl"
 * @author Jacob Lorensen, TDC KabelTV, 2007
 */
public class FtthPseudoMAC extends MACaddress implements Cloneable {
    public FtthPseudoMAC(byte[] bytes) {
        this.bytes = bytes;
    }

    /**
     * Create an FtthPseudoMac from a MAC address represented in a String
     * 
     * @param s
     *                the MAC address as a String
     */
    public FtthPseudoMAC(String s) {
        this(parseFtthPseudoMAC(s));
    }

    /**
     * Create a FtthPseudoMAC address from an existing MAC addres
     */
    public FtthPseudoMAC(MACaddress mac) {
        bytes = mac.bytes;
    }

    /**
     * Create a FtthPseudoMAC address from a String
     */
    public static FtthPseudoMAC parseFtthPseudoMAC(String s) {
        return new FtthPseudoMAC(MACaddress.parseMACaddress(s, fmtCOLON));
    }

    /**
     * Create a FtthPseudoMAC address from a String
     */
    public static FtthPseudoMAC parseFtthPseudoMAC(String s, int defaultFormat) {
        return new FtthPseudoMAC(MACaddress.parseMACaddress(s, defaultFormat));
    }

    /**
     * Convert to default String format (not parse-able)
     */
    public String toString() {
        String s;
        s = "P" + getEncodedPortNumber() + "M" + getEncodedModuleNumber() + ",";
        s += Integer.toHexString(getByte(1) & 0x0F);
        for (int i = 2; i < 6; i++) {
            s += ":";
            if ((getByte(i) & 0xFF) < 16)
                s += "0";
            s += Integer.toHexString(getByte(i) & 0xFF);
        }
        return s;
    }

    /**
     * Get the port encoded number
     * 
     * @return the port number decoded
     */
    public int getEncodedPortNumber() {
        return (bytes[0] >> 1) & 0x7F;
    }

    /**
     * Set the port encoded number
     * 
     * @param port
     *                the port number
     */
    public void setEncodedPortNumber(int port) {
        if (port < 0 || 127 < port)
            throw new IllegalArgumentException("Ports numbers must be between 0 and 127");
        bytes[0] = (byte) (((port << 1) | 1) & 0xFF);
    }

    /**
     * Get the module or stack number
     * 
     * @return the module or stack number decoded from the pseudo MAC
     *         address
     */
    public int getEncodedModuleNumber() {
        return ((bytes[1] & 0xF0) >> 4) & 0xFF;
    }

    /**
     * Set the module or stack number
     * 
     * @param module
     *                the module number
     */
    public void setEncodedModuleNumber(int module) {
        if (module < 0 || 15 < module)
            throw new IllegalArgumentException("Module number must be between 0 and 15");
        bytes[1] &= 0x0F;
        bytes[1] |= ((module & 0x0F) << 4) & 0xFF;
    }

    public static class Test {
        public static void main(String[] args) {
            BufferedReader inp = new BufferedReader(new InputStreamReader(System.in));
            String s;
            System.out.println("Indtast MAC adr: ");
            FtthPseudoMAC mac;
            while (true) {
                try {
                    s = inp.readLine();
                } catch (IOException e) {
                    s = null;
                }
                if (s == null) {
                    System.out.println("exit");
                    return;
                }
                mac = FtthPseudoMAC.parseFtthPseudoMAC(s);
                if (mac == null)
                    System.out.println("mac is null, illegal format");
                else {
                    for (int i = 0; i < 4; i++)
                        System.out.println("Format: " + i + ": " + mac.toString(i));
                    System.out.println("Ftth: " + mac.toString());
                }
            }
        }
    }
}

/*******
 * $Id: MACaddress.java,v 1.6 2009-12-03 09:49:22 jablo Exp $
 * $Name: not supported by cvs2svn $
 *
 * Jacob Lorensen, TDC KabelTV, 2007
 */
package dk.yousee.randy.macaddress;

import java.io.IOException;
import java.io.Serializable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
//import java.util.logging.Logger;

/**
 * MAC address class.
 * @author Jacob Lorensen, TDC KabelTV, 2007
 */
public class MACaddress implements Serializable, Cloneable {
    private int defaultFormat;
    /**
     * Normal "colon" format of MAC addresses, e.g. 11:22:33:44:55:66
     */
    public final static int fmtCOLON = 0;
    /**
     * By convention the format used for mac addresses in STALONE
     */
    public final static int fmtSTALONE = fmtCOLON;
    /**
     * BACC format of MAC addresses, e.g. 1,6,11:22:#3:44:55:66
     */
    public final static int fmtBACC = 1;
    /**
     * Cisco router format of MAC addresses, e.g. 1122,3344,5566
     */
    public final static int fmtCISCO = 2;
    /**
     * Plain MAC address format, a series of hex characers: 112233445566 
     */
    public final static int fmtPLAIN = 3;
    /**
     * A MAC address formatted as a valid DNS entry as used by CNR and BACC, e.g. mta-11-22-33-44-55-66 
     */
    public final static int fmtMTADNS = 4;
    /**
     * A MAC address formatted as a valid DNS entry as used by CNR and BACC, e.g. ata-11-22-33-44-55-66 
     */
    public final static int fmtATADNS = 5;
    /**
     * A MAC address formatted as a device in a CMTS, e.g. 0011.1a57.0276
     */
    public final static int fmtCMTS = 6;
    /**
     * A MAC address formatted as an XML SAMIS record MAC address with dashes between bytes
     */
    public final static int fmtDASH = 7;
    /**
     * A MAC address formatted ad CNR log file entry
     */
    public final static int fmtCNRLOG = 8;
    /**
     * A MAC address formatted as a valid DNS entry as used by CNR and BACC for cpe equipment, e.g. x1-6-11-22-33-44-55-66 
     */
    public final static int fmtCPEDNS = 9;
    /**
     * Internal representation of a MAC address
     */
    protected byte[] bytes;

    public MACaddress(byte[] bytes, int defaultFormat) {
        this.bytes = bytes.clone();
        this.defaultFormat = defaultFormat;
    }

    public MACaddress(String s) {
        MACaddress m = parseMACaddress(s);
        if (m == null)
            throw new IllegalArgumentException("Not a legal MAC address: " + s);
        this.defaultFormat = m.defaultFormat;
        this.bytes = m.bytes;
    }

    public MACaddress(String s, int defaultFormat) {
        this(s);
        this.defaultFormat = defaultFormat;
    }

    public MACaddress() {
        bytes = new byte[6];
    }

    public byte[] asbyteArray() {
        byte[] nb = new byte[6];
        for (int i = 0; i < 6; i++)
            nb[i] = bytes[i];
        return nb;
    }

    public byte getByte(int i) {
        return (byte) (0xFF & bytes[i]);
    }

    public void setByte(int i, byte b) {
        bytes[i] = b;
    }

    public void setByte(int i, int b) {
        setByte(i, (byte) (0xFF & b));
    }

    public void setBytes(int i1, int i2, int i3, int i4, int i5, int i6) {
        bytes[1] = (byte) (0xFF & i1);
        bytes[2] = (byte) (0xFF & i2);
        bytes[3] = (byte) (0xFF & i3);
        bytes[4] = (byte) (0xFF & i4);
        bytes[5] = (byte) (0xFF & i5);
        bytes[6] = (byte) (0xFF & i6);
    }
    private final static String baccRegexp = "1,6,(\\p{XDigit}{2}):(\\p{XDigit}{2}):(\\p{XDigit}{2}):(\\p{XDigit}{2}):(\\p{XDigit}{2}):(\\p{XDigit}{2})";
    private final static String colonRegexp = "(\\p{XDigit}{2}):(\\p{XDigit}{2}):(\\p{XDigit}{2}):(\\p{XDigit}{2}):(\\p{XDigit}{2}):(\\p{XDigit}{2})";
    private final static String ciscoRegexp = "(\\p{XDigit}{2})(\\p{XDigit}{2}),(\\p{XDigit}{2})(\\p{XDigit}{2}),(\\p{XDigit}{2})(\\p{XDigit}{2})";
    private final static String plainRegexp = "(\\p{XDigit}{2})(\\p{XDigit}{2})(\\p{XDigit}{2})(\\p{XDigit}{2})(\\p{XDigit}{2})(\\p{XDigit}{2})";
    private final static String mtadnsRegexp = "(?:m)ta-1-6-(\\p{XDigit}{2})-(\\p{XDigit}{2})-(\\p{XDigit}{2})-(\\p{XDigit}{2})-(\\p{XDigit}{2})-(\\p{XDigit}{2}).*";
    private final static String atadnsRegexp = "(?:a)ta-1-6-(\\p{XDigit}{2})-(\\p{XDigit}{2})-(\\p{XDigit}{2})-(\\p{XDigit}{2})-(\\p{XDigit}{2})-(\\p{XDigit}{2}).*";
    private final static String cpednsRegexp = "x1-6-(\\p{XDigit}{2})-(\\p{XDigit}{2})-(\\p{XDigit}{2})-(\\p{XDigit}{2})-(\\p{XDigit}{2})-(\\p{XDigit}{2}).*";
    private final static String cmtsRegexp = "(\\p{XDigit}{2})(\\p{XDigit}{2}).(\\p{XDigit}{2})(\\p{XDigit}{2}).(\\p{XDigit}{2})(\\p{XDigit}{2})";
    private final static String cnrLogRegexp1 = "01:(\\p{XDigit}{2}):(\\p{XDigit}{2}):(\\p{XDigit}{2}):(\\p{XDigit}{2}):(\\p{XDigit}{2}):(\\p{XDigit}{2})";
    private final static String dashRegexp = "(\\p{XDigit}{2})-(\\p{XDigit}{2})-(\\p{XDigit}{2})-(\\p{XDigit}{2})-(\\p{XDigit}{2})-(\\p{XDigit}{2})";
    private static Pattern[] parr = new Pattern[]{
        Pattern.compile(colonRegexp),
        Pattern.compile(baccRegexp),
        Pattern.compile(ciscoRegexp),
        Pattern.compile(plainRegexp),
        Pattern.compile(mtadnsRegexp),
        Pattern.compile(atadnsRegexp),
        Pattern.compile(cpednsRegexp),
        Pattern.compile(cmtsRegexp),
        Pattern.compile(dashRegexp),
        Pattern.compile(cnrLogRegexp1)
    };

    /**
     * Generate a MAC address type from a String object in various formats.
     * Formats supported:
     * <ul>
     * <li>BACC format: 1,6,bb:bb:bb:bb:bb:bb
     * <li>Standard format: bb:bb:bb:bb:bb:bb
     * <li>Cisco format: bbbb,bbbb,bbbb
     * <li>Plain format: bbbbbbbbbbbb
     * </ul>
     * The default output format of the mac address will be set to teh same format as
     * that used to parse the mac address, conserving formatting as much as possible.
     * @param s
     *                the input String to parse as an MAC address
     * @return returns a MACaddress object or null if parse error
     */
    public static MACaddress parseMACaddress(String s) {
        Matcher m;
        for (int i = 0; i < parr.length; i++) {
            m = parr[i].matcher(s);
            if (m.matches())
                try {
                    byte[] bytes = new byte[6];
                    bytes[0] = (byte) Integer.parseInt(m.group(1), 16);
                    bytes[1] = (byte) Integer.parseInt(m.group(2), 16);
                    bytes[2] = (byte) Integer.parseInt(m.group(3), 16);
                    bytes[3] = (byte) Integer.parseInt(m.group(4), 16);
                    bytes[4] = (byte) Integer.parseInt(m.group(5), 16);
                    bytes[5] = (byte) Integer.parseInt(m.group(6), 16);
//                    Logger.getLogger(MACaddress.class.getName()).finer("Matched mac adr format: " + i + ": " + m);
                    return new MACaddress(bytes, i);
                } catch (NumberFormatException e) {
                    return null;
                }
        }
        return null;
    }

    /**
     * Generate a MAC address type from a String object in various formats
     * and default converts to colon format String.
     * 
     * @param s
     *                the MAC address to parse
     * @param defaultFormat
     *                denotes the default toString() format
     * @return returns a MACaddress object or null if parse error
     */
    public static MACaddress parseMACaddress(String s, int defaultFormat) {
        MACaddress m = parseMACaddress(s);
        if (m != null)
            m.defaultFormat = defaultFormat;
        return m;
    }

    /**
     * Convert a MAC address to a String
     */
    @Override
    public String toString() {
        return toString(defaultFormat);
    }

    public String toString(int fmt) {
        String s = "";
        switch (fmt) {
            case fmtBACC:
                s = "1,6,";
            // fallthrough
            case fmtCOLON:
                for (int i = 0; i < 6; i++) {
                    if ((0xFF & bytes[i]) < 16)
                        s += "0";
                    s += Integer.toHexString(0xff & bytes[i]);
                    if (i < 5)
                        s += ":";
                }
                break;
            case fmtMTADNS:
                s = "mta-1-6-";
                for (int i = 0; i < 6; i++) {
                    if ((0xFF & bytes[i]) < 16)
                        s += "0";
                    s += Integer.toHexString(0xff & bytes[i]);
                    if (i < 5)
                        s += "-";
                }
                break;
            case fmtATADNS:
                s = "ata-1-6-";
                for (int i = 0; i < 6; i++) {
                    if ((0xFF & bytes[i]) < 16)
                        s += "0";
                    s += Integer.toHexString(0xff & bytes[i]);
                    if (i < 5)
                        s += "-";
                }
                break;
            case fmtCPEDNS:
                s = "x1-6-";
                for (int i = 0; i < 6; i++) {
                    if ((0xFF & bytes[i]) < 16)
                        s += "0";
                    s += Integer.toHexString(0xff & bytes[i]);
                    if (i < 5)
                        s += "-";
                }
                break;
            case fmtCISCO:
                for (int i = 0; i < 6; i += 2) {
                    if ((0xFF & bytes[i]) < 16)
                        s += "0";
                    s += Integer.toHexString(0xFF & bytes[i]);
                    if ((0xFF & bytes[i + 1]) < 16)
                        s += "0";
                    s += Integer.toHexString(0xff & bytes[i + 1]);
                    if (i < 4)
                        s += ",";
                }
                break;
            case fmtPLAIN:
                for (int i = 0; i < 6; i++) {
                    if ((0xFF & bytes[i]) < 16)
                        s += "0";
                    s += Integer.toHexString(0xFF & bytes[i]);
                }
                break;
            case fmtDASH:
                for (int i = 0; i < 6; i++) {
                    if ((0xFF & bytes[i]) < 16)
                        s += "0";
                    s += Integer.toHexString(0xFF & bytes[i]);
                    if (i < 5)
                        s += "-";
                }
                break;
            case fmtCMTS:
                for (int i = 0; i < 6; i += 2) {
                    if ((0xFF & bytes[i]) < 16)
                        s += "0";
                    s += Integer.toHexString(0xFF & bytes[i]);
                    if ((0xFF & bytes[i + 1]) < 16)
                        s += "0";
                    s += Integer.toHexString(0xff & bytes[i + 1]);
                    if (i < 4)
                        s += ".";
                }
                break;
            default:
                throw new IllegalArgumentException("Illegal format specified: " + fmt);
        }
        return s;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        final MACaddress other = (MACaddress) obj;
        if (other.bytes == bytes)
            return true;
        for (int i = 0; i < bytes.length; i++)
            if (bytes[i] != other.bytes[i])
                return false;
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        if (bytes != null) {
            for (int b : bytes) {
                hash *= 89;
                hash += b;
            }
        }
        return hash;
    }

    @Override
    public Object clone() {
        return new MACaddress(this.bytes.clone(), this.defaultFormat);
    }

    // Serialization interface
    /**
     * Save the MACaddress instance to a stream (i.e.,
     * serialize it).
     *
     * @serialData The defaultFormat then 6 bytes of the MACaddress
     */
    private void writeObject(java.io.ObjectOutputStream s) throws IOException {
        s.writeInt(defaultFormat);
        s.write(bytes);
    }
    private static final long serialVersionUID = 20071115214500L;

    /**
     * Reconstitute the MACaddress instance from a stream (i.e.,
     * deserialize it).
     */
    private void readObject(java.io.ObjectInputStream s) throws IOException, ClassNotFoundException {
        defaultFormat = s.readInt();
        if (bytes == null)
            bytes = new byte[6];
        if (s.read(bytes) != bytes.length)
            throw new ClassNotFoundException("MACaddress too few bytes read (expected 6)");
    }

    /**
     * Add an integer to a mac address producing a new Mac address, remembering 
     * to "carry" over between the bytes in the mac address
     * @param inc the integer to add; negative values subtract
     * @return a new mac address that is n higher than this mac address
     */
    public MACaddress addToMac(int inc) {
        MACaddress res = (MACaddress) this.clone();
        int carry = inc;
        for (int i = 5; i >= 0; i--) {
            byte b = res.getByte(i);
            int ib = b & 0xFF;
            ib += carry;
            if (ib > 0xFF)
                carry = 1;
            else
                carry = 0;
            res.setByte(i, ib & 0xFF);
        }
        return res;
    }
}

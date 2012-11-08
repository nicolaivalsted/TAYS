package dk.yousee.randy.base;

/**
 * User: aka
 * Date: 08/11/12
 * Time: 11.04
 * Simple class to filter some xml
 */
public class XmlFiltering {

    public String filterXml(String tag, String xml) {
        String res=null;
        int pos0 = xml.indexOf("<" + tag + ">");
        int pos1 = xml.indexOf("</" + tag + ">");
        if (pos0 >= 0 && pos1 > 0) {
            int tagLen = tag.length() + 2;
            String value = xml.substring(pos0 + tagLen, pos1);
            if (value != null && value.trim().length() != 0) {
                res=value;
            }
        }
        return res;
    }

}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.yousee.randy.logging;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.log4j.spi.LoggingEvent;

/**
 *
 * @author jablo
 */
//
public class KVParsingJSONFormatter extends uk.me.mjt.log4jjson.SimpleJsonLayout {
    @Override
    public void after(LoggingEvent le, Map<String, Object> r) {
        Object omsg = le.getMessage();
        String msg = safeToString(omsg);
        Map parsedkv = parseKVPairs(msg);
        r.putAll(parsedkv);
    }

    /**
     * LoggingEvent messages can have any type, and we call toString on them. As
     * the user can define the toString method, we should catch any exceptions.
     *
     * @param obj
     * @return
     */
    private static String safeToString(Object obj) {
        if (obj == null)
            return null;
        try {
            return obj.toString();
        } catch (Throwable t) {
            return "Error getting message: " + t.getMessage();
        }
    }
    private final static Pattern kv = Pattern.compile("(\\w+)=([^\"']\\S*)");
    private final static Pattern kqvq = Pattern.compile("(\\w+)='([^']*)'");
    private final static Pattern kqqvqq = Pattern.compile("(\\w+)=\"([^\"]*)\"");
    private final static Pattern[] ps = new Pattern[]{kv, kqvq, kqqvqq};

    private Map<String, String> parseKVPairs(String s) {
        Map result = new HashMap();
        for (Pattern p : ps) {
            Matcher matcher = p.matcher(s);
            while (matcher.find()) {
                String k = matcher.group(1);
                String v = matcher.group(2);
                result.put(k, v);
            }
        }
        return result;
    }
}
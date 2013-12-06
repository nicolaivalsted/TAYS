/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.yousee.randy.logging;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.log4j.MDC;
import org.apache.log4j.NDC;
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
        // Add All key=value from log message as json fields
        Map parsedkv = parseKVPairs(msg);
        r.putAll(parsedkv);
        // Add all key=value messages pushed on the NDC stack
        Stack cloneStack = NDC.cloneStack();
        if (cloneStack != null) {
            for (Iterator it = cloneStack.iterator(); it.hasNext();) {
                String s = safeToString(it.next());
                if (s == null)
                    continue;
                Map<String, String> kvs = parseKVPairs(s);
                r.putAll(kvs);
            }
        }
        // Add all key-value pairs from the MDC logging map
        Hashtable context = MDC.getContext();
        if (context != null)
            r.putAll(hashTableToMap(context));
    }

    private Map<String, String> hashTableToMap(Hashtable context) {
        HashMap<String, String> result = new HashMap();
        Set entrySet = context.entrySet();
        for (Object o : entrySet) {
            Entry<String, Object> e = (Entry<String, Object>) o;
            if (e.getValue() == null)
                continue;
            result.put(e.getKey(), e.getValue().toString());
        }
        return result;
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
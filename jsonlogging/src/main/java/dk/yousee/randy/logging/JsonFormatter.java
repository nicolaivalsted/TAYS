/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.yousee.randy.logging;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

/**
 *
 * @author m26517
 */
public class JsonFormatter implements ValueFormatter {
	
	@Override
    public JsonElement format(String arg) {
        return new JsonParser().parse(arg);
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.yousee.tays.bbservice.restbase;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;

/**
 * Utility class to hold an XML message element. Produces <message>some
 * text</message> imutable pattern added
 */
@XmlRootElement
public class Message {
    private String message;

    public Message() {
        message = "";
    }

    public Message(String string) {
        message = string;
    }

    @XmlValue
    public String getMessage() {
        return message;
    }
}

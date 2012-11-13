/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.yousee.randy.virtualmacclient;

import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author jablo
 */
@XmlRootElement
public class OKResponse {
    private String message;

    public OKResponse() {
    }

    public OKResponse(String msg) {
        message = msg;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
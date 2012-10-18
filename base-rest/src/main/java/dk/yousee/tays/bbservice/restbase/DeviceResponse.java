/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.yousee.tays.bbservice.restbase;

import dk.yousee.tays.bbservice.restbase.Link;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author jablo
 */
@XmlRootElement
public class DeviceResponse {
    protected String macAddress;
    protected Link link;

    public Link getLink() {
        return link;
    }

    public void setLink(Link details) {
        this.link = details;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String mac) {
        this.macAddress = mac;
    }
}

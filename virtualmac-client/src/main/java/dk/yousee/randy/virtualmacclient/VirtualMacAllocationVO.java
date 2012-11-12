/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.yousee.randy.virtualmacclient;

import java.util.Date;
import javax.xml.bind.annotation.XmlRootElement;
//import ktv.valueobjects.MACaddress;

/**
 * Value object to hold virtual mac address allocation information from the virtual
 * mac allocation service
 * @author Jacob Lorensen, YouSee, 2011-12-05
 */
@XmlRootElement
public class VirtualMacAllocationVO {
    protected String mac;
    protected Date allocationTimestamp;
    protected String subscriberAccount;

    public VirtualMacAllocationVO() {
    }

    public String getSubscriberAccount() {
        return subscriberAccount;
    }

    public void setSubscriberAccount(String subscriberAccount) {
        this.subscriberAccount = subscriberAccount;
    }

    public Date getAllocationTimestamp() {
        return allocationTimestamp;
    }

    public void setAllocationTimestamp(Date allocationTimestamp) {
        this.allocationTimestamp = allocationTimestamp;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getMac() {
        return mac;
    }

    @Override
    public String toString() {
        return getClass().getCanonicalName() + "{" + mac
                + ";" + subscriberAccount + ";" + allocationTimestamp + "}";
    }
}

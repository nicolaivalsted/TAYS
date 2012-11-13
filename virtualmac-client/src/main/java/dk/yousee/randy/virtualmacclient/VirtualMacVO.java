/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.yousee.randy.virtualmacclient;

import javax.xml.bind.annotation.XmlRootElement;
//import ktv.valueobjects.MACaddress;

@XmlRootElement
public class VirtualMacVO {
    private String mac;
    protected VirtualMacAllocationVO allocation;

    public VirtualMacVO() {
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String str) {
        this.mac = str;
    }

    public VirtualMacAllocationVO getAllocation() {
        return allocation;
    }

    public void setAllocation(VirtualMacAllocationVO allocation) {
        this.allocation = allocation;
    }

    @Override
    public String toString() {
        return this.getClass().getCanonicalName() + "{" + mac + "}";
    }
}

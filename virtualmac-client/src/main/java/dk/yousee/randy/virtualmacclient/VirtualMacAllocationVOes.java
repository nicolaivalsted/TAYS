/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.yousee.randy.virtualmacclient;

import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

/**
 *
 * @author jablo
 */
@XmlRootElement
@XmlSeeAlso(VirtualMacAllocationVO.class)
public class VirtualMacAllocationVOes {
    private List<VirtualMacAllocationVO> allocation;

    public List<VirtualMacAllocationVO> getVirtualMacAllocationVO() {
        return allocation;
    }

    public void setVirtualMacAllocationVO(List<VirtualMacAllocationVO> allocation) {
        this.allocation = allocation;
    }
    
    public void addVirtualMacAllocationVO(VirtualMacAllocationVO vo) {
        allocation.add(vo);
    }
}

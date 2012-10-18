/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.yousee.tays.bbservice.restbase.asucase;

import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author jablo
 */
@XmlRootElement
public class SimplifiedSubscriberServicesResponse {
    protected List<SimplifiedSubscriberService> services;

    public SimplifiedSubscriberServicesResponse() {
    }

    public SimplifiedSubscriberServicesResponse(List<SimplifiedSubscriberService> services) {
        this.services = services;
    }

    public List<SimplifiedSubscriberService> getServices() {
        return services;
    }

    public void setServices(List<SimplifiedSubscriberService> services) {
        this.services = services;
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.yousee.tays.bbservice.restbase.asucase;

import dk.yousee.tays.bbservice.restbase.SubscriberAccessNetResponse;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author jablo
 */
@XmlRootElement
public class AsuRequest {
    protected SubscriberAccessNetResponse accessNet;
    protected SimplifiedSubscriberServicesResponse services;

    public AsuRequest() {
    }

    public AsuRequest(SubscriberAccessNetResponse accessNet, SimplifiedSubscriberServicesResponse services) {
        this.accessNet = accessNet;
        this.services = services;
    }

    public SubscriberAccessNetResponse getAccessNet() {
        return accessNet;
    }

    public void setAccessNet(SubscriberAccessNetResponse accessNet) {
        this.accessNet = accessNet;
    }

    public SimplifiedSubscriberServicesResponse getServices() {
        return services;
    }

    public void setServices(SimplifiedSubscriberServicesResponse services) {
        this.services = services;
    }
}

package dk.yousee.tays.bbservice.restbase;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * A class used to describe a broadband subscriber's equipment: cable modem, mta and cpes. 
 * Used in accessnet-rest tays-rest
 * @author m27236
 */
@XmlRootElement(name = "AccessNet")
@XmlAccessorType(XmlAccessType.FIELD)
public class SubscriberAccessNetResponse {
    @XmlElement(nillable = true)
    private Device cm;
    @XmlElement(nillable = true)
    private Device mta;
    @XmlElement(name = "cpes")
    private List<Device> cpes = new ArrayList<Device>();
    //
    @XmlElement(name = "links")
    private List<Link> links = new ArrayList<Link>();

    public SubscriberAccessNetResponse() {
    }

//    /**
//     * Build an AccessnetResponse from the SubscriberNetworkTopology requested from BACC. This
//     * functionality should be moved to the AccessNet-rest and/or Tays-rest rest service, NOT
//     * inside this shared class in rest-base.
//     * @param uriInfo
//     * @param subscriberNetworkTopology
//     *
//    public SubscriberAccessNetResponse(UriInfo uriInfo, SubscriberNetworkTopology subscriberNetworkTopology) {
//        if (subscriberNetworkTopology.getCm() != null) {
//            cm = new Device(subscriberNetworkTopology.getCm());
//            cm.addLink(new Link("self", uriInfo.getBaseUriBuilder().
//                    path("../").
//                    path(ServiceEndpoints.ACCESSNET_DEVICES_SERVICE).
//                    path(cm.getMacAddress()).
//                    build().normalize().toASCIIString(), "GET"));
//            final String href = uriInfo.getBaseUriBuilder().
//                    path("../").
//                    path(ServiceEndpoints.SMP_SUBSCRIBER_SERVICE).
//                    queryParam("cmmac", new MACaddress(cm.getMacAddress()).toString(MACaddress.fmtPLAIN)).
//                    build().normalize().toASCIIString();
//            cm.addLink(new Link("subscriber", href, "GET"));
//        }
//        if (subscriberNetworkTopology.getMta() != null) {
//            mta = new Device(subscriberNetworkTopology.getMta());
//            mta.addLink(new Link("self", uriInfo.getBaseUriBuilder().
//                    path("../").
//                    path(ServiceEndpoints.ACCESSNET_DEVICES_SERVICE).
//                    path(mta.getMacAddress()).
//                    build().normalize().toASCIIString(), "GET"));
//            final String href = uriInfo.getBaseUriBuilder().
//                    path("../").
//                    path(ServiceEndpoints.SMP_SUBSCRIBER_SERVICE).
//                    queryParam("mtamac", new MACaddress(mta.getMacAddress()).toString(MACaddress.fmtPLAIN)).
//                    build().normalize().toASCIIString();
//            mta.addLink(new Link("subscriber", href, "GET"));
//        }
//        if (!subscriberNetworkTopology.getCpes().isEmpty()) {
//            for (RDUDevice rdud : subscriberNetworkTopology.getCpes()) {
//                Device cpeDev = new Device(rdud);
//                cpes.add(cpeDev);
//                cpeDev.addLink(new Link("self", uriInfo.getBaseUriBuilder().
//                        path("../").
//                        path(ServiceEndpoints.ACCESSNET_DEVICES_SERVICE).
//                        path(cpeDev.getMacAddress()).
//                        build().normalize().toASCIIString(), "GET"));
//                final String href = uriInfo.getBaseUriBuilder().
//                        path("../").
//                        path(ServiceEndpoints.SMP_SUBSCRIBER_SERVICE).
//                        queryParam("cpemac", rdud.getMacAddress().toString(MACaddress.fmtPLAIN)).
//                        build().normalize().toASCIIString();
//                cpeDev.addLink(new Link("subscriber", href, "GET"));
//            }
//        }
//    }/
     /**
      * Build an AccessnetResponse object
      * @param cm in
      * @param mta in
      * @param cpes in
      */
     public SubscriberAccessNetResponse(Device cm, Device mta, List<Device> cpes) {
         this.cm = cm;
         this.mta = mta;
         if(cpes!=null){
             this.cpes = cpes;
         }
    }

    public Device getCm() {
        return cm;
    }

    public List<Device> getCpes() {
        return cpes;
    }
    
    public void addCpe(Device device){
        cpes.add(device);
    }

    public Device getMta() {
        return mta;
    }

    public void setCm(Device cm) {
        this.cm = cm;
    }

    public void setMta(Device mta) {
        this.mta = mta;
    }
}

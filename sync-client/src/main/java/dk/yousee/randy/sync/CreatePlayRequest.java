package dk.yousee.randy.sync;

/**
 * Created with IntelliJ IDEA.
 * User: aka
 * Date: 12/01/13
 * Time: 07.50
 * Request for creating play event
 */
public class CreatePlayRequest {


    public CreatePlayRequest(String subscriber, String serviceItem, boolean signal, String system, String reference, String user) {
        this.subscriber = new SubscriberId(subscriber);
        this.serviceItem = serviceItem;
        this.signal = signal;
        this.system = system;
        this.reference = reference;
        this.user = user;
    }

    public CreatePlayRequest(SubscriberId subscriber, String serviceItem, boolean signal, String system, String reference, String user) {
        this.subscriber = subscriber;
        this.serviceItem = serviceItem;
        this.signal = signal;
        this.system = system;
        this.reference = reference;
        this.user = user;
    }

    SubscriberId subscriber;
    String serviceItem;
    boolean signal;

    public SubscriberId getSubscriber() {
        return subscriber;
    }

    /**
     * Service item that describe BB
     * @return broad band service item (alias Stalone item)
     */
    public String getServiceItem() {
        return serviceItem;
    }

    public boolean isSignal() {
        return signal;
    }

    String system;
    public String getSystem() {
        return system;
    }

    String reference;
    public String getReference() {
        return reference;
    }

    String user;
    public String getUser() {
        return user;
    }

}

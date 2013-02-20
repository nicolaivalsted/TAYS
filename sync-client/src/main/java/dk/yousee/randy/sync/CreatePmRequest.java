package dk.yousee.randy.sync;

/**
 * Created with IntelliJ IDEA.
 * User: aka
 * Date: 12/01/13
 * Time: 07.50
 * Request for creating Pm engagement
 */
public class CreatePmRequest {


    public CreatePmRequest(String subscriber, String system, String reference, String user, String content) {
        this.subscriber = new SubscriberId(subscriber);
        this.system = system;
        this.reference = reference;
        this.user = user;
        this.content=content;
    }

    SubscriberId subscriber;

    public SubscriberId getSubscriber() {
        return subscriber;
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

    String content;

    public String getContent() {
        return content;
    }

}

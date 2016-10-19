package dk.yousee.smp.order.model;

/**
 * The facade to clients to sigma
 */
public interface OrderService {

    /**
     * Webservice URL to connect to
     * @return string "http://server:port/xxxx"
     */
    public String getSigmaServiceUrl();

    /**
     * Connection timeout for service
     * @return ms. Null means unassigned
     */
    public Integer getOperationTimeout();
    /**
     * Read information about the connection
     * @return a string with connect information
     */
    public String connectInfo();
    /**
     * Find subscriber by subscriber Id and return contain the entire engagement
     *
     * @param acct subscriber id (equivalent to CaAccountKey in kasia-services)
     * @return reply, the entire engagement for the customer containing (customer info, service plans ...) but no future orders.
     */
    public Response readSubscription(Acct acct);


}

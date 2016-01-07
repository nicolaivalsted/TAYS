package dk.yousee.smp5.order.model;



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
    
    /**
     * Creates an account in a synchronized way and return the customers engagement (will be empty, but ready for more order processing)
     *
     * @param order to create the customer
     * @return reply, the entire engagement for the customer containing (customer info ..) but no real plans (yet)
     */
    public Response addSubscription(Order order);
    
    /**
     * <p>
     * Maintain service plans (add, update, delete) (contact, address, bb, mobb, voip ...)
     * </p>
     * <p>
     * precondition: order.type must be filled in<br/>
     * <p/>
     * </p>
     * <p>
     * Orders MUST be synchronous.
     * </p>
     *
     * @param order - build an order structure and pass this as "command"
     * @return reply from Sigma. An order or an error message. (+ contains xml documents)
     */
	public ExecuteOrderReply maintainPlan(Order order);
	
	public ExecuteOrderReply maintainPlanJMS(Order order);

    /**
    * Find orders by subscriber ,  return all the oreders.
    *
     * @param subscriberID - user of net
     */
   public QueryOrdersBySubscriberReply queryOrdersBySubscriber(String subscriberID);
   
   /**
    * Find orders by subscriber  ,only return the open orders.
    * @param subscriberID - user of net
    */
   public QueryOrdersBySubscriberReply queryOpenOrdersBySubscriber(String subscriberID);
   
   public SearchCustomersResponse searchSubscriber(SearchCustomersRequest searchCustomersRequest);
   
   public QueryOrderReply queryOrderState(String orderID);

}

package dk.yousee.smp.order.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class Order implements Serializable {

    static final private long serialVersionUID = -7569650801379990454L;
    public static final String TYPE_PROVISIONING = "PROVISIONING";
    public static final String TYPE_TECHNICAL_PROVISIONING = "TECHNICAL_PROVISIONING";
    public static final String TYPE_DELETE_SUBSCRIBER = "DELETE_SUBSCRIBER";

	private String type;
	private boolean asynchronous = false;

    private Date execDate = null;
    private String externalKey = null; 
    /**
     * The client calling sigma see xml tag: orderValue/apiClientId
     */
    private String apiClientId="triple";

    public String getExternalKey() {
        return externalKey;
    }

    public void setExternalKey(String externalKey) {
        this.externalKey = externalKey;
    }

    public String getApiClientId() {
        return apiClientId;
    }

    public void setApiClientId(String apiClientId) {
        this.apiClientId = apiClientId;
    }

	private String returnEventUrl;

    private Subscriber subscriber;

    /**
     * Date string must be of format: YYYYMMDDhhmmss
     * @param newDate, do not assign if you want order executed NOW
     */
    public void setExecDate(Date newDate){
        this.execDate = newDate;

    }

    /**
     *
     * @return the order execution date. Null is fully allowed, meaning NOW
     */
    public Date getExecDate() {
        return this.execDate;
    }

    public void setSubscriber(Subscriber subscriber) {
		this.subscriber = subscriber;
	}
	public Subscriber getSubscriber() {
		return subscriber;
	}
    public void setType(String type) {
		this.type = type;
	}
	public String getType() {
		return type;
	}

    /**
     * Setting the call to be come asynchronous
     * @param asynchronous, true turns the call asynchronous.
     * Default is false (same as not calling the setter)
     */
	public void setAsynchronous(boolean asynchronous) {
    	this.asynchronous = asynchronous;
	}
	public boolean isAsynchronous() {
		return asynchronous;
	}

    /**
     * list of greater ServicePlans (Level=SERVICE, ADDRESS, CONTACT...)
     */
    private List<OrderData> orderData = new ArrayList<OrderData>();
	public void setOrderData(List<OrderData> orderData) {
		this.orderData = orderData;
	}

    /**
     * <p>
     * The order service plans.
     * The value is function overloaded
     * </p>
     * 1) in future order-response the value is null<br/>
     * 2) in subscriber response (to become deprecated) the list contains only ServicePlans !!<br/>
     * 3) Same in QUERY it is only OrderDataLevel.SERVICE<br/>
     * 4)
     *
     * @return list of serviceplans ++
     */
	public List<OrderData> getOrderData() {
		return orderData;
	}
    public void addOrderData(OrderData data) {
        if (orderData == null){
            orderData = new ArrayList<OrderData>();
        }
        orderData.add(data);
    }

	public void setReturnEventUrl(String returnEventUrl) {
		this.returnEventUrl = returnEventUrl;
	}
	public String getReturnEventUrl() {
		return returnEventUrl;
	}

    private Map<String,String> params;

    public Map<String,String> getParams() {
        if(params==null)params=new TreeMap<String,String>();
        return params;
    }

    public void setParams(Map<String,String> params) {
        this.params = params;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("{type='").append(type).append('\'');
        sb.append(", externalKey='").append(externalKey).append('\'');
        sb.append(", subscriber=").append(subscriber);
//        sb.append(", orderData=").append(orderData);
//        sb.append(", params=").append(params);
        sb.append('}');
        return sb.toString();
    }
}

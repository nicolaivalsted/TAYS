/**
 * 
 */
package dk.yousee.smp5.order.model;

/**
 * @author m64746
 *
 * Date: 16/10/2015
 * Time: 12:36:20
 */
public class ExecuteOrderReply {
	private MadeOrder executing;
    private String errorMessage;
    private Smp5Xml xml;

    /**
     * A success full reply
     * @param executing order info
     * @param xml strings
     */
    public ExecuteOrderReply(MadeOrder executing, Smp5Xml xml) {
        this.executing = executing;
        this.xml = xml;
    }

    /**
     * A failure reply
     * @param errorMessage message
     * @param xml strings
     */
    public ExecuteOrderReply(String errorMessage, Smp5Xml xml) {
        this.errorMessage = errorMessage;
        this.xml = xml;
    }

    public MadeOrder getExecuting() {
        return executing;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public Smp5Xml getXml() {
        return xml;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("{\"order\":").append(getExecuting()==null?"none":executing.getOrderId().toString());
        if (getErrorMessage() != null) sb.append(", \"errorMessage\":\"").append(errorMessage).append('"');
        if (getXml() != null) sb.append(", \"xml\":").append(getXml().toString());
        sb.append('}');
        return sb.toString();
    }

    public Integer getOrderId() {
        return getExecuting()==null?null:getExecuting().getOrderId();
    }

    /**
     * Created with IntelliJ IDEA.
     * User: aka
     * Date: 04/06/12
     * Time: 15.10
     * Response from SMP when it delivers a message like "Created order X for this update"
     */
    public static class MadeOrder {

        private Integer orderId;
        /**
         * State returned from SMP
         */
        private ProvisionStateEnum state;

        public MadeOrder(Integer orderId, ProvisionStateEnum state) {
            this.orderId = orderId;
            this.state = state;
        }

        public Integer getOrderId() {
            return orderId;
        }

        public ProvisionStateEnum getState() {
            return state;
        }


        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder();
            sb.append("{\"orderId\":").append(getOrderId());
            if (getState() != null) sb.append(", \"state\":").append(state);
            sb.append('}');
            return sb.toString();
        }
    }
}

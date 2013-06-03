package dk.yousee.smp.cases;

import dk.yousee.smp.casemodel.SubscriberModel;
import dk.yousee.smp.order.model.Acct;
import dk.yousee.smp.order.model.BusinessException;
import dk.yousee.smp.order.model.ExecuteOrderReply;
import dk.yousee.smp.order.model.Order;
import dk.yousee.smp.order.model.OrderService;
import dk.yousee.smp.order.model.Response;
import dk.yousee.smp.order.model.SystemException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by IntelliJ IDEA.
 * User: aka
 * Date: Oct 25, 2010
 * Time: 2:05:50 PM<br/>
 * Abstract case to base multiple cases on
 */
public class AbstractCase {

    private SubscriberModel model;
    private Acct acct;
    private Response response;
    private OrderService service;
    private String errorMessage;
    private ExecuteOrderReply lastOrderReply;

    /**
     * When we compose orders it is relevant to keep the same model or getting a new one.
     * <br/>
     * When you update the customer in one order and update plan in an new order then you want a new model.
     * <br/>
     * When you update the customer (and send no order yet) and then update a plan and want both
     * update to be in same order - then you want to keep the model. (saying true)
     * @param model model to optionally keep
     * @param keepModel true == keep it, false == make a new model from response
     * @return the model to use
     */
    protected static SubscriberModel selectModel(SubscriberModel model,boolean keepModel){
        return keepModel?model:new SubscriberModel(model.getResponse());
    }

    /**
     * Constructor with the customer account.
     *
     * @param acct    account to work for
     * @param service the service to use to contact sigma
     */
    public AbstractCase(Acct acct, OrderService service) {
        this.acct = acct;
        this.service = service;
    }

    /**
     * Constructor with the customer account. - from other usec ase
     *
     * @param model   the model coming from another use case
     * @param service the service to use to contact sigma
     */
    public AbstractCase(SubscriberModel model, OrderService service) {
        this.model = model;
        this.acct = model.getAcct();
        this.service = service;
    }

    public OrderService getService() {
        return service;
    }

    /**
     * Returns the account you have set - in case you have forgotten
     *
     * @return account id (9 digits)
     */
    public Acct getAcct() {
        return acct;
    }

    public SubscriberModel getModel() {
        if (model == null) buildSubscriberModel();
        return model;
    }

    protected void ensureAcct() throws BusinessException {
        if (!getModel().customerExists()) {
            throw new BusinessException(
                    "Operation failed,  Cannot create/update/delete when the customer does not exist. Acct: %s", getAcct());
        }
    }

    public void setModel(SubscriberModel model) {
        this.model = model;
    }

    /**
     * Load the customer response into SubscriberModel.
     */
    private void buildSubscriberModel() {
        if (response == null) readSubscriber();
        model = new SubscriberModel(response);
    }

    /**
     * Get the customer response from SMP.
     *
     * @throws SystemException when technical error
     */
    public void readSubscriber() {
        try {
            response = service.readSubscription(acct);
        } catch (Exception e) {
            errorMessage = e.getMessage();
            StringBuilder cause=new StringBuilder();
            if(e.getCause()!=null){
                cause.append(", caused by ").append(e.getCause().getMessage());
            }
            throw new SystemException(String.format("readSubscriber failed, got exception: %s%s",e.getMessage(),cause.toString()));
        }
        buildSubscriberModel();
    }

    protected Response getResponse() {
        return response;
    }

    protected void setResponse(Response response) {
        this.response = response;
    }

    protected void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    /**
     * The error message that has been generated
     *
     * @return error message, null means no error
     */
    public String getErrorMessage() {
        return errorMessage;
    }

    /**
     * commit the changes to SMP, the order is the order generated in the model.<br/>
     * precondition: order must be established by model updates<br/>
     * postcondition: OrderReply made, orderId returned
     *
     * @return order number
     * @throws BusinessException when could not send, see the errorMessage as well
     */
    public Integer send() throws BusinessException {
        return send(model.getOrder());
    }

    /**
     * commit the changes to SMP.<br/>
     * postcondition: OrderReply made, orderId returned
     *
     * @param order2send the order to send. (should be taken from the model
     * @return order number
     * @throws BusinessException when could not send, see the errorMessage as well
     */
    public Integer send(Order order2send) throws BusinessException {
        setErrorMessage(null);
        try {
            lastOrderReply = service.maintainPlan(order2send);
        } catch (Exception e) {
            setErrorMessage(e.getMessage());
            throw new RuntimeException(e.getMessage(), e);
        }
        if (lastOrderReply.getErrorMessage() != null) {
            String detailError = lastOrderReply.getXml().getResponse();
            detailError = errorMessageHandler(lastOrderReply.getErrorMessage(), detailError);
            setErrorMessage(detailError);
            throw new BusinessException("When sendng order, got exception: %s", getErrorMessage());
        }
        return lastOrderReply.getOrderId();
    }


    public String errorMessageHandler(String original, String detailError) {
        try {
            DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder;
            docBuilder = docBuilderFactory.newDocumentBuilder();
            Document document = docBuilder.parse(new InputSource(new StringReader(detailError)));
            NodeList nodeList = document.getElementsByTagName("smp:errorCode");
            List<String> codelist = new ArrayList<String>();
            List<String> messagelist = new ArrayList<String>();

            for (int s = 0; s < nodeList.getLength(); s++) {
                Node node = nodeList.item(s);
//                String name = node.getNodeName();
                String value = node.getFirstChild().getNodeValue();
                codelist.add(value);
            }

            NodeList nodeList2 = document.getElementsByTagName("smp:errorMessage");
            for (int s = 0; s < nodeList2.getLength(); s++) {
                Node node = nodeList2.item(s);
//                String name = node.getNodeName();
                String value = node.getFirstChild().getNodeValue();
                messagelist.add(value);
            }

            for (int i = 0; i < codelist.size(); i++) {
                original = original + "\n" + "errorcode = " + codelist.get(i) + "  errorMessage = " + messagelist.get(i);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return original;
    }


    /**
     * @return the last order reply from sigma
     */
    public ExecuteOrderReply getLastOrderReply() {
        return lastOrderReply;
    }

}

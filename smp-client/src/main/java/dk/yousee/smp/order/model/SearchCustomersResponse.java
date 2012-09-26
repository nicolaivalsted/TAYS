package dk.yousee.smp.order.model;

import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: m14857
 * Date: Mar 4, 2011
 * Time: 11:54:37 AM
 * the result to search customers.
 * Contains a list of CustomerInfo's
 */
public class SearchCustomersResponse  {

    public SearchCustomersResponse(ArrayList<CustomerInfo> customersList, SmpXml xml) {

        for(CustomerInfo one:customersList){
            if(!this.customersList.contains(one)){
                this.customersList.add(one);
            }
        }
        setXml(xml);
    }

    public SearchCustomersResponse(String errorMessage, SmpXml xml) {
        this.errorMessage=errorMessage;
        setXml(xml);
    }

    private ArrayList<CustomerInfo> customersList = new ArrayList<CustomerInfo>();

    private String errorMessage;

    private String xmlRequest;
    private String xmlResponse;



    public ArrayList<CustomerInfo> getCustomersList() {
        return customersList;
    }

    private void setXml(SmpXml xml) {
        this.xmlRequest = xml.getRequest();
        this.xmlResponse = xml.getResponse();
    }

    public SmpXml getXml() {
        return new SmpXml(xmlRequest,xmlResponse);
    }

//    @Deprecated
//    public String getXmlRequest() {
//        return xmlRequest;
//    }

//    @Deprecated
//    public void setXmlRequest(String xmlRequest) {
//        this.xmlRequest = xmlRequest;
//    }

//    @Deprecated
//    public String getXmlResponse() {
//        return xmlResponse;
//    }

//    @Deprecated
//    public void setXmlResponse(String xmlResponse) {
//        this.xmlResponse = xmlResponse;
//    }

    public String getErrorMessage() {
        return errorMessage;
    }

//    @Deprecated
//    public void setErrorMessage(String errorMessage) {
//        this.errorMessage = errorMessage;
//    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("{\"xml\":").append(customersList);
        if (getErrorMessage() != null) sb.append(", \"errorMessage\":\"").append(errorMessage).append('"');
        if (getCustomersList() != null) sb.append(", \"list\":").append(customersList);
        sb.append('}');
        return sb.toString();
    }
}

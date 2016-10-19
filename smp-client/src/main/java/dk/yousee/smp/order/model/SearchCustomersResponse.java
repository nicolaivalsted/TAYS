package dk.yousee.smp.order.model;

import java.util.ArrayList;
import java.util.List;

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

    /**
	 * 
	 */
	public SearchCustomersResponse() {
	}

	private ArrayList<CustomerInfo> customersList = new ArrayList<CustomerInfo>();

    private String errorMessage;

    private String xmlRequest;
    private String xmlResponse;



    public ArrayList<CustomerInfo> getCustomersList() {
        return customersList;
    }

    public void setCustomersList(List<CustomerInfo> customersList) {
		this.customersList = (ArrayList<CustomerInfo>) customersList;
	}

	private void setXml(SmpXml xml) {
        this.xmlRequest = xml.getRequest();
        this.xmlResponse = xml.getResponse();
    }

    public SmpXml getXml() {
        return new SmpXml(xmlRequest,xmlResponse);
    }

    public String getErrorMessage() {
        return errorMessage;
    }

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

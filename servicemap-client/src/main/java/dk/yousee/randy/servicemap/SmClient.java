package dk.yousee.randy.servicemap;

import dk.yousee.randy.base.AbstractClient;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.Set;

/**
 * User: aka
 * Date: 06/09/12
 * Time: 23.45
 * Client to access service map
 */
public class SmClient extends AbstractClient<SmConnectorImpl> {


    URL generateVendorUrl() throws MalformedURLException {
        return new URL(String.format("%s/servicemap/api/vendor", getConnector().getServiceMapHost()));
    }

    public Vendors fetchVendors() {
        try {
            return innerFetchVendors();
        } catch (Exception e){
            return new Vendors("Failed2access",e.getMessage());
        }
    }
    public Vendors innerFetchVendors() throws Exception{
        String response = performGet(generateVendorUrl());
        return new Vendors(response);
    }

    URL generateForeningsMailUrl() throws MalformedURLException {
        return new URL(String.format("%s/servicemap/api/foreningsmail", getConnector().getServiceMapHost()));
    }

    public MailResponse fetchForeningsMails() {
        try {
            return innerFetchForeningsMails();
        } catch (Exception e){
            return new MailResponse("Failed2access",e.getMessage());
        }
    }
    public MailResponse innerFetchForeningsMails() throws Exception{
        String response = performGet(generateForeningsMailUrl());
        return new MailResponse(response);
    }

    URL generateForeningsMailUrlByAnlaeg(String anlaeg) throws MalformedURLException {
        return new URL(String.format("%s/servicemap/api/foreningsmail/%s", getConnector().getServiceMapHost(),anlaeg));
    }

    public MailResponse fetchForeningsMailsByAnlaeg(String anlaeg) {
        try {
            return innerFetchForeningsMailsByAnlaeg(anlaeg);
        } catch (Exception e){
            return new MailResponse("Failed2access",e.getMessage());
        }
    }
    public MailResponse innerFetchForeningsMailsByAnlaeg(String anlaeg) throws Exception{
        String response = performGet(generateForeningsMailUrlByAnlaeg(anlaeg));
        return new MailResponse(response);
    }

    URL generateItemUrl(Set items) throws MalformedURLException {
        String itemParams=generateItemParams(items);
        return new URL(String.format("%s/servicemap/api/item/query%s", getConnector().getServiceMapHost(),itemParams));
    }

    private String generateItemParams(Set<String> items) {
        StringBuilder sb=new StringBuilder();
        for(String item:items){
            if(sb.length()==0){
                sb.append("?");
            } else {
                sb.append("&");
            }
            sb.append("stalone");
            sb.append("=");
            sb.append(item);
        }
        return sb.toString();
    }

    public ItemResponse fetchItem(Set items) {
        try {
            return innerFetchItem(items);
        } catch (Exception e){
            return new ItemResponse("Failed2access",e.getMessage());
        }
    }
    public ItemResponse innerFetchItem(Set items) throws Exception{
        String response = performGet(generateItemUrl(items));
        return new ItemResponse(response);
    }
}

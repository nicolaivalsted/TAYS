package dk.yousee.randy.servicemap;

import dk.yousee.randy.base.AbstractClient;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * User: aka
 * Date: 06/09/12
 * Time: 23.45
 * Client to access service map
 */
public class SmClient extends AbstractClient<SmConnectorImpl> {


    URL generateVendorlUrl() throws MalformedURLException {
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
        String response = performGet(generateVendorlUrl());
        return new Vendors(response);
    }

}

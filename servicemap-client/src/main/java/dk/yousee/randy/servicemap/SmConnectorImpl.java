package dk.yousee.randy.servicemap;

import dk.yousee.randy.base.AbstractConnector;

/**
 * User: aka
 * Date: 09/04/12
 * Time: 14.46
 * Class that helps managing HttpClient
 */
public class SmConnectorImpl extends AbstractConnector {

    public static final String DEV_HOST="http://localhost:9993";
    public static final String T_HOST="http://ttays.yousee.tv";
    public static final String P_HOST="http://ptays.yousee.tv";
    private String serviceMapHost;

    public String getServiceMapHost() {
        if(serviceMapHost==null){
            serviceMapHost=DEV_HOST;
        }
        return serviceMapHost;
    }

    public void setServiceMapHost(String serviceMapHost) {
        this.serviceMapHost = serviceMapHost;
    }

    public SmConnectorImpl() {
        super();
    }

}


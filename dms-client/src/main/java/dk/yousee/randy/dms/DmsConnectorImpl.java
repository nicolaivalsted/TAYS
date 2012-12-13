package dk.yousee.randy.dms;

import dk.yousee.randy.base.AbstractConnector;

/**
 * User: aka
 * Date: 09/04/12
 * Time: 14.46
 * Class that helps managing HttpClient
 */
public class DmsConnectorImpl extends AbstractConnector {

    public static final String LOCAL_HOST="http://localhost:8080";
    public static final String DEV_HOST="http://localhost:9993";
    public static final String T_HOST="http://ttays.yousee.tv";
    public static final String P_HOST="http://ptays.yousee.tv";
    private String dmsHost;

    public String getDmsHost() {
        if(dmsHost ==null){
            dmsHost =LOCAL_HOST;
        }
        return dmsHost;
    }

    public void setDmsHost(String dmsHost) {
        this.dmsHost = dmsHost;
    }

    public DmsConnectorImpl() {
        super();
    }

}


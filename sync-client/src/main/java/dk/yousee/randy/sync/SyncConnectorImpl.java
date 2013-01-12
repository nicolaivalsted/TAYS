package dk.yousee.randy.sync;

import dk.yousee.randy.base.AbstractConnector;

/**
 * User: aka
 * Date: 11/01/13
 * Class that helps managing HttpClient
 */
public class SyncConnectorImpl extends AbstractConnector {

    public static final String DEV_HOST="http://localhost:9997";
    public static final String T_HOST="http://ttays.yousee.tv";
    public static final String P_HOST="http://ptays.yousee.tv";
    private String syncHost;

    public String getSyncHost() {
        if(syncHost ==null){
            syncHost =DEV_HOST;
        }
        return syncHost;
    }

    public void setSyncHost(String syncHost) {
        this.syncHost = syncHost;
    }

    public SyncConnectorImpl() {
        super();
    }

}


package dk.yousee.randy.kasia;

import dk.yousee.randy.base.AbstractConnector;

/**
 * User: aka
 * Date: 09/04/12
 * Time: 14.46
 * Class that helps managing HttpClient
 */
public class KasiaConnectorImpl extends AbstractConnector {


    public static final String PREPROD_KASIA_HOST="http://preprod-kasia.yousee.dk";
    public static final String KASIA_HOST="http://kasia.yousee.dk";
    private String kasiaHost;

    public String getKasiaHost() {
        return kasiaHost;
    }

    public void setKasiaHost(String kasiaHost) {
        this.kasiaHost = kasiaHost;
    }

    public KasiaConnectorImpl() {
        super();
    }

}


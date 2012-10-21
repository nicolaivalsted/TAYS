package dk.yousee.randy.kasia;

import dk.yousee.randy.base.AbstractClient;

/**
 * User: aka
 * Date: 19/10/12
 * Time: 08.54
 * Doing business with Kasia, use this as a base
 */
public abstract class AbstractKasiaClient extends AbstractClient<KasiaConnectorImpl> {

    /**
     * @return normal "Accept" value for kasia applications
     */
    protected String getDefaultMediaType() {
        return "application/vnd.yousee.kasia2+json;version=1;charset=UTF-8";
    }
}

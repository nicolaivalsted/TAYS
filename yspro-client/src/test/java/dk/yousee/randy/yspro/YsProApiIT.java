package dk.yousee.randy.yspro;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author m27236
 */
public class YsProApiIT {
    
    private static YsProApi api;
    private static ProStoreConnectorImpl connector;
    
    @BeforeClass
    public static void before() {
        connector = new ProStoreConnectorImpl();
        connector.setYsProHost(ProStoreConnectorImpl.YSPRO_HOST);
        connector.setSystemLogin(connector.getSystemLogin());
        connector.setSystemPassword("We4rAndy");
        connector.setOperationTimeout(20000);
        
        api = new YsProApi();
        api.setClient(connector);
    }
    
    @Test
    public void testGetmailInfo() throws YsProException{
        final String kpm = "120108063922";
        
        ProStoreResponse psr = api.findMailGetInfo(kpm);
        Assert.assertNotNull(psr);
        
        Assert.assertTrue(psr.getStatus()==0);
    }
    
    
}

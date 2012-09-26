package dk.yousee.smp.casemodel.vo.helpers;

import dk.yousee.smp.casemodel.SubscriberModel;
import dk.yousee.smp.casemodel.vo.ModemId;
import dk.yousee.smp.order.model.Acct;
import dk.yousee.smp.order.model.ServiceProviderEnum;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by IntelliJ IDEA.
 * User: aka
 * Date: Nov 9, 2010
 * Time: 3:27:55 PM
 * Test of key generation with different providers
 */
public class KeyTest {

    private SubscriberModel model=new SubscriberModel(new Acct("1234"));
    private ModemId modemId;

    @Before
    public void setUp(){
        model=new SubscriberModel(new Acct("1234"));
        modemId=new ModemId("123456");
    }
    @Test
    public void defaultProvider() {
        Key key=new Key(model);
        Assert.assertEquals(model.getProvider()+":cpe_"+modemId,key.CpeComposedService(modemId));
    }
    @Test
    public void otherProvider() {
        model.setProvider(ServiceProviderEnum.Telia);
        Key key=new Key(model);
        Assert.assertEquals(ServiceProviderEnum.Telia+":cpe_"+modemId,key.CpeComposedService(modemId));
    }
}

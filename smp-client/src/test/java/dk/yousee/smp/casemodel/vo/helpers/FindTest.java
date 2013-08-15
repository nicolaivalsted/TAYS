package dk.yousee.smp.casemodel.vo.helpers;

import dk.yousee.smp.casemodel.SubscriberModel;
import dk.yousee.smp.casemodel.vo.ModemId;
import dk.yousee.smp.casemodel.vo.PhoneNumber;
import dk.yousee.smp.casemodel.vo.cbp.CableBBService;
import dk.yousee.smp.casemodel.vo.cbp.InetAccess;
import dk.yousee.smp.casemodel.vo.cpee.CpeComposedService;
import dk.yousee.smp.casemodel.vo.cpee.HsdAccess;
import dk.yousee.smp.casemodel.vo.cvp.CableVoiceService;
import dk.yousee.smp.casemodel.vo.cvp.DialToneAccess;
import dk.yousee.smp.order.model.Acct;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Created with IntelliJ IDEA.
 * User: aka
 * Date: 26/04/12
 * Time: 10.09
 * Test that finders works
 */
public class FindTest {

    private SubscriberModel model = null;
    private ModemId modemId;

    @Before
    public void before() {
        model = new SubscriberModel(new Acct("123456789"));
        modemId = ModemId.create("1000111122");
    }


    @Test
    public void CableBBService() {
        // parse a compose service
        CableBBService service = new CableBBService(model, model.key().CableBBService(modemId));
        Find find = new Find(model, model.getServiceLevelUnit());

        // check composed services
        Assert.assertEquals("Must contain 1 element", 1, find.CableBBService().size());

        // check that no modem is found yet
        CableBBService selected1 = find.CableBBService(modemId);
        Assert.assertNotNull("Modem is identified by external key, therefore we can look it up now", selected1);

        InetAccess access = new InetAccess(model, model.key().generateUUID(), service);
        ModemId modem2=ModemId.create("1000999900");
        access.setModemId(modem2);

        CableBBService selected2 = find.CableBBService(modemId);
        Assert.assertNull("external key now contains wrong modem", selected2);

        CableBBService selected3 = find.CableBBService(modem2);
        Assert.assertTrue("Modem is identified by modem2, therefore we can look it up now", selected3==service);
    }

    @Test
    public void CpeComposedService() {
        // parse a compose service
        CpeComposedService service = new CpeComposedService(model, model.key().CpeComposedService(modemId));
        Find find = new Find(model, model.getServiceLevelUnit());

        // check composed services
        Assert.assertEquals("Must contain 1 element", 1, find.CpeComposedService().size());

        // check that no modem is found yet
        CpeComposedService selected1 = find.CpeComposedService(modemId);
        Assert.assertNull("Not yet found a modem", selected1);

        HsdAccess access = new HsdAccess(model, model.key().generateUUID(), service);
        access.setCmOwnership(modemId);

        CpeComposedService selected2 = find.CpeComposedService(modemId);
        Assert.assertTrue("must have selected the service", selected2 == service);
    }

    @Test
    public void CableVoiceService() {
        // parse a compose service
        CableVoiceService service = new CableVoiceService(model, model.key().CableVoiceService(modemId.getId()));
        Find find = new Find(model, model.getServiceLevelUnit());

        // check composed services
        Assert.assertEquals("Must contain 1 element", 1, find.CableVoiceService().size());

        CableVoiceService selected1 = find.CableVoiceService(modemId);
        Assert.assertNotNull("Modem is identified by external key, therefore we can look it up now", selected1);

        PhoneNumber phone = PhoneNumber.create("458774897");
        CableVoiceService selected2 = find.CableVoiceService(phone);
        Assert.assertNull("Not yet found a voice, phone not connected", selected2);

        DialToneAccess dta = new DialToneAccess(model, model.key().generateUUID(), service);
        dta.setPhoneNumber(phone);

        CableVoiceService selected3 = find.CableVoiceService(phone);
        Assert.assertTrue("voip is identified by phone, therefore we can look it up now", service == selected3);
        CableVoiceService selected4 = find.CableVoiceService(PhoneNumber.create("12345678"));
        Assert.assertNull("Not that voip", selected4);
    }

}

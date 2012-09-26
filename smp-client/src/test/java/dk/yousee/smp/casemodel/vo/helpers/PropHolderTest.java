package dk.yousee.smp.casemodel.vo.helpers;

import dk.yousee.smp.casemodel.SubscriberModel;
import dk.yousee.smp.order.model.OrderDataLevel;
import dk.yousee.smp.order.model.OrderDataType;
import dk.yousee.smp.order.model.Response;
import dk.yousee.smp.order.model.ResponseEntity;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: aka
 * Date: Nov 5, 2010
 * Time: 9:38:51 AM
 * testing PropHolder in various important cases around make an order or not
 */
public class PropHolderTest {

    private SubscriberModel model;
    private BasicUnit basicUnit;
    private OrderDataType type;
    private Response response;
    private String externalKey = "externalkey";

    @Before
    public void setup() {
        response = new Response();
        type = new OrderDataType("horse");
    }

    /**
     * No existing service plan should produce an order
     */
    @Test
    public void updateValue_without_existing_service_plan() {
        model = new SubscriberModel(response);
        basicUnit = new BasicUnit(model, externalKey, type, OrderDataLevel.SERVICE, null) {
        };
        PropHolder test = new PropHolder(basicUnit, "key");
        boolean upd = test.updateValue(externalKey);
        Assert.assertTrue("Expects update", upd);
        Assert.assertTrue("When we have an update of the model, an order is expected", model.hasOrder());
    }

    /**
     * It is important usage that same value makes no update
     */
    @Test
    public void updateValue_with_existing_service_plan_same_value() {
        response.setSmp(new ResponseEntity());
        ResponseEntity re = new ResponseEntity();
        Map<String, String> params = new HashMap<String, String>();
        params.put("key", externalKey);
        re.setParams(params);
        re.setValue(type);
        re.setExternalKey(externalKey);
        re.getLevel();
        response.getSmp().getEntities().add(re);
        model = new SubscriberModel(response);
        basicUnit = new BasicUnit(model, externalKey, type, OrderDataLevel.SERVICE, null) {
        };
        PropHolder test = new PropHolder(basicUnit, "key");
        boolean upd = test.updateValue(externalKey);
        Assert.assertFalse("Expects no update", upd);
        Assert.assertFalse("When we have NO update of the model, NO order is expected", model.hasOrder());
    }

    /**
     * It is important usage that a value, but other value makes update
     */
    @Test
    public void updateValue_with_existing_service_plan_other_value() {
        response.setSmp(new ResponseEntity());
        ResponseEntity re = new ResponseEntity();
        Map<String, String> params = new HashMap<String, String>();
        params.put("key", externalKey);
        re.setParams(params);
        re.setValue(type);
        re.setExternalKey(externalKey);
        response.getSmp().getEntities().add(re);
        model = new SubscriberModel(response);
        basicUnit = new BasicUnit(model, externalKey, type, OrderDataLevel.SERVICE, null) {
        };
        PropHolder test = new PropHolder(basicUnit, "key");
        boolean upd = test.updateValue("other-value");
        Assert.assertTrue("Expects update", upd);
        Assert.assertTrue("When we have an update of the model, an order is expected", model.hasOrder());
    }
}

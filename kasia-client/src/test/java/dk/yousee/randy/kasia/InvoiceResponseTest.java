package dk.yousee.randy.kasia;

import org.junit.Assert;
import org.junit.Test;

/**
 * User: aka
 * Date: 19/10/12
 * Time: 10.37
 * Test that we can understand a response
 */
public class InvoiceResponseTest {



    @Test
    public void construct_good() throws Exception {
        String uuid="f6f14f73-415a-4c30-a94c-bd74ef2d8da5";
        String response=String.format("{\"order-output\":{\"uuid\":\"%s\"}}",uuid);

        InvoiceResponse test=new InvoiceResponse("",response);
        Assert.assertNotNull(test);
        Assert.assertNotNull(test.getJsonSource());
        Assert.assertNull("No error",test.getMessage());
        Assert.assertEquals(uuid,test.getOrderOutput().getUuid());
    }
    @Test
    public void construct_bad() throws Exception {
        InvoiceResponse test=new InvoiceResponse("Could not find server...","");
        Assert.assertNotNull(test);
        Assert.assertNull("makes no json source",test.getJsonSource());
        Assert.assertNotNull("Now there is error",test.getMessage());
    }

    @Test
    public void construct_notJson() throws Exception {
        InvoiceResponse test=new InvoiceResponse("","h=7");
        Assert.assertNotNull(test);
        Assert.assertNull("makes no json source",test.getJsonSource());
        String message=test.getMessage();
        Assert.assertNotNull("Now there is error",message);
    }
    @Test
    public void construct_missingElements() throws Exception {
        String uuid="f6f14f73-415a-4c30-a94c-bd74ef2d8da5";
        String response=String.format("{\"fejl\":{\"uuid\":\"%s\"}}",uuid);

        InvoiceResponse test=new InvoiceResponse("",response);
        Assert.assertNotNull(test);
        Assert.assertNotNull("still got json",test.getJsonSource());
        Assert.assertNotNull("An error, missing something..",test.getMessage());
    }
}

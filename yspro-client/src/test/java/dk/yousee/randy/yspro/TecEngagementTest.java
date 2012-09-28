package dk.yousee.randy.yspro;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Created with IntelliJ IDEA.
 * User: aka
 * Date: 07/09/12
 * Time: 14.50
 * Test load of technical engagement from YsPro
 */
public class TecEngagementTest {


    String input;
    @Before
    public void setUp() throws Exception {

        input="{\n" +
            "  \"Status\": 0,\n" +
            "  \"Message\": \"OK\",\n" +
            "  \"DateTime\": \"2012-09-13 21:51:32\",\n" +
            "  \"Products\": [\n" +
            "    {\n" +
            "      \"From\": \"2012-09-13 00:00:00\",\n" +
            "      \"To\": \"2012-09-13 14:33:00\",\n" +
            "      \"UUID\": \"4cf9cfa3-6f09-4bcc-9fcb-c360577e3a73\",\n" +
            "      \"ProductID\": \"7000\",\n" +
            "      \"Description\": \"two\",\n" +
            "      \"BusinessPosition\": \"crm_key_update\",\n" +
            "      \"ServiceItem\": \"stalone_update\",\n" +
            "      \"OttProduct\": \"crudCycleProduct\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"From\": \"2012-09-13 21:28:07\",\n" +
            "      \"To\": \"9999-12-31 23:59:59\",\n" +
            "      \"UUID\": \"5ef6fb43-0742-4d14-8c04-129cb4d6a034\",\n" +
            "      \"ProductID\": \"6900\",\n" +
            "      \"HW_Version\": \"7\",\n" +
            "      \"Device_Mac\": \"12:34:56:78:90:AB\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"From\": \"2012-09-13 00:00:00\",\n" +
            "      \"To\": \"2012-09-13 14:33:00\",\n" +
            "      \"UUID\": \"8dce9924-ff67-4265-a652-ab744c9411a5\",\n" +
            "      \"ProductID\": \"7000\",\n" +
            "      \"OttProduct\": \"crudCycleProduct\",\n" +
            "      \"BusinessPosition\": \"crm_key\",\n" +
            "      \"ServiceItem\": \"stalone123\"\n" +
            "    }\n" +
            "  ]\n" +
            "}";

//        input="{\n" +
//            "  \"Status\": \"0\",\n" +
//            "  \"DateTime\": \"2012-09-06 21:31:07\",\n" +
//            "  \"CalledFunction\": \"GetUserProductData\",\n" +
//            "  \"Message\": \"OK\",\n" +
//            "  \"Data\": [\n" +
//            "    {\n" +
//            "      \"ProductID\": \"7000\",\n" +
//            "      \"From\": \"2012-09-03 18:00:00\",\n" +
//            "      \"To\": \"2012-10-03 17:59:59\",\n" +
//            "      \"DataName\": \"BusinessPosition\",\n" +
//            "      \"Value\": \"1023\",\n" +
//            "      \"PairNumber\": \"7\",\n" +
//            "      \"UUID\": \"d179b909-cd29-4195-8756-ab75bbc8cd89\"\n" +
//            "    },\n" +
//            "    {\n" +
//            "      \"ProductID\": \"7000\",\n" +
//            "      \"From\": \"2012-09-03 18:00:00\",\n" +
//            "      \"To\": \"2012-10-03 17:59:59\",\n" +
//            "      \"DataName\": \"Description\",\n" +
//            "      \"Value\": \"Another test...\",\n" +
//            "      \"PairNumber\": \"7\",\n" +
//            "      \"UUID\": \"d179b909-cd29-4195-8756-ab75bbc8cd89\"\n" +
//            "    },\n" +
//            "    {\n" +
//            "      \"ProductID\": \"7000\",\n" +
//            "      \"From\": \"2012-09-03 18:30:00\",\n" +
//            "      \"To\": \"2012-10-03 17:59:59\",\n" +
//            "      \"DataName\": \"BusinessPosition\",\n" +
//            "      \"Value\": \"0101\",\n" +
//            "      \"PairNumber\": \"1\",\n" +
//            "      \"UUID\": \"42\"\n" +
//            "    },\n" +
//            "    {\n" +
//            "      \"ProductID\": \"7000\",\n" +
//            "      \"From\": \"2012-09-03 18:30:00\",\n" +
//            "      \"To\": \"2012-10-03 17:59:59\",\n" +
//            "      \"DataName\": \"Description\",\n" +
//            "      \"Value\": \"My Test...\",\n" +
//            "      \"PairNumber\": \"1\",\n" +
//            "      \"UUID\": \"42\"\n" +
//            "    },\n" +
//            "    {\n" +
//            "      \"ProductID\": \"7000\",\n" +
//            "      \"From\": \"2012-09-03 18:30:00\",\n" +
//            "      \"To\": \"2012-10-03 17:59:59\",\n" +
//            "      \"DataName\": \"ServiceItem\",\n" +
//            "      \"Value\": \"qos1\",\n" +
//            "      \"PairNumber\": \"1\",\n" +
//            "      \"UUID\": \"42\"\n" +
//            "    },\n" +
//            "    {\n" +
//            "      \"ProductID\": \"7000\",\n" +
//            "      \"From\": \"2012-09-03 18:30:00\",\n" +
//            "      \"To\": \"2012-10-03 17:59:59\",\n" +
//            "      \"DataName\": \"StaloneID\",\n" +
//            "      \"Value\": \"123456789\",\n" +
//            "      \"PairNumber\": \"1\",\n" +
//            "      \"UUID\": \"42\"\n" +
//            "    },\n" +
//            "    {\n" +
//            "      \"ProductID\": \"7000\",\n" +
//            "      \"From\": \"2012-09-03 18:30:00\",\n" +
//            "      \"To\": \"2012-10-03 17:59:59\",\n" +
//            "      \"DataName\": \"BusinessPosition\",\n" +
//            "      \"Value\": \"0109\",\n" +
//            "      \"PairNumber\": \"2\",\n" +
//            "      \"UUID\": \"43\"\n" +
//            "    },\n" +
//            "    {\n" +
//            "      \"ProductID\": \"7000\",\n" +
//            "      \"From\": \"2012-09-03 18:30:00\",\n" +
//            "      \"To\": \"2012-10-03 17:59:59\",\n" +
//            "      \"DataName\": \"Description\",\n" +
//            "      \"Value\": \"My Test...\",\n" +
//            "      \"PairNumber\": \"2\",\n" +
//            "      \"UUID\": \"43\"\n" +
//            "    },\n" +
//            "    {\n" +
//            "      \"ProductID\": \"7000\",\n" +
//            "      \"From\": \"2012-09-03 18:30:00\",\n" +
//            "      \"To\": \"2012-10-03 17:59:59\",\n" +
//            "      \"DataName\": \"ServiceItem\",\n" +
//            "      \"Value\": \"qos1\",\n" +
//            "      \"PairNumber\": \"2\",\n" +
//            "      \"UUID\": \"43\"\n" +
//            "    },\n" +
//            "    {\n" +
//            "      \"ProductID\": \"7000\",\n" +
//            "      \"From\": \"2012-09-03 18:30:00\",\n" +
//            "      \"To\": \"2012-10-03 17:59:59\",\n" +
//            "      \"DataName\": \"StaloneID\",\n" +
//            "      \"Value\": \"123456789\",\n" +
//            "      \"PairNumber\": \"2\",\n" +
//            "      \"UUID\": \"43\"\n" +
//            "    }\n" +
//            "  ]\n" +
//            "}";
    }

    @Test
    public void createFromDetils() throws Exception {
        TecEngagement test=TecEngagement.create(input);
        Assert.assertNotNull(test);
        Assert.assertEquals("Expects to parse to x products",3,test.getProducts().size());
    }
}

package dk.yousee.smp.com;

import dk.yousee.smp.order.model.QueryOrderReply;
import dk.yousee.smp.order.model.SmpXml;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Created with IntelliJ IDEA.
 * User: aka
 * Date: 05/06/12
 * Time: 13.49
 * Test generation of query / parsing
 */
public class FindOrderComTest {

    FindOrderCom test = new FindOrderCom();

    private String ordreId = null;
    private String response = null;

    @Test
    public void pack() {
        String request = test.convertRequest(ordreId);
        Assert.assertNotNull(request);
        Assert.assertTrue(request.contains(ordreId));
    }

    @Test
    public void parse() {
        SmpXml xml = new SmpXml(test.convertRequest(ordreId), response);
        QueryOrderReply reply = test.convertResponse(xml, ordreId);
        Assert.assertNotNull(reply);
    }

    @Before
    public void before() {
        ordreId = "4892411";
        response = "<sa:getOrderByKeyResponse xmlns:sa=\"http://java.sun.com/products/oss/xml/ServiceActivation\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:cmn=\"http://java.sun.com/products/oss/xml/Common\" xmlns:smpcbe=\"http://www.sigma-systems.com/schemas/3.1/SmpCBECoreSchema\" xmlns:smpcmn=\"http://www.sigma-systems.com/schemas/3.1/SmpCommonValues\" xmlns:smpsa=\"http://www.sigma-systems.com/schemas/3.1/SmpServiceActivationSchema\" xmlns:cbesvc=\"http://java.sun.com/products/oss/xml/CBE/Service\">\n" +
            "    <sa:orderValue xmlns:smp=\"http://www.sigma-systems.com/schemas/3.1/SmpServiceActivationSchema\" xsi:type=\"smp:ActionOrderValue\">\n" +
            "        <cmn:lastUpdateVersionNumber>2</cmn:lastUpdateVersionNumber>\n" +
            "        <sa:actualCompletionDate>2012-06-05T14:07:15.000+02:00</sa:actualCompletionDate>\n" +
            "        <sa:apiClientId>triple</sa:apiClientId>\n" +
            "        <sa:orderKey xsi:type=\"smp:OrderKeyType\">\n" +
            "            <cmn:type>com.sigma.samp.cmn.order.SampOrderKey</cmn:type>\n" +
            "            <sa:primaryKey>4892411</sa:primaryKey>\n" +
            "        </sa:orderKey>\n" +
            "        <sa:priority>10</sa:priority>\n" +
            "        <sa:orderState smpState=\"closed.completed.all\" xsi:type=\"smp:SubOrderStateType\">closed.completed</sa:orderState>\n" +
            "        <smp:subKey>\n" +
            "            <cmn:type>SubSpec:-</cmn:type>\n" +
            "            <smpcbe:primaryKey>691449</smpcbe:primaryKey>\n" +
            "        </smp:subKey>\n" +
            "        <smp:orderParamList>\n" +
            "            <smpcmn:param name=\"has_groups\">yes</smpcmn:param>\n" +
            "            <smpcmn:param name=\"has_impact\">y</smpcmn:param>\n" +
            "        </smp:orderParamList>\n" +
            "        <smp:orderItemList>\n" +
            "            <smp:orderItem>\n" +
            "                <smp:orderItemKey>\n" +
            "                    <cmn:type>com.sigma.samp.cmn.order.SampOrderLineItemValue</cmn:type>\n" +
            "                    <sa:primaryKey>12072739</sa:primaryKey>\n" +
            "                </smp:orderItemKey>\n" +
            "                <smp:action>update</smp:action>\n" +
            "                <smp:itemParamList>\n" +
            "                    <smpcmn:param name=\"ignored\">n</smpcmn:param>\n" +
            "                    <smpcmn:param name=\"has_impact\">n</smpcmn:param>\n" +
            "                </smp:itemParamList>\n" +
            "                <smpsa:entityKey xmlns:smp=\"http://www.sigma-systems.com/schemas/3.1/SmpCBEServiceSchema\" xsi:type=\"smp:SubSvcKeyType\">\n" +
            "                    <cmn:type>SubSvcSpec:smp_voice_line</cmn:type>\n" +
            "                    <smp:primaryKey>93736749</smp:primaryKey>\n" +
            "                </smpsa:entityKey>\n" +
            "                <smpsa:entityValue xmlns:smp=\"http://www.sigma-systems.com/schemas/3.1/SmpCBEServiceSchema\" xsi:type=\"smp:SubSvcType\">\n" +
            "                    <cmn:lastUpdateVersionNumber>-1</cmn:lastUpdateVersionNumber>\n" +
            "                    <cbesvc:serviceState xmlns:smp=\"http://www.sigma-systems.com/schemas/3.1/SmpServiceActivationSchema\" provisionState=\"inactive\" xsi:type=\"smp:SubSvcStateType\">inactive</cbesvc:serviceState>\n" +
            "                    <smp:serviceKey>\n" +
            "                        <cmn:type>SubSvcSpec:smp_voice_line</cmn:type>\n" +
            "                        <smp:primaryKey>93736749</smp:primaryKey>\n" +
            "                        <smp:externalKey/>\n" +
            "                    </smp:serviceKey>\n" +
            "                </smpsa:entityValue>\n" +
            "                <smp:itemState>closed.completed.all</smp:itemState>\n" +
            "                <smp:auditInfo>\n" +
            "                    <smpcmn:createdDateTime>2012-06-05T14:07:14.000+02:00</smpcmn:createdDateTime>\n" +
            "                    <smpcmn:createdBy>samp.csra1</smpcmn:createdBy>\n" +
            "                </smp:auditInfo>\n" +
            "            </smp:orderItem>\n" +
            "            <smp:orderItem>\n" +
            "                <smp:orderItemKey>\n" +
            "                    <cmn:type>com.sigma.samp.cmn.order.SampOrderLineItemValue</cmn:type>\n" +
            "                    <sa:primaryKey>12072738</sa:primaryKey>\n" +
            "                </smp:orderItemKey>\n" +
            "                <smp:action>update</smp:action>\n" +
            "                <smp:itemParamList>\n" +
            "                    <smpcmn:param name=\"ignored\">n</smpcmn:param>\n" +
            "                    <smpcmn:param name=\"has_impact\">y</smpcmn:param>\n" +
            "                </smp:itemParamList>\n" +
            "                <smpsa:entityKey xmlns:smp=\"http://www.sigma-systems.com/schemas/3.1/SmpCBEServiceSchema\" xsi:type=\"smp:SubSvcKeyType\">\n" +
            "                    <cmn:type>SubSvcSpec:smp_switch_dial_tone_access</cmn:type>\n" +
            "                    <smp:primaryKey>93736799</smp:primaryKey>\n" +
            "                </smpsa:entityKey>\n" +
            "                <smpsa:entityValue xmlns:smp=\"http://www.sigma-systems.com/schemas/3.1/SmpCBEServiceSchema\" xsi:type=\"smp:SubSvcType\">\n" +
            "                    <cmn:lastUpdateVersionNumber>-1</cmn:lastUpdateVersionNumber>\n" +
            "                    <cbesvc:serviceState xmlns:smp=\"http://www.sigma-systems.com/schemas/3.1/SmpServiceActivationSchema\" provisionState=\"inactive\" xsi:type=\"smp:SubSvcStateType\">inactive</cbesvc:serviceState>\n" +
            "                    <smp:serviceKey>\n" +
            "                        <cmn:type>SubSvcSpec:smp_switch_dial_tone_access</cmn:type>\n" +
            "                        <smp:primaryKey>93736799</smp:primaryKey>\n" +
            "                        <smp:externalKey/>\n" +
            "                    </smp:serviceKey>\n" +
            "                    <smp:paramList>\n" +
            "                        <smpcbe:param name=\"privacy\">NONE</smpcbe:param>\n" +
            "                        <smpcbe:oldParam name=\"privacy\">none</smpcbe:oldParam>\n" +
            "                    </smp:paramList>\n" +
            "                </smpsa:entityValue>\n" +
            "                <smp:itemState>closed.completed.all</smp:itemState>\n" +
            "                <smp:auditInfo>\n" +
            "                    <smpcmn:createdDateTime>2012-06-05T14:07:15.000+02:00</smpcmn:createdDateTime>\n" +
            "                    <smpcmn:createdBy>guest</smpcmn:createdBy>\n" +
            "                </smp:auditInfo>\n" +
            "            </smp:orderItem>\n" +
            "        </smp:orderItemList>\n" +
            "        <smp:auditInfo>\n" +
            "            <smpcmn:createdDateTime>2012-06-05T14:07:15.000+02:00</smpcmn:createdDateTime>\n" +
            "            <smpcmn:createdBy>guest</smpcmn:createdBy>\n" +
            "        </smp:auditInfo>\n" +
            "    </sa:orderValue>\n" +
            "</sa:getOrderByKeyResponse>";
    }
}

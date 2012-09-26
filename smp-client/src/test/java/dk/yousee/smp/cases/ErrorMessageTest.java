package dk.yousee.smp.cases;

import org.junit.Ignore;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;
import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: m14857
 * Date: Feb 22, 2011
 * Time: 2:42:33 PM
 * To change this template use File | Settings | File Templates.
 */
//@Ignore
public class ErrorMessageTest {

     public String errorMessageHandler(String original, String detailError ){
        try {
            DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder;
            docBuilder = docBuilderFactory.newDocumentBuilder();
            Document document = docBuilder.parse(new InputSource(new StringReader(detailError)));
            NodeList nodeList = document.getElementsByTagName("smp:errorCode");
            ArrayList codelist = new ArrayList();
            ArrayList messagelist = new ArrayList();

            for (int s = 0; s < nodeList.getLength(); s++) {
                Node node = nodeList.item(s);
                String name = node.getNodeName();
                String value = node.getFirstChild().getNodeValue();
                codelist.add(value);
            }

            NodeList nodeList2 = document.getElementsByTagName("smp:errorMessage");
            for (int s = 0; s < nodeList2.getLength(); s++) {
                Node node = nodeList2.item(s);
                String name = node.getNodeName();
                String value = node.getFirstChild().getNodeValue();
                messagelist.add(value);
            }

            for (int i = 0; i < codelist.size(); i++) {
                original = original+ "\n" + "errorcode = " + (String)codelist.get(i) + "  errorMessage = " + (String)messagelist.get(i) ;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return original;
    }

    @Test
    public void TestErrorMessage() {
        String detailError = "<smpsa:executeOrderException xmlns:smpsa=\"http://www.sigma-systems.com/schemas/3.1/SmpServiceActivationSchema\" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:cmn=\"http://java.sun.com/products/oss/xml/Common\">\n" +
                "  <smpsa:illegalArgumentException xmlns:smp=\"http://www.sigma-systems.com/schemas/3.1/SmpCommonValues\" xsi:type=\"smp:SmpIllegalArgumentException\">\n" +
                "    <cmn:message>Sub profile validation failed.</cmn:message>\n" +
                "    <smp:applicationErrors>\n" +
                "      <smp:item>\n" +
                "        <smp:errorCode resourceNm=\"ApplError\">VLDERR_VALUE_NOT_UNIQUE</smp:errorCode>\n" +
                "        <smp:errorParamList>\n" +
                "          <smp:param resourceNm=\"SvcParmSpec\">sim_card.sim_card_id</smp:param>\n" +
                "          <smp:param resourceNm=\"\">89450100080724000528</smp:param>\n" +
                "        </smp:errorParamList>\n" +
                "        <smp:errorMessage xml:lang=\"en_CA\">Duplicate entry has detected for \"SIM Card Id\" parameter; each entry for this parameter must be unique</smp:errorMessage>\n" +
                "        <smp:gRef localAppEnv=\"\" entityClassNm=\"SubSvc\" entityKey=\"2374286\"/>\n" +
                "      </smp:item>\n" +
                "      <smp:item>\n" +
                "        <smp:errorCode resourceNm=\"Error\">ORDER_PROCESSING_FAILED_ERROR_ORDER_KEY</smp:errorCode>\n" +
                "        <smp:errorParamList>\n" +
                "          <smp:param resourceNm=\"\">125408</smp:param>\n" +
                "        </smp:errorParamList>\n" +
                "        <smp:errorMessage xml:lang=\"en_CA\">ORDER_PROCESSING_FAILED_ERROR_ORDER_KEY</smp:errorMessage>\n" +
                "      </smp:item>\n" +
                "    </smp:applicationErrors>\n" +
                "  </smpsa:illegalArgumentException>\n" +
                "</smpsa:executeOrderException>";

        String returnedMessage = errorMessageHandler("mu", detailError);
        System.out.println(returnedMessage);
    }
}

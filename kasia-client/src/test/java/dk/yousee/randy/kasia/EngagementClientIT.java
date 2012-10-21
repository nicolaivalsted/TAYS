package dk.yousee.randy.kasia;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.net.URL;

public class EngagementClientIT {

    private EngagementClient client;

    @Before
    public void before() {
        KasiaConnectorImpl connector = new KasiaConnectorImpl();
        connector.setOperationTimeout(20000);
        client = new EngagementClient();
        client.setConnector(connector);
    }

    @Test
    public void parse() throws Exception {
        URL url = new URL("http://preprod-kasia.yousee.dk/abonnement/provisionering/614010743");
        String json = client.findEngagement(url, "application/vnd.yousee.kasia2+json;version=1;charset=UTF-8");
        Assert.assertNotNull("expects a string", json);
        Assert.assertTrue("must have a substring called 'produkter'", json.contains("produkter"));
        if (json.contains("\\")) {
            json = json.replace("\\", "");
            if (json.startsWith("\"")) {
                json = json.substring(1);
            }
            if (json.endsWith("\"")) {
                json = json.substring(0, json.length() - 1);
            }
        }
        JsonElement element = new JsonParser().parse(json);
        Assert.assertNotNull("expects element", element);
    }

}

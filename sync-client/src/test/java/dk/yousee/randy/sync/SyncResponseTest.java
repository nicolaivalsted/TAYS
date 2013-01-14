package dk.yousee.randy.sync;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created with IntelliJ IDEA.
 * User: aka
 * Date: 12/01/13
 * Time: 12.41
 * Building up the response
 */
public class SyncResponseTest {


    @Test
    public void construct1() throws Exception {

        String input="{\"id\":1,\"subscriber\":\"608301280\"\n" +
            ",\"complete\":false" +
            ",\"links\":[{\"href\":\"http://localhost:9997/sync/api/sync/1\",\"rel\":\"process\",\"type\":\"PUT\"}" +
            ",{\"href\":\"http://localhost:9997/sync/api/sync/1\",\"rel\":\"query\",\"type\":\"GET\"}]}\n";

        SyncResponse response=new SyncResponse(null,input);
        Assert.assertNotNull(response);
        Assert.assertEquals("PUT",response.getProcessLink().getType());
        Assert.assertFalse(response.isComplete());
        Assert.assertEquals("1",response.getId());
    }

    @Test
    public void construct2() throws Exception {
        String input="{\"id\":133,\"subscriber\":\"608301280\"\n" +
            ",\"complete\":true" +
            ",\"links\":[" +
            "{\"href\":\"http://localhost:9997/sync/api/sync/1\",\"rel\":\"query\",\"type\":\"GET\"}]" +
            "            ,\"operations\":[" +
            "               {\"id\":1,\"type\":\"engagement\"\n" +
            "            ,\"error\":\"SOMETHING\"\n" +
            "            ,\"message\":\"an message\"}" +
            "              ,{\"id\":2,\"type\":\"engagement\"\n" +
            "            ,\"error\":\"FETCH_ENGAGEMENT\"\n" +
            "            ,\"message\":\"status:404 ,phrase:Status not handled, Not Found\"}" +
            "               ]\n"+
            "}";
        SyncResponse response=new SyncResponse(null,input);
        Assert.assertNotNull(response);
        Assert.assertNull(response.getProcessLink());
        Assert.assertEquals("GET", response.getQueryLink().getType());
        Assert.assertTrue(response.isComplete());
        Assert.assertEquals("FETCH_ENGAGEMENT",response.getError());
        Assert.assertTrue(response.getMessage().contains("Not Found"));
        Assert.assertEquals("133",response.getId());
    }
}

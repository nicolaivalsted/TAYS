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
    public void construct3() throws Exception {
        String input="{\n" + 
        		"   \"forbruger\": \"612448541\",\n" + 
        		"   \"produkter\": [\n" + 
        		"       {\n" + 
        		"           \"id\": \"105938087\",\n" + 
        		"           \"service_items\": [\n" + 
        		"               \"1302102\"\n" + 
        		"           ],\n" + 
        		"           \"signal\": false\n" + 
        		"       },\n" + 
        		"       {\n" + 
        		"           \"id\": \"105938087\",\n" + 
        		"           \"service_items\": [\n" + 
        		"               \"1313108\"\n" + 
        		"           ],\n" + 
        		"           \"signal\": false\n" + 
        		"       },\n" + 
        		"       {\n" + 
        		"           \"id\": \"105938087\",\n" + 
        		"           \"service_items\": [\n" + 
        		"               \"1302102\"\n" + 
        		"           ],\n" + 
        		"           \"signal\": false\n" + 
        		"       },\n" + 
        		"       {\n" + 
        		"           \"id\": \"105938087\",\n" + 
        		"           \"service_items\": [\n" + 
        		"               \"1302101\"\n" + 
        		"           ],\n" + 
        		"           \"signal\": true\n" + 
        		"       }\n" + 
        		"   ],\n" + 
        		"   \"syncDate\": \"2013-07-19T15:04:06.949Z\",\n" + 
        		"   \"inputContainsSMP\": true,\n" + 
        		"   \"inputContainsYsPro\": false,\n" + 
        		"   \"id\": \"40f0a249-f1ae-4034-bda4-e1ec177b3707\",\n" + 
        		"   \"links\": [\n" + 
        		"       {\n" + 
        		"           \"rel\": \"debug\",\n" + 
        		"           \"type\": \"get\",\n" + 
        		"           \"url\": \"http://ttays.yousee.tv/sync/api/synchronize/debug/40f0a249-f1ae-4034-bda4-e1ec177b3707\",\n" + 
        		"           \"href\": \"http://ttays.yousee.tv/sync/api/synchronize/debug/40f0a249-f1ae-4034-bda4-e1ec177b3707\",\n" + 
        		"           \"mediatype\": \"application/json\"\n" + 
        		"       }\n" + 
        		"   ]," +
        		"	\"complete\": true" +
        		"}";
            SyncResponse response=new SyncResponse(null,input);
            
            Assert.assertTrue(response.isComplete());
    	
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

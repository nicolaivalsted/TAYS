package dk.yousee.randy.kasia;

import com.google.gson.JsonParser;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * User: aka
 * Date: 28/10/12
 * Time: 12.09
 * Testing compare prices
 */
public class ItemPriceTest {

    List<ItemPrice> items;

    @Before
    public void setUp() throws Exception {
        items = new ArrayList<ItemPrice>();
        items.add(create("1703001", "{\"totalpris\": \"9.00\"}"));
        items.add(create("1703002", "{\"totalpris\": \"19.00\"}"));
        items.add(create("1703003", "{\"totalpris\": \"29.00\"}"));
        items.add(create("1703004", "{\"totalpris\": \"39.00\"}"));
        items.add(create("1703005", "{\"totalpris\": \"49.00\"}"));
    }

    private ItemPrice create(String id, String json) {
        return new ItemPrice(id, new JsonParser().parse(json).getAsJsonObject());
    }

    private String id(String input){
        for(ItemPrice one:items){
            if(one.matchPrice(input))return one.getId();
        }
        return null;
    }

    @Test
    public void centiKroner() {
        Assert.assertEquals("000",ItemPrice.centiKroner(null));
        Assert.assertEquals("000",ItemPrice.centiKroner(""));
        Assert.assertEquals("000",ItemPrice.centiKroner(" "));
        Assert.assertEquals("000",ItemPrice.centiKroner("."));
        Assert.assertEquals("000",ItemPrice.centiKroner(" . "));
        Assert.assertEquals("100",ItemPrice.centiKroner("1"));
        Assert.assertEquals("100",ItemPrice.centiKroner("1."));
        Assert.assertEquals("100",ItemPrice.centiKroner("1.0"));
        Assert.assertEquals("100",ItemPrice.centiKroner("1.00"));
        Assert.assertEquals("100",ItemPrice.centiKroner("1,00"));
        Assert.assertEquals("000",ItemPrice.centiKroner("0,00"));
        Assert.assertEquals("001",ItemPrice.centiKroner("0,01"));
        Assert.assertEquals("010",ItemPrice.centiKroner("0,1"));
        Assert.assertEquals("1000",ItemPrice.centiKroner("10"));
    }

    @Test
    public void testMatchPrice() {
        Assert.assertEquals("identical 9.00","1703001",id("9.00"));
    }
}

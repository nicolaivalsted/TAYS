package dk.yousee.randy.kasia;

import junit.framework.Assert;
import org.junit.Test;

public class InvoiceRequestTest {

    @Test
    public void createYouBioOrder() throws Exception {
        String s = new InvoiceRequest("612345678", "salesItem", "title", "user", "system", "vodid").printJson().toString();
        Assert.assertEquals("{\"kundeid\":\"612345678\",\"handlinger\":[{\"handling\":\"OPRET\",\"varenr\":\"salesItem\",\"title\":\"title\",\"vodk-id\":\"vodid\"}],\"info\":{\"salgskanal\":\"K\",\"klient-funktion\":\"rent-movie\",\"klient-bruger\":\"user\",\"klient-system\":\"system\"}}", s);
    }

    @Test
    public void createRentMovieWithPrice() throws Exception {
        String s = InvoiceRequest.createMovieWithPrice("612345678", InvoiceRequest.SALES_ITEM_RENT_MOVIE, "title", "user", "system", "vodid", "42.00", "rent-movie").printJson().toString();
        Assert.assertEquals("{\"kundeid\":\"612345678\",\"handlinger\":[{\"handling\":\"OPRET\",\"varenr\":\"2800001\",\"title\":\"title\",\"vodk-id\":\"vodid\",\"pris\":\"42.00\"}],\"info\":{\"salgskanal\":\"W\",\"klient-funktion\":\"rent-movie\",\"klient-bruger\":\"user\",\"klient-system\":\"system\"}}", s);
    }

    @Test
    public void createBuyMovieWithPrice() throws Exception {
        String s = InvoiceRequest.createMovieWithPrice("612345678", InvoiceRequest.SALES_ITEM_BUY_MOVIE, "title", "user", "system", "vodid", "42.00", "buy-movie").printJson().toString();
        Assert.assertEquals("{\"kundeid\":\"612345678\",\"handlinger\":[{\"handling\":\"OPRET\",\"varenr\":\"2800003\",\"title\":\"title\",\"vodk-id\":\"vodid\",\"pris\":\"42.00\"}],\"info\":{\"salgskanal\":\"W\",\"klient-funktion\":\"buy-movie\",\"klient-bruger\":\"user\",\"klient-system\":\"system\"}}", s);
    }

    @Test
    public void createBuyClipcardWithPrice() throws Exception {
        String s = InvoiceRequest.createBuyClipcardWithPrice("612345678", InvoiceRequest.SALES_ITEM_BUY_CLIPCARD, "user", "system", "42.00", "buy-clipcard").printJson().toString();
        Assert.assertEquals("{\"kundeid\":\"612345678\",\"handlinger\":[{\"handling\":\"OPRET\",\"varenr\":\"2800002\",\"pris\":\"42.00\"}],\"info\":{\"salgskanal\":\"W\",\"klient-funktion\":\"buy-clipcard\",\"klient-bruger\":\"user\",\"klient-system\":\"system\"}}", s);
    }

}

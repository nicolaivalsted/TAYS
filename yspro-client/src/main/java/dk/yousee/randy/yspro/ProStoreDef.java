package dk.yousee.randy.yspro;

/**
 * User: aka
 * Date: 28/09/12
 * Time: 11.27
 * Definitions in pro store
 */
public class ProStoreDef {

    /**
     * Name of field that contains business position in Ys Pro
     */
    public static final String BUSINESS_POSITION_KEY_YSPRO = "BusinessPosition";
    /**
     * Name of field that contains description in Ys Pro
     */
    public static final String DESCRIPTION_KEY_YSPRO = "Description";
    /**
     * Name of field that contains service item in Ys Pro.<br/>
     * Service Item is also called "Stalone" in older terms.
     */
    public static final String SERVICE_ITEM_KEY_YSPRO = "ServiceItem";

    /**
     * Value for YsPro product that defines YOU-BIO product
     */
    public static final YsProProduct YOU_BIO_PRODUCT = YsProProduct.create("7000");

    /**
     * Value for YsPro product that defines DMS product
     * Here is stored the net-gem box information for a customer
     */
    public static final String OTT_PRODUCT_KEY_YSPRO = "OttProduct";

    public static final YsProProduct NET_GEM_STB_PRODUCT = YsProProduct.create("6900");
    
    public static final String OTT_DEVICE_MAC_YSPRO = "Device_Mac";
    
    public static final String OTT_CMORE_PRODUCT = "xCmore";
    
    public static final String OTT_HW_INFO = "Hw_Info";
    
    public static final String OTT_DMS_SERVICE_ID = "DmsService";
}

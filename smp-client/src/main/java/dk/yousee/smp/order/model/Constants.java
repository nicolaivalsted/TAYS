package dk.yousee.smp.order.model;

/**
 * Created by IntelliJ IDEA.
 * User: m26778
 * Date: 14-01-2010
 * Time: 13:24:57<br/>
 * Constants in the BSS adapter.
 */
public class Constants {

    public static final OrderDataType SERVICE_TYPE_MOBB_SERVICE_DEF = new OrderDataType( ServicePrefix.SubSvcSpec,"mobilebb_service_def");
    public static final OrderDataType SERVICE_TYPE_MOBB_SERVICE_ATTRIBS = new OrderDataType(ServicePrefix.SubSvcSpec,"mobilebb_service_attribs");

    public static final OrderDataType SERVICE_TYPE_SAMP_SUB = new OrderDataType(ServicePrefix.SubSvcSpec,"samp_sub");
    public static final OrderDataType SERVICE_TYPE_CONTACT_SPEC = new OrderDataType(ServicePrefix.SubContactSpec,"-");
    public static final OrderDataType SERVICE_TYPE_ADDRESS_SPEC = new OrderDataType(ServicePrefix.SubAddressSpec,"-");
    public static final OrderDataType SERVICE_TYPE_PARENT_SERVICE_KEY = new OrderDataType(ServicePrefix.SubSvc);


//    public static final String STATUS_CODE_ASYNC_STARTED = "ASYNC_STARTED";


    public static final OrderDataType FEATURE_LIST_ADDRESS = new OrderDataType( "FEATURE_ADDRESS");
//    public static final OrderDataType ORDER_STATE = new OrderDataType( ServicePrefix.smp,"SubOrderStateType");

//    /* Magiske vaerdier til at genkende parameter vaerdier og navne, saa de kan blive oversat til sigma-sprog */
//    public static final OrderDataType PARAM_NAME = new OrderDataType( "PARAM_NAME");
//    public static final OrderDataType PARAM_VALUE = new OrderDataType( "PARAM_VALUE");

// param name for subscriber name and sur name
    public static final String FIRST_NAME ="first_name";
    public static final String LAST_NAME ="last_name";
    

}

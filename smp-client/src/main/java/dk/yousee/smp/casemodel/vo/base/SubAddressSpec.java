package dk.yousee.smp.casemodel.vo.base;

import dk.yousee.smp.casemodel.SubscriberModel;
import dk.yousee.smp.casemodel.vo.helpers.BasicUnit;
import dk.yousee.smp.casemodel.vo.helpers.PropHolder;
import dk.yousee.smp.order.model.NickName;
import dk.yousee.smp.order.model.OrderDataLevel;
import dk.yousee.smp.order.model.OrderDataType;
import dk.yousee.smp.order.model.ServicePrefix;

/**
 * Created by IntelliJ IDEA.
 * User: aka
 * Date: Oct 15, 2010
 * Time: 10:59:08 AM<br/>
 * Address sub specification, a special Service Plan :-)
 * 
 */
public final class SubAddressSpec extends BasicUnit {
    public static OrderDataLevel LEVEL = OrderDataLevel.ADDRESS;
    public static OrderDataType TYPE = new OrderDataType(ServicePrefix.SubAddressSpec,"-");
    public static NickName NAME = new NickName("address");
    public static final String EXTERNAL_KEY = "primary";

    public SubAddressSpec(SubscriberModel model) {
        super(model, EXTERNAL_KEY,  TYPE, LEVEL,NAME,null);
        model.getServiceLevelUnit().add(this);
    }

    public static final String AMS_ID ="ams_id";
    public PropHolder ams_id = new PropHolder(this, AMS_ID);

    public static final String ADDRESS1 = "address1";
    public PropHolder address1 = new PropHolder(this, ADDRESS1);

    public static final String ADDRESS2 = "address2";
    public PropHolder address2 = new PropHolder(this, ADDRESS2);

    public static final String DISTRICT = "district";
    public PropHolder district = new PropHolder(this, DISTRICT);

    public static final String ZIP_CODE = "zipcode";
    public PropHolder zipcode = new PropHolder(this, ZIP_CODE);

    public static final String CITY = "city";
    public PropHolder city = new PropHolder(this, CITY);

    public static final String COUNTRY = "country";
    public PropHolder country = new PropHolder(this, COUNTRY);

    public static final String MUNICIPALITY = "ntd_return_segment_nm";
    public PropHolder ntd_return_segment_nm = new PropHolder(this, MUNICIPALITY);

}

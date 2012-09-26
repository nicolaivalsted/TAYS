package dk.yousee.smp.casemodel.vo.base;

import dk.yousee.smp.casemodel.SubscriberModel;
import dk.yousee.smp.casemodel.vo.helpers.BasicUnit;
import dk.yousee.smp.casemodel.vo.helpers.PropHolder;
import dk.yousee.smp.order.model.Constants;
import dk.yousee.smp.order.model.OrderDataLevel;
import dk.yousee.smp.order.model.OrderDataType;

/**
 * Created by IntelliJ IDEA.
 * User: aka
 * Date: Oct 15, 2010
 * Time: 10:58:51 AM
 * Contact sub specification, a special Service Plan :-)<br/>
 * Kontakt parametre til at komme i kontakt med kunden / brugeren
 */
public final class SubContactSpec extends BasicUnit {

    public static OrderDataLevel LEVEL = OrderDataLevel.CONTACT;
    public static OrderDataType TYPE = Constants.SERVICE_TYPE_CONTACT_SPEC;
    public static final String EXTERNAL_KEY = "primary";

    public SubContactSpec(SubscriberModel model) {
        super(model, EXTERNAL_KEY,  TYPE, LEVEL,null);
        model.getServiceLevelUnit().add(this);
    }

    public PropHolder first_name = new PropHolder(this, Constants.FIRST_NAME);
    public PropHolder mittle_name = new PropHolder(this, "mittle_name");

    public PropHolder last_name = new PropHolder(this, Constants.LAST_NAME);

    public static final String ISP ="isp";
    public PropHolder isp = new PropHolder(this, ISP);

    public static final String PHONES_CELL_NUMBER = "phones.cell.number";
    public PropHolder phone_cell_number = new PropHolder(this, PHONES_CELL_NUMBER);

    public static final String EMAILS_HOME_ADDRESS = "emails.home.address";
    public PropHolder emails_home_address = new PropHolder(this, EMAILS_HOME_ADDRESS);

    public static final String PHONES_BUSINESS_NUMBER = "phones.business.number";
    public PropHolder phones_business_number = new PropHolder(this, PHONES_BUSINESS_NUMBER);

    public static final String PHONES_HOME_NUMBER = "phones.home.number";
    public PropHolder phones_home_number = new PhoneNumberPropHolder(this, PHONES_HOME_NUMBER);

}

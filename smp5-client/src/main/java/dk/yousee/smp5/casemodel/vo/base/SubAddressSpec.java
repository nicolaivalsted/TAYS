/**
 * 
 */
package dk.yousee.smp5.casemodel.vo.base;

import dk.yousee.smp5.casemodel.SubscriberModel;
import dk.yousee.smp5.casemodel.vo.helpers.BasicUnit;
import dk.yousee.smp5.casemodel.vo.helpers.PropHolder;
import dk.yousee.smp5.order.model.Constants;
import dk.yousee.smp5.order.model.NickName;
import dk.yousee.smp5.order.model.OrderDataLevel;
import dk.yousee.smp5.order.model.OrderDataType;
import dk.yousee.smp5.order.model.ServicePrefix;

/**
 * @author m64746
 *
 *         Date: 14/10/2015 Time: 13:43:10 Address sub specification, a special
 *         Service Plan
 */
public class SubAddressSpec extends BasicUnit {
	public static OrderDataLevel LEVEL = OrderDataLevel.ADDRESS;
	public static OrderDataType TYPE = new OrderDataType(ServicePrefix.SubAddressSpec, "-");
	public static NickName NAME = new NickName("address");
	public static final String EXTERNAL_KEY = "primary";

	public SubAddressSpec(SubscriberModel model) {
		super(model, EXTERNAL_KEY, TYPE, LEVEL, NAME, null);
		model.getServiceLevelUnit().add(this);
	}

	public PropHolder ams_id = new PropHolder(this, Constants.AMS_ID);

	public PropHolder district = new PropHolder(this, Constants.DISTRICT);

	public PropHolder zipcode = new PropHolder(this, Constants.ZIP_CODE);

	public PropHolder city = new PropHolder(this, Constants.CITY);

	public PropHolder country = new PropHolder(this, Constants.COUNTRY);

	public PropHolder ntd_return_segment_nm = new PropHolder(this, Constants.MUNICIPALITY);

	public PropHolder floor = new PropHolder(this, "floor");

	public PropHolder street_name = new PropHolder(this, "street_nm");

	public PropHolder cableUnit = new PropHolder(this, "region");

}

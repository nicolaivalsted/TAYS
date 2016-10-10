package dk.yousee.smp5.cases;

import org.apache.commons.lang3.StringUtils;

import dk.yousee.smp5.casemodel.SubscriberModel;
import dk.yousee.smp5.casemodel.vo.base.SampSub;
import dk.yousee.smp5.casemodel.vo.base.SubAddressSpec;
import dk.yousee.smp5.casemodel.vo.base.SubContactSpec;
import dk.yousee.smp5.casemodel.vo.base.SubSpec;
import dk.yousee.smp5.cases.subscriber.AddressInfo;
import dk.yousee.smp5.cases.subscriber.ContactInfo;
import dk.yousee.smp5.cases.subscriber.SubscriberInfo;
import dk.yousee.smp5.order.model.Acct;
import dk.yousee.smp5.order.model.Action;
import dk.yousee.smp5.order.model.BusinessException;
import dk.yousee.smp5.order.model.Order;
import dk.yousee.smp5.order.model.OrderService;
import dk.yousee.smp5.order.model.Subscriber;

public class SubscriberCase extends AbstractCase {
	public boolean justCreated = false;

	/**
	 * @param service
	 *            the service where data should be read / written
	 * @param acct
	 *            the customers 9 digit number
	 */
	public SubscriberCase(OrderService service, Acct acct) {
		super(acct, service);
	}

	public SubscriberCase(OrderService service, SubscriberModel model) {
		super(model, service);
	}

	/**
	 * Does the customer exist in Sigma yet
	 *
	 * @return true means the customer is registered
	 */
	public boolean customerExists() {
		return getModel().customerExists();
	}

	/**
	 * @param customer
	 */
	public boolean updateContact(ContactInfo customer) {
		SubContactSpec subContactSpec = getModel().find().SubContactSpec();
		boolean changes = false;

		if (StringUtils.isNotBlank(customer.getFirstName()) && !customer.getFirstName().equalsIgnoreCase(getValue(subContactSpec.first_name.getValue()))) {
			subContactSpec.first_name.setValue(customer.getFirstName());
			changes = true;
		}

		if (StringUtils.isNotBlank(customer.getLastName()) && !customer.getLastName().equalsIgnoreCase(getValue(subContactSpec.last_name.getValue()))) {
			subContactSpec.last_name.setValue(customer.getLastName());
			changes = true;
		}

		if (StringUtils.isNotBlank(customer.getEmail()) && !customer.getEmail().equalsIgnoreCase(getValue(subContactSpec.emails_home_address.getValue()))) {
			subContactSpec.emails_home_address.setValue(customer.getEmail());
			changes = true;
		}

		if (StringUtils.isNotBlank(customer.getPrivattlf())
				&& !customer.getPrivattlf().equalsIgnoreCase(getValue(subContactSpec.phones_home_number.getValue()))) {
			subContactSpec.phones_home_number.setValue(customer.getPrivattlf());
			changes = true;
		}

		if (StringUtils.isNotBlank(customer.getIsp()) && !customer.getIsp().equalsIgnoreCase(getValue(subContactSpec.isp.getValue()))) {
			subContactSpec.isp.setValue(customer.getIsp());
			changes = true;
		}
		return changes;
	}

	/**
	 * @param Subscriber
	 */
	public Subscriber updateSusbcriber(SubscriberInfo requestSubscriber) {
		Subscriber sub = getModel().getSubscriber();

		if (requestSubscriber.getLid() != null && !getValue(requestSubscriber.getLid()).equalsIgnoreCase(getValue(sub.getLid()))) {
			sub.setLid(requestSubscriber.getLid());
		}

		if (requestSubscriber.getLinkid() != null && !getValue(requestSubscriber.getLinkid()).equalsIgnoreCase(getValue(sub.getLinkid()))) {
			sub.setLinkid(requestSubscriber.getLinkid());
		}

		return sub;
	}

	private String getValue(String value) {
		return getValue(value, "");
	}

	private String getValue(String value, String defaultValue) {
		return StringUtils.isNotBlank(value) ? value : defaultValue;
	}

	/**
	 * @param address
	 */
	public SubAddressSpec updateAddress(AddressInfo address) {
		SubAddressSpec subAddressSpec = getModel().find().SubAddressSpec();
		if (StringUtils.isNotBlank(address.getFloor()) && address.getFloor().equalsIgnoreCase(getValue(subAddressSpec.floor.getValue()))) {
			subAddressSpec.floor.setValue(address.getFloor());
		}

		if (StringUtils.isNotBlank(address.getStreetName()) && address.getStreetName().equalsIgnoreCase(getValue(subAddressSpec.street_name.getValue()))) {
			subAddressSpec.street_name.setValue(address.getStreetName());
		}

		if (StringUtils.isNotBlank(address.getZipcode()) && address.getZipcode().equalsIgnoreCase(getValue(subAddressSpec.zipcode.getValue()))) {
			subAddressSpec.zipcode.setValue(address.getZipcode());
		}

		if (StringUtils.isNotBlank(address.getDistrict()) && address.getDistrict().equalsIgnoreCase(getValue(subAddressSpec.district.getValue()))) {
			subAddressSpec.district.setValue(address.getDistrict());
		}

		if (StringUtils.isNotBlank(address.getCity()) && address.getCity().equalsIgnoreCase(getValue(subAddressSpec.city.getValue()))) {
			subAddressSpec.city.setValue(address.getCity());
		}

		if (StringUtils.isNotBlank(address.getGeographicName()) && address.getGeographicName().equalsIgnoreCase(getValue(subAddressSpec.geo_name.getValue()))) {
			subAddressSpec.geo_name.setValue(address.getGeographicName());
		}

		if (StringUtils.isNotBlank(address.getDoorCode()) && address.getDoorCode().equalsIgnoreCase(getValue(subAddressSpec.door_code.getValue()))) {
			subAddressSpec.door_code.setValue(address.getDoorCode());
		}

		if (StringUtils.isNotBlank(address.getStreetNumber()) && address.getStreetNumber().equalsIgnoreCase(getValue(subAddressSpec.street_num.getValue()))) {
			subAddressSpec.street_num.setValue(address.getStreetNumber());
		}

		if (StringUtils.isNotBlank(address.getSide()) && address.getSide().equalsIgnoreCase(getValue(subAddressSpec.street_number_suffix.getValue()))) {
			subAddressSpec.street_number_suffix.setValue(address.getSide());
		}

		if (StringUtils.isNotBlank(address.getNtd_return_segment_nm())
				&& address.getNtd_return_segment_nm().equalsIgnoreCase(getValue(subAddressSpec.ntd_return_segment_nm.getValue()))) {
			subAddressSpec.ntd_return_segment_nm.setValue(address.getNtd_return_segment_nm());
		}

		String ams_id = address.getAms();
		if (address.getAms().equals("")) {
			if (subAddressSpec.ams_id.getValue().equals("")) {
				ams_id = "0000";
			} else {
				ams_id = subAddressSpec.ams_id.getValue();
			}
		}
		subAddressSpec.ams_id.setValue(ams_id);

		return subAddressSpec;
	}

	/**
	 * Will pad a string "0" in front of zip codes less than 4 digits
	 * 
	 * @param zip
	 *            raw value with potentical only 3 digits
	 * @return a four digit zip code
	 */
	public String zip4ch(String zip) {
		if (zip == null || zip.trim().length() == 0) {
			return null;
		}
		if (zip.length() < 4) {
			return zip4ch("0" + zip);
		} else {
			return zip;
		}
	}

	/**
	 * Adds subscription to sigma. It means that a new customer is created
	 *
	 * @return the new model starting the customer's content
	 * @throws dk.yousee.smp5.order.model.BusinessException
	 *             when add cannot be performed
	 */
	public SubscriberModel addSubscription() throws BusinessException {
		return this.addSubscription(getModel().getOrder());
	}

	/**
	 * Adds subscription to sigma. It means that a new customer is created
	 *
	 * @param order
	 *            the order to process
	 * @return the new model starting the customer's content
	 * @throws dk.yousee.smp.order.model.BusinessException
	 *             when add cannot be performed
	 */
	public SubscriberModel addSubscription(Order order) throws BusinessException {
		setErrorMessage(null);
		try {
			setResponse(getService().addSubscription(order));
		} catch (Exception e) {
			e.printStackTrace();
			setErrorMessage(e.getMessage());
			return null;
		}
		if (getResponse().getSmp() == null) {
			setErrorMessage(getResponse().getErrorMessage());
		}
		setModel(new SubscriberModel(getResponse()));
		if (getErrorMessage() != null) {
			throw new BusinessException("could not create subscription, for customer %s, got message: %s", order.getSubscriber().getKundeId(),
					getErrorMessage());
		}
		justCreated = true;
		return getModel();
	}

	/**
	 * @param customer
	 * @param address
	 * @return
	 */
	public Order buildAddSubscriptionOrder(ContactInfo customer, AddressInfo address, SubscriberInfo subscriberInfo) {
		setModel(new SubscriberModel(getAcct()));

		Order smpOrder = getModel().getOrder();
		// Populate Subscriber object, contact and address objects
		Subscriber subscriber = smpOrder.getSubscriber();
		subscriber.setKundeId(new Acct(subscriberInfo.getAcct()));
		subscriber.setLid(subscriberInfo.getLid());
		subscriber.setLinkid(subscriberInfo.getLinkid());

		SubContactSpec mc = getModel().add().SubContactSpec();
		mc.emails_home_address.setValue(customer.getEmail());
		mc.phones_home_number.setValue(customer.getPrivattlf());
		mc.isp.setValue(customer.getIsp());
		mc.first_name.setValue(customer.getFirstName());
		mc.last_name.setValue(customer.getLastName());

		SubAddressSpec ma = getModel().add().SubAddressSpec();
		ma.street_name.setValue(address.getStreetName());
		ma.street_number_suffix.setValue(address.getSide());
		ma.floor.setValue(address.getFloor());
		ma.ams_id.setValue(address.getAms());
		ma.zipcode.setValue(zip4ch(address.getZipcode()));
		ma.district.setValue(address.getDistrict());
		ma.city.setValue(address.getCity());
		ma.geo_name.setValue(address.getGeographicName());
		ma.door_code.setValue(address.getDoorCode());
		ma.street_num.setValue(address.getStreetNumber());
		ma.ntd_return_segment_nm.setValue(address.getNtd_return_segment_nm());
		return getModel().getOrder();
	}

	/**
	 * Flow state for the subscriber
	 * <p>
	 * The situation is that WHEN a subscriber has been created (SMP add) then
	 * the subscriber engagement (model) must be read again. The creation was
	 * done as "snapshot" order, this means the creation was performed before
	 * SMP returned the creation order. In other words the processing in SMP has
	 * been performed to the end. Therefore it is possible to ask for the
	 * engagement right away.
	 * </p>
	 * 
	 * @return true when subscriber has been created in this case
	 */
	public boolean isJustCreated() {
		return justCreated;
	}

	/**
	 * @throws BusinessException
	 * 
	 */
	public Order deleteSubscription() throws BusinessException {
		SubscriberModel model = getModel();
		if (getResponse().getSmp() == null)
			setErrorMessage(getResponse().getErrorMessage());
		if (getErrorMessage() != null) {
			throw new BusinessException("could not create subscription, for customer %s, got message: %s", getAcct(), getErrorMessage());
		}

		SubSpec subSpec = model.add().SubSpec(model.getOrder().getExternalKey());
		SubAddressSpec subAddressSpec = model.find().SubAddressSpec();
		SubContactSpec subContactSpec = model.find().SubContactSpec();
		SampSub sampSub = model.find().SampSub();

		subSpec.getDefaultOrderData().addChild(subContactSpec.getDefaultOrderData());
		subSpec.getDefaultOrderData().addChild(subAddressSpec.getDefaultOrderData());
		subSpec.getDefaultOrderData().addChild(sampSub.getDefaultOrderData());

		model.getOrder().getOrderData().remove(1);
		model.getOrder().getOrderData().remove(1);
		model.getOrder().getOrderData().get(1).setAction(Action.DELETE);

		subSpec.delete();
		return model.getOrder();
	}
}

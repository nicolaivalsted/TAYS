package dk.yousee.smp5.cases;

import dk.yousee.smp5.casemodel.SubscriberModel;
import dk.yousee.smp5.casemodel.vo.base.SubAddressSpec;
import dk.yousee.smp5.casemodel.vo.base.SubContactSpec;
import dk.yousee.smp5.cases.subscriber.AddressInfo;
import dk.yousee.smp5.cases.subscriber.ContactInfo;
import dk.yousee.smp5.order.model.Acct;
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
	public SubContactSpec updateContact(ContactInfo customer) {
		SubContactSpec plan = getModel().find().SubContactSpec();
		plan.first_name.setValue(customer.getFirstName());
		plan.last_name.setValue(customer.getLastName());
		plan.isp.setValue(customer.getIsp());
		plan.phone_cell_number.setValue(customer.getMobiltlf());
		plan.emails_home_address.setValue(customer.getEmail());
		plan.phones_business_number.setValue(customer.getArbejdstlf());
		plan.phones_home_number.setValue(customer.getPrivattlf());
		return plan;
	}

	/**
	 * @param address
	 */
	public SubAddressSpec updateAddress(AddressInfo address) {
		SubAddressSpec subAddressSpec = getModel().find().SubAddressSpec();
		subAddressSpec.floor.setValue(address.getFloor());
		subAddressSpec.street_name.setValue(address.getStreetName());
		subAddressSpec.ams_id.setValue(address.getAms());
		subAddressSpec.zipcode.setValue(zip4ch(address.getZipcode()));
		subAddressSpec.district.setValue(address.getDistrict());
		subAddressSpec.city.setValue(address.getCity());
		subAddressSpec.ntd_return_segment_nm.setValue(address.getNtd_return_segment_nm());
		subAddressSpec.cableUnit.setValue(address.getCableUnit());
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
			throw new BusinessException("could not create subscription, for customer %s, got message: %s", order.getSubscriber()
					.getKundeId(), getErrorMessage());
		}
		justCreated = true;
		return getModel();
	}

	/**
	 * @param customer
	 * @param address
	 * @return
	 */
	public Order buildAddSubscriptionOrder(ContactInfo customer, AddressInfo address) {
		setModel(new SubscriberModel(getAcct()));

		Order smpOrder = getModel().getOrder();
		// Populate Subscriber object, contact and address objects
		Subscriber subscriber = smpOrder.getSubscriber();
		subscriber.setFornavn(customer.getFirstName());
		subscriber.setEfternavn(customer.getLastName());
		subscriber.setKundeId(new Acct(customer.getAcct()));

		SubContactSpec mc = getModel().add().SubContactSpec();
		mc.isp.setValue(customer.getIsp());
		mc.phone_cell_number.setValue(customer.getMobiltlf());
		mc.emails_home_address.setValue(customer.getEmail());
		mc.phones_business_number.setValue(customer.getArbejdstlf());
		mc.phones_home_number.setValue(customer.getPrivattlf());

		SubAddressSpec ma = getModel().add().SubAddressSpec();
		ma.street_name.setValue(address.getStreetName());
		ma.floor.setValue(address.getFloor());
		ma.ams_id.setValue(address.getAms());
		ma.zipcode.setValue(zip4ch(address.getZipcode()));
		ma.district.setValue(address.getDistrict());
		ma.city.setValue(address.getCity());
		ma.country.setValue(address.getCountry());
		ma.ntd_return_segment_nm.setValue(address.getNtd_return_segment_nm());
		ma.cableUnit.setValue(address.getCableUnit());
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
}

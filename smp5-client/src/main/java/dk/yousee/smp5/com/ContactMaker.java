package dk.yousee.smp5.com;

import com.sigmaSystems.schemas.x31.smpCBECoreSchema.AssocListType;
import com.sigmaSystems.schemas.x31.smpCBECoreSchema.AssocType;
import com.sigmaSystems.schemas.x31.smpCBECoreSchema.EntityParamListType;
import com.sigmaSystems.schemas.x31.smpCBECoreSchema.SubAddressKeyType;
import com.sigmaSystems.schemas.x31.smpCBECoreSchema.SubContactKeyType;
import com.sigmaSystems.schemas.x31.smpCBECoreSchema.SubContactType;

import dk.yousee.smp5.order.model.Action;
import dk.yousee.smp5.order.model.Constants;
import dk.yousee.smp5.order.model.OrderData;

/**
 * @author m64746
 *
 * Date: 16/10/2015
 * Time: 13:06:18
 * This Class produces smp xml contact object
 * 
 */
public class ContactMaker extends XmlMaker {
	
	private static final ServiceStateTypeConverter sstc = new ServiceStateTypeConverter();

    public SubContactType createContactEntityValue(OrderData contactData) {
        SubContactType contactType = SubContactType.Factory.newInstance();
        SubContactKeyType subContactKey = createContactKey();
        contactType.setKey(subContactKey);
        contactType.setState(sstc.toSimpleState(contactData.getAction()).getStringValue());
        EntityParamListType subscriberParamList = contactType.addNewParamList();
        addParameters(subscriberParamList, contactData.getParams());
        AssocListType associations = contactType.addNewAssociationList();
        AssocType contactAddressAssoc = associations.addNewAssociation();
        contactAddressAssoc.setAssociationType("contact_on_address");
        SubAddressKeyType zEndKey = SubAddressKeyType.Factory.newInstance();
        zEndKey.setType("SubAddress");
        zEndKey.setExternalKey("primary");
        contactAddressAssoc.setZEndKey(zEndKey);
        if (contactData.getAction().equals(Action.ACTIVATE)) {
            contactAddressAssoc.setChangeAction(AssocType.ChangeAction.ADD);
        }
        return contactType;
    }

    public SubContactKeyType createContactKey() {
        SubContactKeyType subContactKey = SubContactKeyType.Factory.newInstance();
        subContactKey.setType(Constants.SUB_CONTACTKEY_TYPE);
        subContactKey.setExternalKey("primary");
        return subContactKey;
    }

}

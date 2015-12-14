package dk.yousee.smp5.com;

import com.sigmaSystems.schemas.x31.smpCBECoreSchema.AssocListType;
import com.sigmaSystems.schemas.x31.smpCBECoreSchema.AssocType;
import com.sigmaSystems.schemas.x31.smpCBECoreSchema.EntityParamListType;
import com.sigmaSystems.schemas.x31.smpCBECoreSchema.SubAddressKeyType;
import com.sigmaSystems.schemas.x31.smpCBECoreSchema.SubAddressType;
import com.sigmaSystems.schemas.x31.smpCBEServiceSchema.SubSvcKeyType;
import com.sigmaSystems.schemas.x31.smpCBEServiceSchema.SubSvcType;
import com.sigmaSystems.schemas.x31.smpCommonValues.ParamType;
import com.sigmaSystems.schemas.x31.smpServiceActivationSchema.OrderItemType;

import dk.yousee.smp5.order.model.Action;
import dk.yousee.smp5.order.model.Subscriber;
import dk.yousee.smp5.com.ServiceStateTypeConverter;
import dk.yousee.smp5.order.model.Constants;
import dk.yousee.smp5.order.model.OrderData;

/**
 * @author m64746
 *
 * Date: 16/10/2015
 * Time: 12:52:08
 */
public class AddressMaker extends XmlMaker{
	
	private static final ServiceStateTypeConverter sstc = new ServiceStateTypeConverter();

    public SubAddressType createAddressEntityValue(OrderData addressData) {
        SubAddressKeyType subAddressKeyType = createAddressKey();
        SubAddressType addressEntity = SubAddressType.Factory.newInstance();
        addressEntity.setKey(subAddressKeyType);
        addressEntity.setState(sstc.toSimpleState(addressData.getAction()).getStringValue());
        addressEntity.setAddressType("service");
        addressEntity.setIsDefault(true);
        EntityParamListType addressParamList = addressEntity.addNewParamList();
        addParameters(addressParamList, addressData.getParams());
        return addressEntity;
    }
    
    public SubAddressKeyType createAddressKey() {
        SubAddressKeyType subAddressKeyType = SubAddressKeyType.Factory.newInstance();
        subAddressKeyType.setType(Constants.SUB_ADDRESSKEY_TYPE);
        subAddressKeyType.setExternalKey("primary");
        return subAddressKeyType;
    }
    
    public SubSvcType createSampSubEntityValue(OrderData addressData, Subscriber subscriber) {
        SubSvcType entity = SubSvcType.Factory.newInstance();

        entity.xsetServiceState(sstc.toState(addressData.getAction()));

        SubSvcKeyType subKeyType = createSampSubKey(subscriber.getKundeId().toString());
        entity.setServiceKey(subKeyType);
        ParamType paramType = entity.addNewParamList().addNewParam();
        paramType.setName("acct");
        paramType.setNil();

        AssocListType associations = entity.addNewAssociationList();
        AssocType addressAddressAssoc = associations.addNewAssociation();
        addressAddressAssoc.setAssociationType("service_on_address");
        SubAddressKeyType zEndKey = SubAddressKeyType.Factory.newInstance();
        zEndKey.setType(Constants.SUB_ADDRESSKEY_TYPE);
        zEndKey.setExternalKey("primary");
        addressAddressAssoc.setZEndKey(zEndKey);
        if (addressData.getAction() == Action.ACTIVATE) {
            addressAddressAssoc.setChangeAction(AssocType.ChangeAction.ADD);
        }
        return entity;
    }
    
    public static SubSvcKeyType createSampSubKey(String kundeId) {
        SubSvcKeyType subKeyType = SubSvcKeyType.Factory.newInstance();
        subKeyType.setType(Constants.SUB_SVCKEY_TYPE_SAMP);
        subKeyType.setExternalKey("sigma_samp_sub_" + kundeId);
        return subKeyType;
    }
    
    public void addSampSub(OrderItemType orderItem, Subscriber subscriber, OrderData addressData) {
        SubSvcType entity = createSampSubEntityValue(addressData, subscriber);
        SubSvcKeyType subKeyType = createSampSubKey(subscriber.getKundeId().toString());

        orderItem.setEntityKey(subKeyType);
        orderItem.setEntityValue(entity);
        orderItem.setItemState("open.not_running.not_started");
    }

}

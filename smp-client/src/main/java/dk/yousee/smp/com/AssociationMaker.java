package dk.yousee.smp.com;

import com.sigmaSystems.schemas.x31.smpCBECoreSchema.AssocListType;
import com.sigmaSystems.schemas.x31.smpCBECoreSchema.AssocType;
import com.sigmaSystems.schemas.x31.smpCBECoreSchema.SubAddressKeyType;
import com.sigmaSystems.schemas.x31.smpCBEServiceSchema.SubSvcKeyType;
import dk.yousee.smp.casemodel.vo.base.SubAddressSpec;
import dk.yousee.smp.order.model.OrderDataAssociation;
import dk.yousee.smp.order.model.OrderDataType;
import dk.yousee.smp.order.model.SmpManipulationAction;
import org.apache.log4j.Logger;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: aka
 * Date: 03/06/12
 * Time: 11.29
 * Makes things relevant for associations
 */
public class AssociationMaker extends XmlMaker {

    private static final Logger logger = Logger.getLogger(AssociationMaker.class);

    public AssocListType createAssociationsFromOrderDataAssociations(List<OrderDataAssociation> assocData) {
        AssocListType assocList = AssocListType.Factory.newInstance();
        for (OrderDataAssociation one : assocData) {

            logger.debug("Creating association. OrderData has type: " + one.getType());
            logger.debug("orderData: " + one.getExternalKey() + " value: " + one.getType());
            AssocType assocType = assocList.addNewAssociation();
            assocType.setAssociationType(one.getAssociationType());
            if (SubAddressSpec.TYPE.equals(one.getType())) {
                SubAddressKeyType zEndKey = initiateSubAddressKeyType(one.getType(), one.getExternalKey());
                assocType.setZEndKey(zEndKey);
            } else {
                SubSvcKeyType zEndKey = initiateSubSvcKeyType(one.getType(), one.getExternalKey());
                assocType.setZEndKey(zEndKey);
            }

            logger.debug("Assoc value is: " + one.getType());

            if (one.getAction() != null) {
                logger.debug("Adding change action tag with action: " + one.getAction());
                assocType.setChangeAction(toChangeAction(one.getAction()));
            } else {
                logger.warn("association had no action ..."+one);
            }
        }
        return assocList;
    }
    private SubAddressKeyType initiateSubAddressKeyType(OrderDataType type, String externalKey) {
        SubAddressKeyType keyType = SubAddressKeyType.Factory.newInstance();
        keyType.setType(type.toString());
        keyType.setExternalKey(externalKey);
        return keyType;
    }

    private AssocType.ChangeAction.Enum toChangeAction(SmpManipulationAction action) {
        if (SmpManipulationAction.ADD == action) return AssocType.ChangeAction.ADD;
        if (SmpManipulationAction.DELETE == action) return AssocType.ChangeAction.DELETE;
        throw new IllegalArgumentException("Unknown action: " + action);
    }
}

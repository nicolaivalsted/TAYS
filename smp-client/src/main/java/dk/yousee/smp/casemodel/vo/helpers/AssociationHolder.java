package dk.yousee.smp.casemodel.vo.helpers;

import dk.yousee.smp.casemodel.vo.base.SubAddressSpec;
import dk.yousee.smp.order.model.BusinessException;
import dk.yousee.smp.order.model.OrderDataAssociation;
import dk.yousee.smp.order.model.OrderDataType;
import dk.yousee.smp.order.model.ResponseAssociation;
import dk.yousee.smp.order.model.SmpManipulationAction;

/**
 * Created by IntelliJ IDEA.
 * User: m14857
 * Date: Oct 22, 2010
 * Time: 3:13:54 PM
 * Class that hold OrderDataAssociations
 */
public class AssociationHolder {

    private BasicUnit unit;
//    private boolean mandatory = false;
    private String associationType;
    private OrderDataType type;

    public AssociationHolder(BasicUnit unit, String associationType, OrderDataType type) {
        this.unit = unit;
        this.associationType = associationType;
        this.type = type;
    }

//    public AssociationHolder(BasicUnit unit, String associationType, OrderDataType type, boolean mandatory) {
//        this.unit = unit;
//        this.associationType = associationType;
//        this.type = type;
//        this.mandatory = mandatory;
//    }


    public boolean isEmpty() {
        return get() == null;
    }

    public ResponseAssociation get() {
//       if (unit.isDefaultOrderDataExist()) {
//           return unit.getAssociationByTypeInOrderData(associationType);
//       } else {
//           return unit.getAssociationByTypeInResponse(associationType);
//       }
//        return null;
        return unit.getAssociationByTypeInResponse(associationType);
    }

    /**
     * if associationType is service_on_address, we don't need externalKey, it is always 'primary'
     *
     * @param basicUnit == the service we want to link to.
     * @throws dk.yousee.smp.order.model.BusinessException
     *          when the basic unit's type is not the correct one.
     */
    public void add(BasicUnit basicUnit) throws BusinessException {
        if (basicUnit.getType() != type) {
            throw new BusinessException("The service 's type doesn't match with the suppoesd association service 's type.");
        }
        if (get() == null) {
            OrderDataAssociation orderDataAssociation = new OrderDataAssociation();
            orderDataAssociation.setAction(SmpManipulationAction.ADD);
            orderDataAssociation.setAssociationType(associationType);
            orderDataAssociation.setType(type);
            if (type == SubAddressSpec.TYPE) {
                orderDataAssociation.setExternalKey("primary");
            } else {
                orderDataAssociation.setExternalKey(basicUnit.getExternalKey());
            }
            unit.assignAssociation(orderDataAssociation);
        } else {
            throw new IllegalStateException("The service has Association already.We can't add more. Type: " + associationType);
        }
    }


    /**
     * if associationType is service_on_address, we don't need externalKey, it is always 'primary'
     *
     * @param externalKey key
     * @throws BusinessException when it does not exist
     */
    public void delete(String externalKey) throws BusinessException {
        if (get() == null) {
            throw new BusinessException("The service has not Association. We can't delete. Type: %s", associationType);
        } else {
            OrderDataAssociation orderDataAssociation = new OrderDataAssociation();
            orderDataAssociation.setAction(SmpManipulationAction.DELETE);
            orderDataAssociation.setAssociationType(associationType);
            orderDataAssociation.setType(type);
            orderDataAssociation.setExternalKey(externalKey);
            unit.assignAssociation(orderDataAssociation);
        }
    }

//    /**
//     * if associationType is service_on_address, we don't need externalKey, it is always 'primary'
//     */
//    public void update(String externalKey) throws Exception {
//        if (get() == null) {
//            throw new Exception("The service has not Association. We can't update. Type: " + associationType);
//        } else if (associationType.equalsIgnoreCase("service_on_address")) {
//            throw new Exception("we can't update service_on_address Association. Type: " + associationType);
//        } else {
//            OrderDataAssociation orderDataAssociation = new OrderDataAssociation();
//            orderDataAssociation.setAction(SmpManipulationAction.UPDATE);
//            orderDataAssociation.setAssociationType(associationType);
//            orderDataAssociation.setType(type);
//            orderDataAssociation.setExternalKey(externalKey);
//            unit.assignAssociation(orderDataAssociation);
//        }
//    }

//    /**
//     * if associationType is service_on_address, we don't need externalKey, it is always 'primary'
//     *
//     * @param externalKey == external key matching combined with {@link #type}
//     */
//    /**
//     * @deprecated
//     */
//    public void add(String externalKey) {
//        if (get() == null) {
//            OrderDataAssociation orderDataAssociation = new OrderDataAssociation();
//            orderDataAssociation.setAction(SmpManipulationAction.ADD);
//            orderDataAssociation.setAssociationType(associationType);
//            orderDataAssociation.setType(type);
//            if (type == SubAddressSpec.TYPE) {
//                orderDataAssociation.setExternalKey("primary");
//            } else {
//                orderDataAssociation.setExternalKey(externalKey);
//            }
//            unit.assignAssociation(orderDataAssociation);
//        } else {
//            throw new IllegalStateException("The service has Association already.We can't add more. Type: " + associationType);
//        }
//    }
}
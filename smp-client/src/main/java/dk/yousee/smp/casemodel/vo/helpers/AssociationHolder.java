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

    public boolean isEmpty() {
        return get() == null;
    }

    public ResponseAssociation get() {
        return unit.getAssociationByTypeInResponse(associationType);
    }

    /**
     * if associationType is service_on_address, we don't need externalKey, it is always 'primary'
     *
     * @param basicUnit == the service we want to link to.
     */
    public void add(BasicUnit basicUnit)  {
        if (basicUnit.getType() != type) {
            throw new IllegalArgumentException("The service 's type doesn't match with the suppoesd association service 's type.");
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

}

package dk.yousee.smp.com;

import com.sigmaSystems.schemas.x31.smpServiceActivationSchema.SubSvcStateType;
import com.sun.java.products.oss.xml.serviceActivation.ServiceStateType;
import dk.yousee.smp.order.model.Action;
import dk.yousee.smp.order.model.ProvisionStateEnum;
import org.apache.log4j.Logger;

/**
 * Created by IntelliJ IDEA.
 * User: aka
 * Date: Oct 12, 2010
 * Time: 12:33:03 PM
 * Producer of serviceSateTypes.
 */
public class ServiceStateTypeConverter {

    private static final Logger logger = Logger.getLogger(ServiceStateTypeConverter.class);

    public ServiceStateType toState(Action action) {
        if (Action.ACTIVATE == action || Action.UPDATE == action) {
            SubSvcStateType state = SubSvcStateType.Factory.newInstance();
            state.setProvisionState(ProvisionStateEnum.ACTIVE.getState());
            state.setStringValue(SubSvcStateType.ACTIVE.toString());
            return state;
        }
        if (Action.DEACTIVATE == action) {
            SubSvcStateType state = SubSvcStateType.Factory.newInstance();
            state.setProvisionState(ProvisionStateEnum.DELETED.getState());
            state.setStringValue(SubSvcStateType.INACTIVE.toString());
            return state;
        }
        if (Action.INACTIVE == action) {
            SubSvcStateType state = SubSvcStateType.Factory.newInstance();
            state.setProvisionState(ProvisionStateEnum.INACTIVE.getState());
            state.setStringValue(SubSvcStateType.INACTIVE.toString());
            return state;
        }

        if (Action.RESUME == action || Action.SWAP == action) {
            SubSvcStateType state = SubSvcStateType.Factory.newInstance();
            state.setProvisionState(ProvisionStateEnum.ACTIVE.getState());
            state.setStringValue(SubSvcStateType.ACTIVE.toString());
            return state;
        }
        if (Action.BLOCK == action) {
            SubSvcStateType state = SubSvcStateType.Factory.newInstance();
            state.setProvisionState(ProvisionStateEnum.MSO_BLOCK.getState());
            state.setStringValue(SubSvcStateType.ACTIVE.toString());
            return state;
        }

        if (Action.SUSPEND == action) {
            SubSvcStateType state = SubSvcStateType.Factory.newInstance();
            state.setProvisionState(ProvisionStateEnum.COURTESY_BLOCK.getState());
            state.setStringValue(SubSvcStateType.ACTIVE.toString());
            return state;
        }

        if (Action.DELETE == action) {
            SubSvcStateType state = SubSvcStateType.Factory.newInstance();
            state.setProvisionState(ProvisionStateEnum.DELETED.getState());
            state.setStringValue(SubSvcStateType.INACTIVE.toString());
            return state;
        }
        throw new IllegalArgumentException("Unknown action: " + action);
    }

    public ServiceStateType toSimpleState(Action action) {
        if (Action.ACTIVATE == action || Action.UPDATE == action) {

            ServiceStateType state = ServiceStateType.Factory.newInstance();
            state.setStringValue(SubSvcStateType.ACTIVE.toString());
            return state;
        }
        if (Action.DEACTIVATE == action) {
            ServiceStateType state = ServiceStateType.Factory.newInstance();
            state.setStringValue(SubSvcStateType.INACTIVE.toString());
            return state;
        }
        if (Action.INACTIVE == action) {
            ServiceStateType state = ServiceStateType.Factory.newInstance();
            state.setStringValue(SubSvcStateType.INACTIVE.toString());
            return state;
        }

        if (Action.RESUME == action || Action.SWAP == action) {
            ServiceStateType state = ServiceStateType.Factory.newInstance();
            state.setStringValue(SubSvcStateType.ACTIVE.toString());
            return state;
        }
        if (Action.BLOCK == action) {
            ServiceStateType state = ServiceStateType.Factory.newInstance();
            state.setStringValue(SubSvcStateType.ACTIVE.toString());
            return state;
        }

        if (Action.SUSPEND == action) {
            ServiceStateType state = ServiceStateType.Factory.newInstance();
            state.setStringValue(SubSvcStateType.ACTIVE.toString());
            return state;
        }

        if (Action.DELETE.equals(action)) {
            ServiceStateType state = ServiceStateType.Factory.newInstance();
            state.setStringValue(SubSvcStateType.INACTIVE.toString());
            return state;
        }
        throw new IllegalArgumentException("Unknown action: " + action);
    }

    public ProvisionStateEnum find(SubSvcStateType state) {
        return find(state.getProvisionState());

    }

    public ProvisionStateEnum find(String state) {
        if (state == null) {
            return null;
        }
        ProvisionStateEnum pse = ProvisionStateEnum.find(state);
        if (pse == null) {
            logger.warn("ProvisionStateEnum does not contain " + state);
            pse=ProvisionStateEnum.BUG_IN_BSS_ADAPTER;
        }
        return pse;
    }
}

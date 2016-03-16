package dk.yousee.smp5.casemodel.vo.helpers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dk.yousee.smp5.order.model.Action;
import dk.yousee.smp5.order.model.OrderDataAssociation;
import dk.yousee.smp5.order.model.ProvisionStateEnum;
import dk.yousee.smp5.order.model.ResponseAssociation;
import dk.yousee.smp5.casemodel.SubscriberModel;
import dk.yousee.smp5.order.model.NickName;
import dk.yousee.smp5.order.model.OrderData;
import dk.yousee.smp5.order.model.OrderDataLevel;
import dk.yousee.smp5.order.model.OrderDataType;
import dk.yousee.smp5.order.model.ResponseEntity;

public abstract class BasicUnit {
	private ResponseEntity entity;
	private BasicUnit parent = null;
	private List<BasicUnit> childrenServices = new ArrayList<BasicUnit>();

	/**
	 * The order data this unit is constructing
	 */
	private List<OrderData> orderDatas = new ArrayList<OrderData>();
	private OrderData cloneADDOrderData = null;
	private OrderDataType type;
	private OrderDataLevel level;
	private SubscriberModel model;
	private String externalKey;
	private NickName name;

	public OrderDataType getType() {
		return type;
	}

	public NickName getName() {
		return name;
	}

	protected BasicUnit getParent() {
		return parent;
	}

	public List<BasicUnit> getChildrenServices() {
		return childrenServices;
	}

	public String getExternalKey() {
		return externalKey;
	}

	public ResponseEntity getEntity() {
		return entity;
	}

	/**
	 * constructs a basic unit with relevant settings.<br/>
	 * 1) Establish entity reference.<br/>
	 * 2) Establish reference to parent and parents reference to me<br/>
	 *
	 * @param model
	 *            that is under construction
	 * @param externalKey
	 *            instance id
	 * @param type
	 *            "class id"
	 * @param level
	 *            "where in hierarchy"
	 * @param parent
	 *            the service plan this unit belongs to (null means no parent,
	 *            it could be a service plan it self)
	 */
	protected BasicUnit(SubscriberModel model, String externalKey, OrderDataType type, OrderDataLevel level, NickName name, BasicUnit parent) {
		this.model = model;
		this.type = type;
		this.level = level;
		this.externalKey = externalKey;
		this.parent = parent;
		this.name = name;
		if (parent != null) {
			parent.addChild(this);
			if (parent.getEntity() != null) {
				this.entity = findBelong2me(parent.getEntity());
			}
		} else {
			if (model.getResponse() != null) {
				this.entity = findBelong2me(model.getResponse().getSmp());
			}
		}
	}

	/**
	 * Searches the subscribers existing entities to find the one this unit
	 * belongs to
	 *
	 * @param entity
	 *            starting point (first called from top)
	 * @return the entity that matches, null if none matches
	 */
	ResponseEntity findBelong2me(ResponseEntity entity) {
		if (entity == null)
			return null;
		if (this.externalKey.equals(entity.getExternalKey()) && (this.getType().equals(entity.getType())))
			return entity;
		for (ResponseEntity child : entity.getEntities()) {
			ResponseEntity mine = findBelong2me(child);
			if (mine != null)
				return mine;
		}
		return null;
	}

	protected void assignValueToKey(String key, String value) {
		OrderData orderData = getDefaultOrderData();
		if (getValueByKeyInResponse(key) != null && getValueByKeyInResponse(key).equals(value)) {
			// ignore
		} else {
			orderData.getParams().put(key, value);
		}
	}

	protected void assignAssociation(OrderDataAssociation orderDataAssociation) {
		OrderData orderData = getDefaultOrderData();
		orderData.getAssociations().add(orderDataAssociation);
	}

	protected ResponseAssociation getAssociationByTypeInResponse(String type) {
		if (entity != null && entity.getAssociations() != null)
			for (ResponseAssociation responseAssociation : entity.getAssociations()) {
				if (responseAssociation.getAssociationType().equalsIgnoreCase(type)) {
					return responseAssociation;
				}
			}
		return null;
	}

	protected String getValueByKeyInResponse(String key) {
		if (entity != null && entity.getParams() != null) {
			return entity.getParams().get(key);
		} else {
			return null;
		}
	}

	protected String getValueByKeyInDefaultOrderData(String key) {
		OrderData orderData = getDefaultOrderData();
		return orderData.getParams().get(key);
	}

	/**
	 * @return true : if there is DefaultOrderData.
	 */
	public boolean isDefaultOrderDataExist() {
		return !orderDatas.isEmpty();
	}

	public OrderData getCloneADDOrderData() {
		if (cloneADDOrderData == null) {
			cloneADDOrderData = new OrderData();
			cloneADDOrderData.setType(getType());
			cloneADDOrderData.setExternalKey(getExternalKey());
			cloneADDOrderData.setLevel(level);
			cloneADDOrderData.setAction(Action.ACTIVATE);
			cloneADDOrderData.getParams().putAll(entity.getParams());
			model.getOrder().getOrderData().add(cloneADDOrderData);
			return cloneADDOrderData;
		} else {
			return cloneADDOrderData;
		}
	}

	/**
	 * @return order data. Postcondition: always an object, never null
	 */
	public OrderData getDefaultOrderData() {
		if (orderDatas.isEmpty()) {
			OrderData od = new OrderData();
			od.setType(getType());
			od.setExternalKey(getExternalKey());
			od.setLevel(level);

			if (entity == null) {
				od.setAction(Action.ACTIVATE);
			} else {
				od.setAction(Action.UPDATE);
				// and all paramss.. if present...
				Map params;
				if (od.getParams() == null) {
					params = new HashMap();
					od.setParams(params);
				} else
					params = od.getParams();
				Map eparams = entity.getParams();
				if (eparams != null)
					params.putAll(eparams);
			}
			orderDatas.add(od);
			if (parent == null) {
				model.getOrder().getOrderData().add(od);
			} else {
				// use CloneADDOrderData and Level.SERVICE_HIDDEN for the case
				// that parent exist and we could igonore it.
				if (od.getAction() == Action.ACTIVATE && parent.getEntity() != null) {
					getDefaultOrderData().setParent(parent.getCloneADDOrderData());
					OrderData po = parent.getCloneADDOrderData();
					parent.getCloneADDOrderData().setLevel(OrderDataLevel.SERVICE_HIDDEN);
					po.getChildren().add(od);
				} else {
					getDefaultOrderData().setParent(parent.getDefaultOrderData());
					OrderData po = parent.getDefaultOrderData();
					po.getChildren().add(od);
				}

			}
		}
		return orderDatas.get(0);
	}

	protected void addChild(BasicUnit unit) {
		childrenServices.add(unit);
	}

	/**
	 * Cause to send delete command to sigma
	 */
	public void delete() {
		// to delete , we should
		if (parent != null) {
			parent.getDefaultOrderData().setLevel(OrderDataLevel.SERVICE_HIDDEN);
		}
		sendAction(Action.DELETE);
	}

	/**
	 * Cause to send the specified action to sigma
	 *
	 * @param action
	 *            - to send to sigma
	 */
	public void sendAction(Action action) {
		OrderData od = getDefaultOrderData();
		od.setAction(action);
	}

	/**
	 * Cause to send the specified action to sigma - cascading to siblings
	 *
	 * @param action
	 *            - to send to sigma
	 */
	public void cascadeSendAction(Action action) {
		this.sendAction(action);
		for (BasicUnit child : getChildrenServices()) {
			child.cascadeSendAction(action);
		}
	}

	public ProvisionStateEnum getServicePlanState() {
		if (getEntity() != null) {
			return getEntity().getState();
		} else {
			return null;
		}
	}

	/**
	 * Is there progress under this ?
	 *
	 * @return null or a collection of units with progress
	 */
	public List<BasicUnit> filterProgress() {
		List<BasicUnit> res = null;
		if (isInProgress()) {
			res = new ArrayList<BasicUnit>();
			res.add(this);
		}
		for (BasicUnit unit : getChildrenServices()) {
			List<BasicUnit> units = unit.filterProgress();
			if (units != null) {
				if (res == null)
					res = new ArrayList<BasicUnit>();
				res.addAll(units);
			}
		}
		return res;
	}

	/**
	 * Is this unit in progress of being updated by SMP
	 *
	 * @return yes (note that it is only Child service levels that ever gets in
	 *         this state)
	 */
	public boolean isInProgress() {
		ProvisionStateEnum state = getEntity() == null ? null : getEntity().getState();
		return state != null && state.isProgress();
	}

	public boolean isDelete() {
		return (orderDatas.size() == 0 || orderDatas == null ) ? false : orderDatas.get(0).getAction() == Action.DELETE;
	}
	
	@Override
	public String toString() {
		final StringBuilder sb = new StringBuilder();
		sb.append("{");
		sb.append(" type=").append(type);
		sb.append(", level=").append(level);
		sb.append(", model=").append(model);
		sb.append(", externalKey='").append(externalKey).append('\'');
		sb.append('}');
		return sb.toString();
	}

}

package dk.yousee.smp5.order.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import dk.yousee.smp5.order.model.ResponseAssociation;
import dk.yousee.smp5.order.model.ProvisionStateEnum;
import dk.yousee.smp5.order.model.ResponseEntityLevel;
import dk.yousee.smp5.order.model.OrderDataType;

public class ResponseEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -7030868904308285833L;
	private ResponseEntityLevel level;
	private ProvisionStateEnum state;
	private String lid;
	private String externalKey;
	private OrderDataType type;
	private List<ResponseAssociation> associations = null;
	private Map<String, String> params;
	private List<ResponseEntity> entities = null;

	public String getExternalKey() {
		return externalKey;
	}

	public void setExternalKey(String externalKey) {
		this.externalKey = externalKey;
	}

	public void setType(OrderDataType type) {
		this.type = type;
	}

	public Map<String, String> getParams() {
		return params;
	}

	public String getParmByKey(String key) {
		if (params != null) {
			return params.get(key);
		}
		return null;
	}

	public void setParams(Map<String, String> params) {
		this.params = params;
	}

	public List<ResponseEntity> getEntities() {
		if (entities == null)
			entities = new ArrayList<ResponseEntity>();
		return entities;
	}

	public void setEntities(List<ResponseEntity> entities) {
		this.entities = entities;
	}

	public OrderDataType getType() {
		return type;
	}

	public void setValue(OrderDataType type) {
		this.type = type;
	}

	public ResponseEntityLevel getLevel() {
		return level;
	}

	public void setLevel(ResponseEntityLevel level) {
		this.level = level;
	}

	public ProvisionStateEnum getState() {
		return state;
	}

	public void setState(ProvisionStateEnum state) {
		this.state = state;
	}

	public List<ResponseAssociation> getAssociations() {
		if (associations == null) {
			associations = new ArrayList<ResponseAssociation>();
		}
		return associations;
	}

	public void setAssociations(List<ResponseAssociation> associations) {
		this.associations = associations;
	}

	public String getLid() {
		return lid;
	}

	public void setLid(String lid) {
		this.lid = lid;
	}

}

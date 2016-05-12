package dk.yousee.smp.order.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by IntelliJ IDEA. User: aka Date: Oct 13, 2010 Time: 10:41:55 AM A
 * response element
 */
public final class ResponseEntity implements Serializable {
	static final private long serialVersionUID = -756965011379990454L;
	/**
	 * SERVICE, CHILD_SERVICE The only client assigning this value seems to be a
	 * case client
	 */
	private ResponseEntityLevel level;

	public ResponseEntityLevel getLevel() {
		return level;
	}

	public void setLevel(ResponseEntityLevel level) {
		this.level = level;
	}

	private String lid;

	public String getLid() {
		return lid;
	}

	public void setLid(String lid) {
		this.lid = lid;
	}

	/**
	 * State returned from SMP
	 */
	private ProvisionStateEnum state;

	public ProvisionStateEnum getState() {
		return state;
	}

	public void setState(ProvisionStateEnum state) {
		this.state = state;
	}

	/**
	 * If instance key in smp is known, put it here. For features put the
	 * feature name in here.
	 */
	private String externalKey;

	public String getExternalKey() {
		return externalKey;
	}

	public void setExternalKey(String externalKey) {
		this.externalKey = externalKey;
	}

	/**
	 * Value of the parameter, parameter name is put in externalkey , for
	 * services value contains service type as defined in Constants
	 */
	private OrderDataType type;

	public OrderDataType getType() {
		return type;
	}

	public void setValue(OrderDataType type) {
		this.type = type;
	}

	/**
	 * Assosiations to the entity
	 */
	private List<ResponseAssociation> associations = null;

	public List<ResponseAssociation> getAssociations() {
		if (associations == null) {
			associations = new ArrayList<ResponseAssociation>();
		}
		return associations;
	}

	public void setAssociations(List<ResponseAssociation> associations) {
		this.associations = associations;
	}

	private Map<String, String> params;

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

	private List<ResponseEntity> entities = null;

	public List<ResponseEntity> getEntities() {
		if (entities == null)
			entities = new ArrayList<ResponseEntity>();
		return entities;
	}

	public void setEntities(List<ResponseEntity> entities) {
		this.entities = entities;
	}
}

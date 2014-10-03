package dk.yousee.smp.casemodel.vo.backup;

import dk.yousee.smp.casemodel.SubscriberModel;
import dk.yousee.smp.casemodel.vo.BusinessPosition;
import dk.yousee.smp.casemodel.vo.ModemId;
import dk.yousee.smp.casemodel.vo.helpers.BasicUnit;
import dk.yousee.smp.casemodel.vo.helpers.PropHolder;
import dk.yousee.smp.order.model.OrderDataLevel;
import dk.yousee.smp.order.model.OrderDataType;
import dk.yousee.smp.order.model.ServicePrefix;

/**
 * Definition of backup.
 */
public class Backup extends BasicUnit {

	public static OrderDataLevel LEVEL = OrderDataLevel.CHILD_SERVICE;
	public static OrderDataType TYPE = new OrderDataType(ServicePrefix.SubSvcSpec, "backup");

	public Backup(SubscriberModel model, String externalKey, BackupService parent) {
		super(model, externalKey, TYPE, LEVEL, null, parent);
		parent.setBackup(this);
	}

	public Backup(SubscriberModel model, BusinessPosition position) {
		super(model, model.key().generateUUID(), TYPE, LEVEL, null, new BackupService(model, model.key().generateUUID()));
		business_position.setValue(position.getId());
		getParent().setBackup(this);
	}

	public BackupService getParent() {
		return (BackupService) super.getParent();
	}

	/**
	 * Name of field
	 */
	public static final String BUSINESS_POSITION = "business_position";

	public PropHolder business_position = new PropHolder(this, BUSINESS_POSITION, true);

	public BusinessPosition getPosition() {
		return BusinessPosition.create(business_position.getValue());
	}

	public ModemId getModemId() {
		return ModemId.create(modem_id.getValue());
	}

	public static final String PRODUCT_CODE = "product_code";
	public PropHolder product_code = new PropHolder(this, PRODUCT_CODE, true);

	public static final String YSPRO_PCODE = "yspro_pcode";
    public PropHolder yspro_pcode = new PropHolder(this, YSPRO_PCODE, false);

	public static final String YSPRO_PROVISIONINGID = "yspro_provisioningid";
	public PropHolder yspro_provisioningid = new PropHolder(this, YSPRO_PROVISIONINGID, false);

	public static final String BACKUP_DISK_SIZE = "cloudbackup_size";
	public PropHolder backup_disk_size = new PropHolder(this, BACKUP_DISK_SIZE, false);
	
    public static final String MODEM_ID = "modem_id";
    public PropHolder modem_id = new PropHolder(this, MODEM_ID, true);
}

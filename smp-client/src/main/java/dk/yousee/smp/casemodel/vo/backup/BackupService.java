package dk.yousee.smp.casemodel.vo.backup;

import dk.yousee.smp.casemodel.SubscriberModel;
import dk.yousee.smp.casemodel.vo.BusinessPosition;
import dk.yousee.smp.casemodel.vo.ModemId;
import dk.yousee.smp.casemodel.vo.helpers.BasicUnit;
import dk.yousee.smp.order.model.NickName;
import dk.yousee.smp.order.model.OrderDataLevel;
import dk.yousee.smp.order.model.OrderDataType;
import dk.yousee.smp.order.model.ServicePrefix;

public class BackupService extends BasicUnit {
    public static OrderDataLevel LEVEL = OrderDataLevel.SERVICE;
    public static OrderDataType TYPE = new OrderDataType(ServicePrefix.SubSvcSpec, "backup_composed");
    public static NickName NAME = new NickName("backup");

    public BackupService(SubscriberModel model, String externalKey) {
        super(model, externalKey, TYPE, LEVEL, NAME, null);
        model.getServiceLevelUnit().add(this);
    }

    private Backup backup;

    public Backup getBackup() {
        return backup;
    }

    public void setBackup(Backup backup) {
        this.backup = backup;
    }

    public BusinessPosition getPosition() {
        return getBackup() == null ? null : getBackup().getPosition();
    }

    public ModemId getModemId() {
        return getBackup() == null ? null : getBackup().getModemId();
    }
}

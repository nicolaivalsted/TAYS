package dk.yousee.smp.casemodel.vo.sikpakke;

import dk.yousee.smp.casemodel.SubscriberModel;
import dk.yousee.smp.casemodel.vo.BusinessPosition;
import dk.yousee.smp.casemodel.vo.helpers.BasicUnit;
import dk.yousee.smp.order.model.NickName;
import dk.yousee.smp.order.model.OrderDataLevel;
import dk.yousee.smp.order.model.OrderDataType;
import dk.yousee.smp.order.model.ServicePrefix;

public class SikkerhedspakkeService extends BasicUnit {
    public static OrderDataLevel LEVEL = OrderDataLevel.SERVICE;
    public static OrderDataType TYPE = new OrderDataType(ServicePrefix.SubSvcSpec, "sikpakke_composed");
    public static NickName NAME = new NickName("sikpakke");

    public SikkerhedspakkeService(SubscriberModel model, String externalKey) {
        super(model, externalKey, TYPE, LEVEL, NAME, null);
        model.getServiceLevelUnit().add(this);
    }

    private Sikkerhedspakke sikkerhedspakke;

    public Sikkerhedspakke getSikkerhedspakke() {
        return sikkerhedspakke;
    }

    public void setSikkerhedspakke(Sikkerhedspakke sikkerhedspakke) {
        this.sikkerhedspakke = sikkerhedspakke;
    }

    public BusinessPosition getPosition() {
        return getSikkerhedspakke() == null ? null : getSikkerhedspakke().getPosition();
    }
}

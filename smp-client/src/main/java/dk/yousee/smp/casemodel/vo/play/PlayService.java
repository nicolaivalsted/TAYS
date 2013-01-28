package dk.yousee.smp.casemodel.vo.play;

import dk.yousee.smp.casemodel.SubscriberModel;
import dk.yousee.smp.casemodel.vo.BusinessPosition;
import dk.yousee.smp.casemodel.vo.helpers.BasicUnit;
import dk.yousee.smp.order.model.NickName;
import dk.yousee.smp.order.model.OrderDataLevel;
import dk.yousee.smp.order.model.OrderDataType;
import dk.yousee.smp.order.model.ServicePrefix;

/**
 * Created by IntelliJ IDEA.
 * User: aka
 * Date: 28/02/12
 * Time: 15.54
 * Play service for music
 */
public class PlayService extends BasicUnit {


    public static OrderDataLevel LEVEL = OrderDataLevel.SERVICE;
    public static OrderDataType TYPE = new OrderDataType(ServicePrefix.SubSvcSpec,"play_composed");
    public static NickName NAME = new NickName("play");

    public PlayService(SubscriberModel model, String externalKey) {
        super(model, externalKey, TYPE, LEVEL, NAME,null);
        model.getServiceLevelUnit().add(this);
    }


    //children
    private Play play;

    public void setPlay(Play play) {
        this.play = play;
    }

    public Play getPlay() {
        return play;
    }

    public BusinessPosition getPosition() {
        return getPlay()==null?null: getPlay().getPosition();
    }

}

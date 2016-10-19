package dk.yousee.smp.casemodel.vo.cbp;

import dk.yousee.smp.casemodel.SubscriberModel;
import dk.yousee.smp.casemodel.vo.helpers.BasicUnit;
import dk.yousee.smp.casemodel.vo.helpers.PropHolder;
import dk.yousee.smp.order.model.OrderDataLevel;
import dk.yousee.smp.order.model.OrderDataType;
import dk.yousee.smp.order.model.ServicePrefix;

import java.util.Random;

/**
 * Created by IntelliJ IDEA.
 * User: m14857
 * Date: Oct 21, 2010
 * Time: 1:50:59 PM
 * Data structure reference to YouSee Data Migration Requirements: 5.5.6	SMP WiFi Service
 */
public class SMPWiFi extends BasicUnit {
    public static OrderDataLevel LEVEL = OrderDataLevel.CHILD_SERVICE;
    public static OrderDataType TYPE = new OrderDataType(ServicePrefix.SubSvcSpec,"wifi_service");

    public SMPWiFi(SubscriberModel model, String externalKey, CableBBService parent) {
        super(model, externalKey, TYPE, LEVEL,null, parent);
        parent.setSmpWiFi(this);
        wifi_service_id.updateValue(externalKey);
    }

    //Type.FEATURE
    public PropHolder wifi_service_id = new PropHolder(this, "wifi_service_id", true);
    public PropHolder wifi_service_product_code = new PropHolder(this, "wifi_service_product_code", true);
    public PropHolder ss_id = new PropHolder(this, "ss_id", true);
    public PropHolder psk = new PropHolder(this, "psk", true);
    public PropHolder psk_5g = new PropHolder(this, "psk_5g", true);
    public PropHolder gw_channel_id = new PropHolder(this, "gw_channel_id", true);
    public PropHolder ss_id_5g = new PropHolder(this, "ss_id_5g", false); // readonly!
    public PropHolder gw_channel_id_5g = new PropHolder(this, "gw_channel_id_5g", false);
    

    public static String generateSsid() {
        char Letter[] = "abdfghjkmnpqrstuvzxy345679".toCharArray();
        String sSSID = "";
        Random generator = new Random();
        for (int i = 0; i < 8; i++) {
            int randomIndex = generator.nextInt(Letter.length);
            sSSID = sSSID + Letter[randomIndex];
        }
        return sSSID;
    }


    public static String generatePsk() {
        char Letter[] = "abdfghjkmnpqrstuvzxy345679".toCharArray();
        String sWPAKEY = "";
        Random generator = new Random();
        for (int i = 0; i < 16; i++) {

            int randomIndex = generator.nextInt(Letter.length);
            if (i > 7) {
                sWPAKEY = sWPAKEY + Letter[randomIndex];
            }
        }
        return sWPAKEY;
    }

}

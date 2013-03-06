package dk.yousee.tays.bbservice.restbase;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author m27236
 */
public enum Type {
    Telefoni("SubSvcSpec:smp_switch_dial_tone_access"),
    Foreningsmail("foreningsmail"),
    WiFi("SubSvcSpec:wifi_service"),
    StaticIp("SubSvcSpec:static_ip_cpe"),
    InternetAccess("SubSvcSpec:internet_access"),
    StdCpe("SubSvcSpec:std_cpe"),
    AddCpe("SubSvcSpec:additional_cpe"),
    CableModem("SubSvcSpec:smp_emta_cm_hsd_access"),
    VoiceMail("SubSvcSpec:primary_voicemail_box"),
    Play("SubSvcSpec:play_composed"),
    TdcMail("SubSvcSpec:tdcmail_composed"),
    EmailUnblock("SubSvcSpec:email_server_unblock"),
    Mobb("SubSvcSpec:mobile_broadband"),
    SimCard("SubSvcSpec:sim_card");
    private String smp_type;
    private static final Map<String, Type> lookup = new HashMap<String, Type>();

    static {
        for (Type t : Type.values()) {
            lookup.put(t.getSmp_type(), t);
        }
    }

    Type(String smp_type) {
        this.smp_type = smp_type;
    }

    public static Type findType(String smpType) {
        return lookup.get(smpType);
    }

    public String getSmp_type() {
        return smp_type;
    }
}

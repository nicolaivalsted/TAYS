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
    BSA("SubSvcSpec:bit_stream_access"),
    StaticIp("SubSvcSpec:static_ip"),
    InternetAccess("SubSvcSpec:internet_access"),
    StdCpe("SubSvcSpec:std_cpe"),
    AddCpe("SubSvcSpec:additional_cpe"),
    CableModem("SubSvcSpec:smp_emta_cm_hsd_access"),
    VoiceMail("SubSvcSpec:primary_voicemail_box"),
    TdcMail("SubSvcSpec:tdcmail_composed"),
    EmailUnblock("SubSvcSpec:email_server_unblock"),
    Mobb("SubSvcSpec:mobile_broadband"),
    SimCard("SubSvcSpec:sim_card"),
    Mta("SubSvcSpec:smp_emta_pc_voip_access"),
    CWiFi("SubSvcSpec:community_wifi"),
    Backup("SubSvcSpec:backup"),
    Mofibo("SubSvcSpec:mofibo_composed"),
    Sikkerhedspakke("SubSvcSpec:security_package"),
    OTT("SubSvcSpec:ott_services_composed"),
    STB("SubSvcSpec:video_cpe_equipment"),
    Video("SubSvcSpec:video_services_composed"),
    SmartCard("SubSvcSpec:smartcard_services_composed"),
    SecurityPackage("SubSvcSpec:security_package_composed"),
    ForenMail("SubSvcSpec:foreningsmail_composed"),
    TdcEmail("SubSvcSpec:tdcmail_composed"),
    DialTone("SubSvcSpec:smp_switch_dial_tone_access");
    
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

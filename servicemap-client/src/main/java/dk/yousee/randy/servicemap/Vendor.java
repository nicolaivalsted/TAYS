package dk.yousee.randy.servicemap;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * User: aka
 * Date: 08/02/12
 * Time: 12.54
 * Vendor
 */
public class Vendor {

    /*
    {
        "id": 1,
        "vendor": "Lena-Christina",
        "isp": "SteffensenSkelgaard",
        "vrf": "VRF_501",
        "activationReference": "m5",
        "note": "YW pilot"
    },
    */

    public static final String ISP="isp";
    public static final String VENDOR="vendor";
    public static final String ACTIVATION_REFERENCE="activationReference";
    public static final String VRF="vrf";
    public static final String NOTE="note";

    public Vendor(JsonObject node) {
        id=node.get("id").getAsLong();
        isp= parse(node, ISP);
        vendor=parse(node, VENDOR);
        vrf= parse(node, VRF);
        activationReference=parse(node, ACTIVATION_REFERENCE);
        note=parse(node, NOTE);
    }

    private String parse(JsonObject node, String key) {
        JsonElement element=node.get(key);
        String res;
        if(element==null){
            res=null;
        } else {
            res=element.getAsString();
        }
        return res;
    }

    private Long id;
    private String isp;
    private String vendor;
    private String activationReference;
    private String vrf;
    private String note;

    public Long getId() {
        return id;
    }

    public String getIsp() {
        return isp;
    }

    public String getVendor() {
        return vendor;
    }

    public String getActivationReference() {
        return activationReference;
    }

    public String getVrf() {
        return vrf;
    }

    public String getNote() {
        return note;
    }


    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("{id=").append(id);
        if(getIsp()!=null)sb.append(", isp='").append(getIsp()).append('\'');
        sb.append(", vendor='").append(vendor).append('\'');
        sb.append(", activationReference='").append(activationReference).append('\'');
        if(getVrf()!=null)sb.append(", vrf='").append(vrf).append('\'');
        if(getNote()!=null)sb.append(", note='").append(getNote()).append('\'');
        sb.append('}');
        return sb.toString();
    }

    public boolean equalsIsp(String isp) {
        return this.isp != null && this.isp.equals(isp);
    }
}

package dk.yousee.randy.voucher;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

/**
 * User: aka
 * Date: 19/10/12
 * Time: 10.00
 * Response from report usage of invoice
 */
public class VoucherResponse {

    String xml;
    int code;
    String clientReference;
    String description;
    String session_id;
    String error=null;

    public VoucherResponse(String error, String description) {
        this.description = description;
        this.error = error;
    }

    public VoucherResponse(String xml, int code, String clientReference, String description, String session_id) {
        this.xml = xml;
        this.code = code;
        this.clientReference = clientReference;
        this.description = description;
        this.session_id = session_id;
    }

    public String getXml() {
        return xml;
    }

    public String getError() {
        return error;
    }

    public int getCode() {
        return code;
    }

    public String getClientReference() {
        return clientReference;
    }

    public String getDescription() {
        return description;
    }

    public String getSession_id() {
        return session_id;
    }

    public JsonElement printJson() {
        JsonObject res = new JsonObject();
        res.addProperty("code", getCode());
        if(getClientReference()!=null)res.addProperty("clientReference", getClientReference());
        if(getDescription()!=null)res.addProperty("description", getDescription());
        if(getError()!=null)res.addProperty("error", getError());
        if(getSession_id()!=null)res.addProperty("session_id", getSession_id());
        return res;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("{");
        sb.append(" \"clientReference\":\"").append(getClientReference()).append('"');
        if (code != 0) sb.append(", \"code\":\"").append(getCode()).append('"');
        if (description != null) sb.append(", \"description\":\"").append(getDescription()).append('"');
        if (error != null) sb.append(", \"error\":\"").append(getError()).append('"');
        if (session_id != null) sb.append(", \"session_id\":\"").append(getSession_id()).append('"');
        sb.append('}');
        return sb.toString();
    }
}

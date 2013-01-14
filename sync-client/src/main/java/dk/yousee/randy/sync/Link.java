package dk.yousee.randy.sync;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.net.MalformedURLException;
import java.net.URL;

/**
* Created with IntelliJ IDEA.
* User: aka
* Date: 13/01/13
* Time: 09.11
* Link to use
*/
public class Link {
    private String rel;
    private URL url;
    private String href;
    private String mediatype;
    private String type;
    private String error;

    public Link(JsonObject json)  {
        rel = json.get("rel").getAsString();
        href = json.get("href").getAsString();
        try {
            url = new URL(href);
        } catch (MalformedURLException e) {
            error=e.getMessage();
        }
        JsonElement jm = json.get("mediatype");
        if(jm!=null){
            mediatype = jm.getAsString();
        }
        type = json.get("type").getAsString();
    }

    public String getRel() {
        return rel;
    }

    public String getHref() {
        return href;
    }

    public URL getUrl() {
        return url;
    }

    public void setUrl(URL url) {
        this.url = url;
    }

    public String getMediatype() {
        return mediatype;
    }

    public String getType() {
        return type;
    }

    public String getError() {
        return error;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("{\"rel\":\"").append(rel).append('"');
        if (href != null) sb.append(", \"href\":\"").append(href).append('"');
        if (mediatype != null) sb.append(", \"mediatype\":\"").append(mediatype).append('"');
        if (getType() != null) sb.append(", \"type\":\"").append(getType()).append('"');
        if (getError() != null) sb.append(", \"error\":\"").append(getError()).append('"');
        sb.append('}');
        return sb.toString();
    }
}

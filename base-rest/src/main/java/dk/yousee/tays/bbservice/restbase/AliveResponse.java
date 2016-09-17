/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.yousee.tays.bbservice.restbase;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author jablo
 */
@XmlRootElement
public class AliveResponse {
    private String message;
    private String version;
	private List<Link> links = new ArrayList<Link>();

    public AliveResponse() {
    }

    public AliveResponse(String message) {
        this.message = message;
    }

    public AliveResponse(String message, String version) {
        this.message = message;
        this.version = version;
    }

    public List<Link> getLinks() {
        return links;
    }

    public void setLinks(List<Link> links) {
        this.links = links;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public void addLink(Link link) {
        links.add(link);
    }
}

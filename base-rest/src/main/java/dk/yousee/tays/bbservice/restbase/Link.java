package dk.yousee.tays.bbservice.restbase;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author m27236
 */
@XmlRootElement(name="link")
@XmlAccessorType(XmlAccessType.FIELD)
public class Link {
    
    private String rel;
    private String href;
    private String method;

    public Link(){        
    }

    public Link(String rel, String href, String method) {
        this.rel = rel;
        this.href = href;
        this.method = method;
    }
    
    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getRel() {
        return rel;
    }

    public void setRel(String rel) {
        this.rel = rel;
    }
}

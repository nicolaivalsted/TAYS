package dk.yousee.smp.order.model;

/**
 * Created with IntelliJ IDEA.
 * User: aka
 * Date: 04/06/12
 * Time: 08.07
 * Contains the xml send to SMP and received from SMP
 * Note this VO is not serializable. Not mend to be remote.
 */
public final class SmpXml {

    /**
     * @param request the string made by convertRequest
     * @param response XML string with what came back from SMP
     */
    public SmpXml(String request, String response) {
        this.request = request;
        this.response = response;
    }

    private String request;
    private String response;

    /**
     * @return Request send to SMP. It is XML as a string
     */
    public String getRequest() {
        return request;
    }

    /**
     * @return response that came back from SMP. It is XML as a string
     */
    public String getResponse() {
        return response;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("{\"request\":\"").append(getRequest()).append('"');
        if (getResponse() != null) sb.append(", \"response\":\"").append(response).append('"');
        sb.append('}');
        return sb.toString();
    }
}

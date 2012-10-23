package dk.yousee.randy.kasia;

/**
 * User: aka
 * Date: 19/10/12
 * Time: 09.59
 * Request for invoice
 */
public class InvoiceRequest {

    private String customer;
    private String salesItem;
    private String title;
    private String user;
    private String system;

    public InvoiceRequest(String customer, String salesItem, String title, String user, String system) {
        this.customer = customer;
        this.salesItem = salesItem;
        this.title = title;
        this.user = user;
        this.system = system;
    }

    public String printJson() {
        String res;
        res = String.format(
            "{\n" +
                "        \"kundeid\" : \"" + customer + "\",\n" +
                "        \"handlinger\" : [{\n" +
                "        \"handling\" : \"OPRET\",\n" +
                "            \"varenr\" : \"" + salesItem + "\"\n" +
                "    }],\n" +
                "        \"info\" : {\n" +
                "        \"klient\" : {\n" +
                "            \"funktion\" : \"rent-movie\",\n" +
                "                \"bruger\" : \"" + user + "\",\n" +
                "                \"system\" : \"" + system + "\"\n" +
                "        },\n" +
                "        \"data\" : {\n" +
                "            \"title\" : \"" + title + "\"\n" +
                "        }\n" +
                "    }\n" +
                "}\n");

        return res;
    }
}

package dk.yousee.randy.voucher;

/**
 * User: aka
 * Date: 19/10/12
 * Time: 09.59
 * Request for using voucher
 */
public class VoucherRequest {

    private String customer;
    private String asset;
    private String system="YOUBIO";
    private String clientReference;
    private String drm_id;
    private String provider;
    private Voucher voucher;

    public VoucherRequest(String customer, String asset, String clientReference, String drm_id, String provider, Voucher voucher) {
        this.customer = customer;
        this.asset = asset;
        this.clientReference = clientReference;
        this.drm_id = drm_id;
        this.provider = provider;
        this.voucher = voucher;
    }

    public String printXml() {
        String res;
        res=String.format("<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:vouc=\"http://voucher.smarttv.dk/\">\n" +
            "   <soapenv:Header/>\n" +
            "   <soapenv:Body>\n" +
            "      <vouc:consumeTicket>\n" +
            "         <arg0>\n" +
            "            <asset>%s</asset>\n" +
            "            <correlator>%s</correlator>\n" +
            "            <customer>%s</customer>\n" +
            "            <drm_id>%s</drm_id>\n" +
            "            <provider>%s</provider>\n" +
            "            <system>%s</system>\n" +
            "            <voucher>%s</voucher>\n" +
            "         </arg0>\n" +
            "      </vouc:consumeTicket>\n" +
            "   </soapenv:Body>\n" +
            "</soapenv:Envelope>",asset, clientReference,customer,drm_id,provider,system,voucher.toString());
        return res;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("{\"customer\":\"").append(customer).append('"');
        if (asset != null) sb.append(", \"asset\":\"").append(asset).append('"');
        if (system != null) sb.append(", \"system\":\"").append(system).append('"');
        if (clientReference != null) sb.append(", \"clientReference\":\"").append(clientReference).append('"');
        if (drm_id != null) sb.append(", \"drm_id\":\"").append(drm_id).append('"');
        if (provider != null) sb.append(", \"provider\":\"").append(provider).append('"');
        if (voucher != null) sb.append(", \"voucher\":").append(voucher);
        sb.append('}');
        return sb.toString();
    }
}

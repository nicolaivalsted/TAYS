package dk.yousee.randy.voucher;

import dk.yousee.randy.base.AbstractConnector;

/**
 * User: aka
 * Date: 09/04/12
 * Time: 14.46
 * Class that helps managing HttpClient
 */
public class VoucherConnectorImpl extends AbstractConnector {

    public static final String PREPROD_VOUCHER_HOST="http://192.168.98.10:8080";
    public static final String VOUCHER_HOST="http://smt-h3106.yousee.dk:8080";
    private String voucherHost;

    public String getVoucherHost() {
        return voucherHost;
    }

    public void setVoucherHost(String voucherHost) {
        this.voucherHost = voucherHost;
    }

    public VoucherConnectorImpl() {
        super();
    }

}


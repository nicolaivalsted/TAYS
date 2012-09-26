package dk.yousee.smp.smpclient;

/**
 * Created with IntelliJ IDEA.
 * User: aka
 * Date: 10/04/12
 * Time: 15.45
 * A factory to produce access to web-service to SMP
 */
public class SmpClientFactory {

    public static SmpClient create(SmpConnectorImpl connector) {
        return new HttpSoapClientImpl(connector);
    }
}

package dk.yousee.smp5.smp5client;


public class Smp5ClientFactory {
	
	public static Smp5Client create(Smp5ConnectorImpl connector) {
        return new HttpSoapClientImpl(connector);
    }
	
	public static Smp5Client createJMS(Smp5ConnectorImpl connector) {
        return new JMSClientImpl(connector);
    }

}

package dk.yousee.smp5.smp5client;

import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

/**
 * @author m64746
 *
 *         Date: 04/01/2016 Time: 15:27:12
 */
public class ContextUtil {

	public static Context getInitialContext() throws NamingException {
		Properties props = new Properties();
		props.setProperty("java.naming.factory.initial", "org.jnp.interfaces.NamingContextFactory");
		props.setProperty("java.naming.factory.url.pkgs", "org.jboss.naming");
		props.setProperty("java.naming.provider.url", "localhost:1099");
		Context context = new InitialContext(props);
		return context;
	}

}

package dk.yousee.smp.order.client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;

import com.caucho.hessian.client.HessianProxyFactory;

public class HessianProxyOverProxy extends HessianProxyFactory {


	private Proxy proxy = null;
	
	public HessianProxyOverProxy(Proxy proxy) {
		super();
		this.proxy=proxy;
	}

	protected URLConnection openConnection(URL url) throws IOException{
//		Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("sltarray02.tdk.dk", 8080));
		URLConnection conn = url.openConnection(proxy);
		conn.setDoOutput(true);
		
		if (super.getReadTimeout()  > 0) {
		      try {
			conn.setReadTimeout((int) super.getReadTimeout());
		      } catch (Throwable e) {
		      }
		    }
		    conn.setRequestProperty("Content-Type", "x-application/hessian");
		
		return conn;
	}
}

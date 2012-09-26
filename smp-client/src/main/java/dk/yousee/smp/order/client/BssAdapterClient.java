package dk.yousee.smp.order.client;

import java.net.MalformedURLException;
import java.net.Proxy;

import com.caucho.hessian.client.HessianProxyFactory;

import dk.yousee.smp.order.model.OrderService;

/**
 * The objective of this class is to make it easy to call the hessian service.<br/>
 * This class really belongs to a bss-asapter-client - it has nothing to do with the pure API - it is a protocol
 * , easy to use.
 * @deprecated do not use Hessian anymore, use on board
 */
public class BssAdapterClient {

    /**
     * value is decided from fact that 1 minute is a long time
     */
    public static final long DEFAULT_READ_TIME_OUT_MS =60L*1*1000;

	private String url;
	private OrderService orderService;
    private long timeout;
    private Proxy proxy = null;

    /**
     * Creates an hessian client to BSS Adapter.
     *
     * @param url the url to the service  Sample value: http://194.239.248.4:50001/bss-adapter/order.service
     */
    public BssAdapterClient(String url) {
        this(url,DEFAULT_READ_TIME_OUT_MS);       
    }
    
    /**
     * Creates an hessian client to BSS Adapter.
     *
     * @param url the url to the service  Sample value: http://194.239.248.4:50001/bss-adapter/order.service
     * @param proxy set null if not on UAT....
     */
    public BssAdapterClient(String url, Proxy proxy){
    	this(url,DEFAULT_READ_TIME_OUT_MS,proxy);  
    }

    /**
     * Creates an hessian client to BSS Adapter. with timeout
     *
     * @param url the url to the service  Sample value: http://194.239.248.4:50001/bss-adapter/order.service
     * @param timeout - read timeout, if the service has not responded before
     * this number of milli seconds a timeout exception will be thrown.
     */
    public BssAdapterClient(String url, long timeout) {
       this(url,timeout,null);
    }
    
    /**
     * Creates an hessian client to BSS Adapter. with timeout
     *
     * @param url the url to the service  Sample value: http://194.239.248.4:50001/bss-adapter/order.service
     * @param timeout - read timeout, if the service has not responded before
     * @param proxy null if prod - proxy to teknik
     * this number of milli seconds a timeout exception will be thrown.
     */
    public BssAdapterClient(String url, long timeout, Proxy proxy) {
        this.url = url;
        this.timeout = timeout;
        this.proxy = proxy;
    }

    /**
     * @return a proxy instance ready to serve requests.
     */
    public OrderService getOrderService() {
 	
    	if (orderService == null) {
    		if(proxy!=null){
    			orderService = getOrderServiceOverProxy();
    		}else{
		        HessianProxyFactory factory = new HessianProxyFactory();
		        factory.setChunkedPost(false);
	            factory.setReadTimeout(timeout);
		        try {
					orderService = (OrderService) factory.create(OrderService.class, url);
				} catch (MalformedURLException e) {
					throw new IllegalArgumentException(e.getMessage());
				}
    		}
    	}
        return orderService;
    }
    
    /**
     * @return a proxy instance ready to serve requests over Proxy server.
     */
    private OrderService getOrderServiceOverProxy(){
    	if (orderService == null) {
	        HessianProxyOverProxy factory = new HessianProxyOverProxy(proxy);
	        factory.setChunkedPost(false);
            factory.setReadTimeout(timeout);
	        try {
				orderService = (OrderService) factory.create(OrderService.class, url);
			} catch (MalformedURLException e) {
				throw new IllegalArgumentException(e.getMessage());
			}
    	}
    	return orderService;
    }
}

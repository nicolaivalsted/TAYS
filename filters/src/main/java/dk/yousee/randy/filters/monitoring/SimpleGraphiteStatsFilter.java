package dk.yousee.randy.filters.monitoring;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Date;
import java.util.Map.Entry;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * This filter tracks the number of calls, and returns for ... and reports to a
 * graphite instance every minute on the current stats. Timing calculation could
 * be added using something like
 * http://stackoverflow.com/questions/10157548/computing-time-weighted-moving-average
 * <ul><li>E = E + K(S - E) or</li> <li>Exponential smoothing:
 * st=αxt-1+(1+α)st-1; s1=x0</li> </ul> but will require adding locking during
 * the moving time average calculation. Performance tests can decide if this
 * added time is worth it, or if locking on every exit from a web service
 * request will add too much congestion and serializing of execution. <p> Still,
 * locking and calculating would be much less expensive than, say, sending an
 * extra network packet or rpc call on every return.
 *
 * @author Jacob Lorensen, YouSee, 2013-04-18
 */
public class SimpleGraphiteStatsFilter implements Filter {
    private final static Logger log = Logger.getLogger(SimpleGraphiteStatsFilter.class.getName());
    protected FilterConfig filterConfig;
    protected String graphiteHost;
    protected int graphitePort;
    private Timer timer = new Timer();
    protected ConcurrentHashMap<String, StatsValues> graphiteMap = new ConcurrentHashMap();
    // You might want to look into the lock free hash table implementation by Cliff Click, 
    // it's part of the Highly Scalable Java library
    // http://sourceforge.net/projects/high-scale-lib/
    // OR
    // Even NOT do ConcurrentHashMap - if there's a concurrent modification exception, ignore it -
    // It happens only in the very beginning when we collect the different graph names. Once all URLs
    // have been visited, the hashmap does not change - thus no need for concurrency control. Interesting thought.

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info("Graphite Stats filter configuring");
        String filterName = filterConfig.getFilterName();
        graphiteHost = filterConfig.getInitParameter("graphite-host");
        String gPort = filterConfig.getInitParameter("graphite-port");
        if (graphiteHost == null)
            throw new ServletException(filterName + " filter missing parameter graphite-host");
        if (gPort == null)
            throw new ServletException(filterName + " filter missing parameter graphite-port");
        try {
            this.graphitePort = Integer.parseInt(gPort);
        } catch (NumberFormatException e) {
            throw new ServletException(filterName + " graphite-port not legal integer: " + gPort, e);
        }
        this.filterConfig = filterConfig;
        timer.scheduleAtFixedRate(getTimerTask(), 60 * 1000, 60 * 1000);
    }

    @Override
    public void destroy() {
        this.filterConfig = null;
        timer.cancel();
    }

    @Override
    public void doFilter(ServletRequest _request, ServletResponse _response, FilterChain chain)
            throws IOException, ServletException {
        log.log(Level.FINE, "{0} filter executing {1}", new Object[]{filterConfig.getFilterName(), this.getClass().getName()});
        HttpServletRequest req = (HttpServletRequest) _request;
        ServletResponseWithStatus response = new ServletResponseWithStatus((HttpServletResponse) _response);
        String graphiteGraph = graphiteGraph(req);
        Date preCallDate = new Date();
        countCall(graphiteGraph);
        chain.doFilter(_request, response);
        int status = response.getStatus();
        Date postCallDate = new Date();
        countReturn(graphiteGraph, status, postCallDate.getTime() - preCallDate.getTime());
    }

    /**
     * Produce a graphite graph / stats name from a HttpServletRequest. Specific
     * filter implementations can override this to produce the most suitable
     * graph stats name. Examples are: <ul><li>Translate
     * <code>/rootpath/resource/{parameter}/subresource</code> into
     * <code>servername.rootpath.resource.subscriber.subresource</code> so all
     * queries on specifix resources get calculated to the same graph <li>...
     * </ul> The default translation only uses the root path and the HTTP
     * method, ie
     * <code>hostname.dottedrootpath.GET</code>,
     * <code>hostname.dottedrootpath.PUT</code>, &hellip;
     *
     * @param req
     * @return name of the graphite graph to calculate stats for
     */
    protected String graphiteGraph(HttpServletRequest req) {
        String method = req.getMethod(); // GET, PUT, POST, DELETE, ...
        String module = req.getContextPath();
        if (module == null)
            module = "";
        String hostName = null;
        try {
            hostName = InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException ex) {
            Logger.getLogger(SimpleGraphiteStatsFilter.class.getName()).log(Level.SEVERE, null, ex);
        }
        String graphPrefix = hostName + module.replace('/', '.');
        String res = graphPrefix + "." + method;
        System.out.append("GraphiteGraph: " + res);
        return res;
    }

    /**
     * OVerride in children that use different StatsValues
     * @param graphiteGraph
     * @return 
     */
    protected StatsValues countCall(String graphiteGraph) {
        StatsValues stats = getOrCreate(graphiteGraph);
        stats.calls.incrementAndGet();
        return stats;
    }

    protected StatsValues countReturn(String graphiteGraph, int status, long l) {
        StatsValues stats = getOrCreate(graphiteGraph);
        stats.retTotal.incrementAndGet();
        if (status < 300)
            stats.ret200.incrementAndGet();
        else if (status < 400)
            stats.ret300.incrementAndGet();
        else if (status < 500)
            stats.ret400.incrementAndGet();
        else
            stats.ret500.incrementAndGet();
        return stats;
    }

    protected StatsValues getOrCreate(String graphiteGraph) {
        StatsValues stats = graphiteMap.get(graphiteGraph);
        if (stats == null) {
            StatsValues newStats = new_statsValue();
            stats = graphiteMap.putIfAbsent(graphiteGraph, newStats);
            if (stats == null)
                stats = newStats;
        }
        return stats;
    }

    /**
     * Override in children that use different stats value class
     */
    protected StatsValues new_statsValue() {
        return new StatsValues();
    }

    /**
     * Override in children that use different stats values or reporting
     * @return 
     */
    protected TimerTask getTimerTask() {
        return new TimerTask() {
            @Override
            public void run() {
                System.out.println("Reporting stats");
                for (Entry<String, StatsValues> e : graphiteMap.entrySet())
                    System.out.println(e.getKey() + " --> " + e.getValue().calls.getAndSet(0)
                            + ", " + e.getValue().retTotal.getAndSet(0)
                            + ", " + e.getValue().ret200.getAndSet(0)
                            + ", " + e.getValue().ret300.getAndSet(0)
                            + ", " + e.getValue().ret400.getAndSet(0)
                            + ", " + e.getValue().ret500.getAndSet(0));
            }
        };
    }
}

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
public class GraphiteStatsFilter implements Filter {
    private final static Logger log = Logger.getLogger(GraphiteStatsFilter.class.getName());
    protected FilterConfig filterConfig;
    protected InetAddress graphiteHost;
    protected int graphitePort;
    private Timer timer = new Timer();
    private TimerTask graphiteTask;
    protected static ConcurrentHashMap<String, StatsValues> graphiteMap = new ConcurrentHashMap();
    // You might want to look into the lock free hash table implementation by Cliff Click, 
    // it's part of the Highly Scalable Java library
    // http://sourceforge.net/projects/high-scale-lib/
    // OR
    // Even NOT do ConcurrentHashMap - if there's a concurrent modification exception, ignore it -
    // It happens only in the very beginning when we collect the different graph names. Once all URLs
    // have been visited, the hashmap does not change - thus no need for concurrency control. Interesting thought.

    public GraphiteStatsFilter() {
        log.log(Level.WARNING, "{0} creating instance", GraphiteStatsFilter.class.getName());
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        log.info("Graphite Stats filter configuring");
        String filterName = filterConfig.getFilterName();
        try {
            graphiteHost = InetAddress.getByName(filterConfig.getInitParameter("graphite-host"));
        } catch (UnknownHostException ex) {
            throw new RuntimeException("graphite-host " + filterConfig.getInitParameter("graphite-host") + " not found", ex);
        }
        String gPort = filterConfig.getInitParameter("graphite-port");
        if (graphiteHost == null)
            throw new ServletException(filterName + " filter missing parameter graphite-host");
        if (gPort == null) {
            log.info("Using default graphite port 2003");
            gPort = "2003";
        }
        try {
            this.graphitePort = Integer.parseInt(gPort);
        } catch (NumberFormatException e) {
            throw new ServletException(filterName + " graphite-port not legal integer: " + gPort, e);
        }
        this.filterConfig = filterConfig;
        graphiteTask = getTimerTask();
        timer.scheduleAtFixedRate(graphiteTask, 60 * 1000, 60 * 1000);
    }

    @Override
    public void destroy() {
        log.log(Level.INFO, "Canceling graphite timer for filter {0}", filterConfig.getFilterName());
        this.filterConfig = null;
        graphiteTask.cancel();
        timer.cancel();        
    }

    @Override
    public void doFilter(ServletRequest _request, ServletResponse _response, FilterChain chain)
            throws IOException, ServletException {
        log.log(Level.FINE, "{0} filter executing {1}", new Object[]{filterConfig.getFilterName(), this.getClass().getName()});
        HttpServletRequest req = (HttpServletRequest) _request;
        ServletResponseWithStatus response = new ServletResponseWithStatus((HttpServletResponse) _response);
        String graphiteGraph = graphName(req);
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
        return "";
    }

    protected String graphiteGraphPrefix(HttpServletRequest req) {
        String module = req.getContextPath();
        if (module == null)
            module = "";
        String hostName = null;
        try {
            hostName = InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException ex) {
            Logger.getLogger(GraphiteStatsFilter.class.getName()).log(Level.SEVERE, null, ex);
        }
        String graphPrefix = "servers." + hostName + module.replace('/', '.');
        return graphPrefix;
    }

    protected String graphiteGraphPostfix(HttpServletRequest req) {
        return "." + req.getMethod();
    }

    /**
     * OVerride in children that use different StatsValues
     *
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
        stats.executionTime.addAndGet((int) l);
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
     *
     * @return
     */
    protected TimerTask getTimerTask() {
        return new TimerTask() {
            @Override
            public void run() {
                GraphiteConnection graphite = null;
                try {
                    graphite = new GraphiteConnection(graphiteHost, graphitePort);
                    for (Entry<String, StatsValues> e : graphiteMap.entrySet()) {
                        String graph = e.getKey();
                        StatsValues value = e.getValue();
                        // get counters and clear
                        int calls = value.calls.getAndSet(0);
                        int returns = value.retTotal.getAndSet(0);
                        int ret200 = value.ret200.getAndSet(0);
                        int ret300 = value.ret300.getAndSet(0);
                        int ret400 = value.ret400.getAndSet(0);
                        int ret500 = value.ret500.getAndSet(0);
                        int calltime = value.executionTime.getAndSet(0);
                        double avgcalltime = (calls > 0) ? (0.0+calltime/calls) : -1;
                        // log
                        log.log(Level.INFO, "{0} --> {1}, {2}, {3}, {4}, {5}, {6}, {7}",
                                new Object[]{e.getKey(), calls, avgcalltime, returns, ret200, ret300, ret400, ret500 });
                        graphite.sendData(graph + ".calls", calls);
                        graphite.sendData(graph + ".returns", returns);
                        graphite.sendData(graph + ".ret200", ret200);
                        graphite.sendData(graph + ".ret300", ret300);
                        graphite.sendData(graph + ".ret400", ret400);
                        graphite.sendData(graph + ".ret500", ret500);
                        // Only send average call if we have a meaning number, ie calls > 0
                        if (calls > 0)
                            graphite.sendData(graph + ".avgtime", avgcalltime);
                    }
                } catch (IOException ex) {
                    Logger.getLogger(GraphiteStatsFilter.class.getName()).log(Level.SEVERE, null, ex);
                    if (graphite != null)
                        try {
                            graphite.close();
                        } catch (IOException ex1) {
                            Logger.getLogger(GraphiteStatsFilter.class.getName()).log(Level.SEVERE, null, ex1);
                        }
                }
            }
        };
    }

    public String graphName(HttpServletRequest req) {
        return graphiteGraphPrefix(req) + graphiteGraph(req) + graphiteGraphPostfix(req);
    }
}

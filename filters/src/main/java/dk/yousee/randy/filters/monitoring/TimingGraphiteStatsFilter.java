package dk.yousee.randy.filters.monitoring;

import java.io.IOException;
import java.util.Map.Entry;
import java.util.TimerTask;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Track calls, returns and timing.
 *
 * @author Jacob Lorensen, YouSee, 2013-04-18
 */
public class TimingGraphiteStatsFilter extends SimpleGraphiteStatsFilter {
    private final static Logger log = Logger.getLogger(TimingGraphiteStatsFilter.class.getName());

    @Override
    protected StatsValues countCall(String graphiteGraph) {
        StatsValues stats = getOrCreate(graphiteGraph);
        stats.calls.incrementAndGet();
        return stats;
    }

    /**
     *
     * @param graphiteGraph
     * @param status
     * @param l
     * @return
     */
    @Override
    protected StatsValues countReturn(String graphiteGraph, int status, long l) {
        TimingStatsValues stats = (TimingStatsValues) super.countReturn(graphiteGraph, status, l);
        stats.linearWeightMovingAverage(l);
        return stats;
    }

    /**
     * Override in children that use different stats value class
     */
    @Override
    protected StatsValues new_statsValue() {
        return new TimingStatsValues();
    }

    @Override
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
                    log.fine("Reporting stats");
                    for (Entry<String, StatsValues> e : graphiteMap.entrySet()) {
                        log.log(Level.FINER, "{0} --> {1}, {2}, {3}, {4}, {5}, {6}",
                                new Object[]{e.getKey(), e.getValue().calls.getAndSet(0),
                                    e.getValue().retTotal.getAndSet(0),
                                    e.getValue().ret200.getAndSet(0), e.getValue().ret300.getAndSet(0),
                                    e.getValue().ret400.getAndSet(0), e.getValue().ret500.getAndSet(0)});
                        String graph = e.getKey();
                        TimingStatsValues value = (TimingStatsValues) e.getValue();
                        int calls, returns, ret200, ret300, ret400, ret500;
                        double avgtime, maxtime, mintime;
                        calls = value.calls.getAndSet(0);
                        returns = value.retTotal.getAndSet(0);
                        ret200 = value.ret200.getAndSet(0);
                        ret300 = value.ret300.getAndSet(0);
                        ret400 = value.ret400.getAndSet(0);
                        ret500 = value.ret500.getAndSet(0);
                        avgtime = value.movingTimeAverage;
                        maxtime = value.movingMax;
                        mintime = value.movingMin;
                        log.log(Level.INFO, "Sending graphipte {0}: {1}, {2}, {3}, {4}, {5}, {6}, {7}, {8}, {9}", new Object[]{
                                    calls, returns, ret200, ret300, ret400, ret500, avgtime, mintime, maxtime
                                });
                        graphite.sendData(graph + ".calls", calls);
                        graphite.sendData(graph + ".returns", returns);
                        graphite.sendData(graph + ".ret200", ret200);
                        graphite.sendData(graph + ".ret300", ret300);
                        graphite.sendData(graph + ".ret400", ret400);
                        graphite.sendData(graph + ".ret500", ret500);
                        graphite.sendData(graph + ".avgtime", avgtime);
                        graphite.sendData(graph + ".maxtime", maxtime);
                        graphite.sendData(graph + ".mintime", mintime);
                    }
                } catch (IOException ex) {
                    Logger.getLogger(SimpleGraphiteStatsFilter.class.getName()).log(Level.SEVERE, null, ex);
                    if (graphite != null)
                        try {
                            graphite.close();
                        } catch (IOException ex1) {
                            Logger.getLogger(TimingGraphiteStatsFilter.class.getName()).log(Level.SEVERE, null, ex1);
                        }
                }
            }
        };
    }
}
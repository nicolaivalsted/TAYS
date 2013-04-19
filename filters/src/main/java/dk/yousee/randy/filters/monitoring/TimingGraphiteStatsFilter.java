package dk.yousee.randy.filters.monitoring;

import java.util.Map.Entry;
import java.util.TimerTask;
import java.util.logging.Logger;

/**
 * Track calls, returns and timing.
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

    /**
     * Override in children that use different stats values or reporting
     *
     * @return
     */
    @Override
    protected TimerTask getTimerTask() {
        return new TimerTask() {
            @Override
            public void run() {
                System.out.println("Reporting stats");
                for (Entry<String, StatsValues> e : graphiteMap.entrySet()) {
                    TimingStatsValues v = (TimingStatsValues) e.getValue();
                    System.out.println(e.getKey() + " --> " + e.getValue().calls.getAndSet(0)
                            + ", " + e.getValue().retTotal.getAndSet(0)
                            + ", " + e.getValue().ret200.getAndSet(0)
                            + ", " + e.getValue().ret300.getAndSet(0)
                            + ", " + e.getValue().ret400.getAndSet(0)
                            + ", " + e.getValue().ret500.getAndSet(0)
                            + ", " + v.movingTimeAverage
                            + ", " + v.movingMax
                            + ", " + v.movingMin);
                    v.movingMax = v.movingMin = 0; // no locking, ok if we miss samples for moving max/min
                }
            }
        };
    }
}

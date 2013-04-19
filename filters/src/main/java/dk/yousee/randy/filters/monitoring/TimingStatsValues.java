/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.yousee.randy.filters.monitoring;

/**
 *
 * @author jablo
 */
public class TimingStatsValues extends StatsValues {
    private final static double K = 0.1; // Smoothing factor. Weight of new sample vs. old estimate
    volatile double movingTimeAverage = 0;
    volatile double movingMax = 0;
    volatile double movingMin = 0;

    /**
     * @param time
     * @see http://stackoverflow.com/questions/10157548/computing-time-weighted-moving-average
     */
    public synchronized void linearWeightMovingAverage(long time) {
        movingTimeAverage = movingTimeAverage + K * (time - movingTimeAverage);
        if (time > movingMax)
            movingMax = time;
        if (movingMin <= 0 || time < movingMin)
            movingMin = time;
    }
}

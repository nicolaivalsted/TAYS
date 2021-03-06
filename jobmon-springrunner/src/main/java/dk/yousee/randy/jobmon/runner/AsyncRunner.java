package dk.yousee.randy.jobmon.runner;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import javax.annotation.PreDestroy;
import org.apache.log4j.Logger;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class AsyncRunner {
    private static final Logger log = Logger.getLogger(AsyncRunner.class);
    private final List<StoppableRunnable> activeJobs = Collections.synchronizedList(new LinkedList());

    /**
     * Run a runnable on an Async thread.
     *
     * @param r Runnable to run
     */
    @Async
    public void runRunnable(String name, StoppableRunnable r) {
        try {
            activeJobs.add(r);
            r.run();
        } catch (Exception e) {
            log.error("Error running jobname=" + name, e);
        } finally {
            activeJobs.remove(r);
        }
    }

    /**
     * A Runnable with a stop() method, which can be used to try and terminate a
     * background running process in a timely manner upon tomcat shutdown.
     */
    public interface StoppableRunnable extends Runnable {
        void stop();
    }

    public abstract static class Stoppable implements StoppableRunnable {
        private volatile boolean stop = false;

        protected synchronized boolean isStop() {
            return stop;
        }

        @Override
        public synchronized void stop() {
            stop = true;
        }
    }

    @PreDestroy
    void preDestroy() {
        if (activeJobs.isEmpty()) {
            log.info("No background jobs to stop");
            return;
        }
        log.info("Cleaning up " + activeJobs.size() + " running jobs");
        for (StoppableRunnable r : activeJobs) {
            r.stop();
        }
        activeJobs.clear();
        try {
            // Give background jobs a little time to stop and cleanup
            log.info("Waiting 15 seconds for background jobs to terminate");
            Thread.sleep(15 * 1000L);
        } catch (InterruptedException ex) {
            log.info("Background job termination wait interrupted", ex);
        }
    }
}

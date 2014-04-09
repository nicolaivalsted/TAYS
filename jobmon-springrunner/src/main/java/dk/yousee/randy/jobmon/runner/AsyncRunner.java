package dk.yousee.randy.jobmon.runner;

import org.apache.log4j.Logger;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class AsyncRunner {
    private static final Logger log = Logger.getLogger(AsyncRunner.class);

    /**
     * Run a runnable on an Async thread.
     *
     * @param r Runnable to run
     */
    @Async
    public void runRunnable(String name, Runnable r) {
        try {
            r.run();
        } catch (Exception e) {
            log.error("Error running jobname=" + name, e);
        }
    }
}

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.yousee.randy.jobmon.runner;

import dk.yousee.randy.jobmon.runner.AsyncRunner.StoppableRunnable;
import dk.yousee.randy.jobmonclient.JobMonClient;
import dk.yousee.randy.jobmonclient.JobState;
import dk.yousee.randy.jobmonclient.RestException;
import dk.yousee.randy.jobmonclient.RunningJobVo;
import dk.yousee.tays.bbservice.restbase.Message;
import java.util.List;
import javax.ws.rs.core.Response;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * Class to execute a runnable wrapped in jobmon test/start/stop and with jobmon
 * progress reporting.
 *
 * @author Jacob Lorensen, TDC, April 2014.
 */
@Component
public class JobmonRunner {
    private final Logger log = Logger.getLogger(JobmonRunner.class);
    @Autowired
    private JobMonClient jobMonClient;
    @Autowired
    private AsyncRunner runner;

    /**
     * Run a JobmonRunnable asynchronously, guaranteeing (attempting to) that
     * only one such job runs at any one time and that progress is being
     * reported through jobmon.
     *
     * @param jobName job name in jobmon
     * @param expectedRuntime expected running time (see jobmon documentation)
     * handle each row, and end extraction
     * @param run the job executing method, called with a ProgressCallback
     * object to report progress
     * @return the jobmon job created or the existing jobmon job already
     * executing
     */
    public Response jobmonAsyncRunner(final String jobName, long expectedRuntime, final JobmonRunnable run) {
        try {
            Response.ResponseBuilder res;
            // find job in jobmon
            List<RunningJobVo> latestJobs = jobMonClient.findLatestJobs(jobName);
            RunningJobVo latestJob = latestJobs != null && !latestJobs.isEmpty() ? latestJobs.iterator().next() : null;
            // if job is not already running, start it
            if (latestJob == null || (latestJob.getState() != JobState.STARTED && latestJob.getState() != JobState.RUNNING)) {
                final RunningJobVo job = jobMonClient.startRun(jobName, expectedRuntime);
                latestJob = job;
                runner.runRunnable(jobName, new StoppableRunnable() {
                    private JobmonProgressCallback progressCallback;
                    
                    @Override
                    public void run() {
                        progressCallback = new JobmonProgressCallback(job);
                        try {
                            run.run(progressCallback);
                        } catch (Exception e) {
                            log.warn("Exception running " + jobName, e);
                            progressCallback.fail(e.getMessage());
                        }
                    }
                    
                    @Override
                    public void stop() {
                        progressCallback.fail("Terminated");
                        run.stop();
                    }
                });
                res = Response.status(Response.Status.CREATED);
            } else {
                res = Response.ok();
            }
            // return jobInfo for the existing running or the newly started jobmon run
            return res.entity(latestJob).build();
        } catch (RestException se) {
            log.error("Backend error: ", se);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(new Message("Error " + se.getMessage())).build();
        }
    }

    /**
     * Runnable-like interface with a progress reporting callback object
     */
    public interface JobmonRunnable {
        void run(ProgressCallback progress);
        
        void stop();
    }

    /**
     * Progress reporting interface. Call progress.updateProgress() whenever
     * convenient, as often as progress can be reasonably reported. The
     * implementation will update jobmon status in spaced intervals of at least
     * 1 second, keeping the number of remote web service calls low.
     */
    public interface ProgressCallback {
        void updateProgress(String progress);
        
        public void done(String progress);
        
        public void fail(String progress);
    }

    /**
     * Update jobmon job status and periodically (synchronously) send the status
     * to the jobmon web service. This is an inner class so it has access to
     * jobMonClient
     */
    private class JobmonProgressCallback implements ProgressCallback {
        private RunningJobVo job;
        private final static long UPDATE_INTERVAL = 1000L;
        private long lastUpdateTime = 0;
        private volatile boolean failCalled = false;
        
        public JobmonProgressCallback(RunningJobVo job) {
            this.job = job;
        }
        
        @Override
        public synchronized void updateProgress(String progress) {
            if (failCalled)
                return;
            try {
                job.setState(JobState.RUNNING);
                job.setProgress(progress);
                // Limit the number of updates of job status - expensive web service calls (compared to row inserts)
                if (lastUpdateTime < System.currentTimeMillis() - UPDATE_INTERVAL) {
                    lastUpdateTime = System.currentTimeMillis();
                    jobMonClient.updateRun(job);
                }
            } catch (Throwable t) {
                log.warn("Could not update runningJob progress: " + job.getId(), t);
            }
        }
        
        @Override
        public synchronized void done(String progress) {
            if (failCalled)
                return;
            job.setState(JobState.DONE);
            job.setProgress(progress);
            try {
                jobMonClient.updateRun(job);
            } catch (Throwable ex) {
                log.warn("Could not update runningJob progress: " + job.getId(), ex);
            }
        }
        
        @Override
        public synchronized void fail(String progress) {
            failCalled = true;
            job.setState(JobState.FAIL);
            job.setProgress(progress);
            try {
                jobMonClient.updateRun(job);
            } catch (Throwable ex) {
                log.warn("Could not update runningJob progress: " + job.getId(), ex);
            }
        }
    }
}
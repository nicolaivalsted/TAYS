/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.yousee.randy.jobmon.runner;

import dk.yousee.randy.jobmon.runner.JobmonRunner.DefaultJobmonRunnable;
import dk.yousee.randy.jobmon.runner.JobmonRunner.ProgressCallback;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.ws.rs.core.Response;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;

/**
 *
 * @author jablo
 */
@Component
public class DBExtractor {
    private final Logger log = Logger.getLogger(DBExtractor.class);
    @Autowired
    private JobmonRunner jobmonRun;

    /**
     * The database extractor implementation methods
     */
    public interface ExtractorIntf {
        /**
         * Given a ResultSetExtractor, call the Dao to extract from
         *
         * @param rse
         */
        void runExtract(ResultSetExtractor<Integer> rse);

        /**
         * Given a ResultSet (single row), send it to the receiving database.
         * Called from the ResultSetExtractor given to
         * <code>runExtract</code>. This method should treat the ResultSet as
         * <i>read-only</i>; specifically <i>not</i> call
         * <code>next()</code> or
         * <code>close()</code> methods.
         *
         * @param rs
         */
        void rowHandler(ResultSet rs);

        /**
         * Make the final commit etc clean up after sending all data.
         */
        void done();

        /**
         * Tell extractor that the job is terminating early.
         */
        void stop();
    }

    /**
     * Run a db extract job asynchronously, guaranteeing (attempting to) that
     * only one such job runs at any one time and that progress is being
     * reported through jobmon.
     *
     * @param jobName job name in jobmon
     * @param expectedRuntime expected running time (see jobmon documentation)
     * @param extractor object with extractor methods to start extraction,
     * handle each row, and end extraction
     * @param dbw destination writing function
     * @return the job created or the existing job running
     */
    public Response.ResponseBuilder jobmonAsyncExtract(String jobName, long expectedRuntime, final ExtractorIntf extractor) {
        return jobmonRun.jobmonAsyncRunner(jobName, expectedRuntime, new ProgressReportingExtractor(extractor));
    }

    /**
     * Runs a db extract job synchronously, no jobmon status check/updating
     *
     * @param extractor extracting function
     * @return the job created or the existing job running
     */
    public Response.ResponseBuilder syncExtract(final ExtractorIntf extractor) {
        Response.ResponseBuilder res;
        extractor.runExtract(new ProgressReportingExtractor(extractor));
        res = Response.status(Response.Status.CREATED);
        return res;
    }

    /**
     * ResultSetExtractor which will use a ExtractorIntf object's
     * <code>rowHandler</code> and
     * <code>done()</code> methods to handle each row.
     */
    private static class ProgressReportingExtractor extends DefaultJobmonRunnable implements ResultSetExtractor<Integer> {
        private static final Logger log = Logger.getLogger(ProgressReportingExtractor.class);
        private static final int updateInterval = 1000;
        private final ExtractorIntf dbw;
        private ProgressCallback progressCallback;

        /**
         * Create a ProgressReportingExtractor with a default do-nothing progress-reporting functions.
         * @param dbw 
         */
        public ProgressReportingExtractor(ExtractorIntf dbw) {
            this(dbw, new ProgressCallback() {
                @Override
                public void updateProgress(String progress) {
                }

                @Override
                public void done(String progress) {
                }

                @Override
                public void fail(String progress) {
                }
            });
        }

        public ProgressReportingExtractor(ExtractorIntf dbw, ProgressCallback progressCallback) {
            this.dbw = dbw;
            this.progressCallback = progressCallback;
        }

        /**
         * Late binding of the progress reporter
         * @param pcb 
         */
        public void setProgress(ProgressCallback pcb) {
            progressCallback = pcb;
        }

        @Override
        public Integer extractData(ResultSet rs) throws SQLException {
            int rows = 0;
            long start = System.currentTimeMillis();
            try {
                while (!isStop() && rs.next()) {
                    dbw.rowHandler(new ReadOnlyResultSet(rs));
                    rows++;
                    progressCallback.updateProgress("Inserted " + rows + " rows");
                }
            } catch (Exception e) {
                progressCallback.fail(e.getMessage() + " " + rows + " rows");
                log.warn("Job terminated due to uncaught exception time=" + (System.currentTimeMillis() - start));
                throw new RuntimeException("Job terminated due to uncaught exception", e);
            }
            log.info("extract time=" + (System.currentTimeMillis() - start));
            if (!isStop())
                progressCallback.done("Inserted " + rows + " rows");
            else
                progressCallback.fail("Terminated after " + rows + " rows");
            return rows;
        }

        @Override
        public void run(ProgressCallback progress) {
            setProgress(progress);
            dbw.runExtract(this);
        }

        @Override
        public void stop() {
            super.stop();
            dbw.stop();
        }
    };
}
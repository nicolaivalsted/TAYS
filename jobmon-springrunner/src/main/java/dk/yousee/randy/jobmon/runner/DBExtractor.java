/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.yousee.randy.jobmon.runner;

import dk.yousee.randy.jobmon.runner.JobmonRunner.JobmonRunnable;
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
    public Response jobmonAsyncExtract(String jobName, long expectedRuntime, final ExtractorIntf extractor) {
        return jobmonRun.jobmonAsyncRunner(jobName, expectedRuntime, new JobmonRunnable() {
            @Override
            public void run(ProgressCallback progress) {
                // This is the intricate wiring... extractor gets the ResultSetExtractor
                // which in itself will call the handleRow() method in the extractor - ie.
                // control loop and jobmon updates are kept here inside DBExtractor, while 
                // the work is being done in the caller's context.
                extractor.runExtract(new ProgressReportingExtractor(extractor, progress));
            }
        });
    }

    /**
     * Runs a db extract job synchronously, no jobmon status check/updating
     *
     * @param extractor extracting function
     * @return the job created or the existing job running
     */
    public Response syncExtract(final ExtractorIntf extractor) {
        Response.ResponseBuilder res;
        extractor.runExtract(new ProgressReportingExtractor(extractor));
        res = Response.status(Response.Status.CREATED);
        return res.build();
    }

    /**
     * ResultSetExtractor which will use a ExtractorIntf object's
     * <code>rowHandler</code> and
     * <code>done()</code> methods to handle each row.
     */
    private static class ProgressReportingExtractor implements ResultSetExtractor<Integer> {
        private static final Logger log = Logger.getLogger(ProgressReportingExtractor.class);
        private static final int updateInterval = 1000;
        private final ExtractorIntf dbw;
        private ProgressCallback progressCallback;

        public ProgressReportingExtractor(ExtractorIntf dbw) {
            this(dbw, null);
        }

        public ProgressReportingExtractor(ExtractorIntf dbw, ProgressCallback progressCallback) {
            this.dbw = dbw;
            this.progressCallback = progressCallback;
        }

        @Override
        public Integer extractData(ResultSet rs) throws SQLException {
            int rows = 0;
            long start = System.currentTimeMillis();
            try {
                while (rs.next()) {
                    dbw.rowHandler(new ReadOnlyResultSet(rs));
                    rows++;
                    if (progressCallback != null)
                        progressCallback.updateProgress("Inserted " + rows + " rows");
                }
                dbw.done();
            } catch (Exception e) {
                progressCallback.done("Inserted " + rows + " rows");
                log.warn("Job terminated due to uncaught exception time=" + (System.currentTimeMillis() - start));
                throw new RuntimeException("Job terminated due to uncaught exception", e);
            }
            log.info("extract time=" + (System.currentTimeMillis() - start));
            if (progressCallback != null)
                progressCallback.done("Inserted " + rows + " rows");
            return rows;
        }
    };
}
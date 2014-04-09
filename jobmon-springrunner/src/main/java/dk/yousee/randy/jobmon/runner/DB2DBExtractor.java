/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.yousee.randy.jobmon.runner;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
import org.springframework.jdbc.core.RowMapper;

/**
 * A Default implementation of a database-to-database (or Dao to Dao)
 * copying/extraction class. Supply the
 * <code>runExtract()</code> method from DBExtractor.ExtractorIntd and supply a
 * <code>insertRows()</code> method then the DB2DBExtractor can be used with
 * <code>DBExtractor.jobmonAsyncExtract()</code> method to run a database
 * extraction job with automatic jobmon run and progress updates
 *
 * @author Jacob Lorensen, TDC, March 2014.
 */
public abstract class DB2DBExtractor<RowType> implements DBExtractor.ExtractorIntf {
    private final Logger log = Logger.getLogger(DB2DBExtractor.class);
    private final int commitSize;
    private final ArrayList<RowType> rowBuf;
    private final RowMapper<RowType> m;

    public DB2DBExtractor(RowMapper<RowType> m, int commitSize) {
        this.commitSize = commitSize;
        this.rowBuf = new ArrayList(commitSize);
        this.m = m;
    }

    public DB2DBExtractor(RowMapper<RowType> m) {
        this(m, 100);
    }

    /**
     * Send rows to the destination database - with buffering to increase
     * performance
     */
    @Override
    public void rowHandler(ResultSet rs) {
        try {
            rowBuf.add(m.mapRow(rs, 0));
        } catch (SQLException ex) {
            throw new RuntimeException("Could not add output row ", ex);
        }
        if (rowBuf.size() >= commitSize) {
            insertRows(rowBuf);
            rowBuf.clear();
        }
    }

    public abstract void insertRows(List<RowType> rowBuf);

    @Override
    public void done() {
        if (!rowBuf.isEmpty())
            insertRows(rowBuf);
        rowBuf.clear();
    }
}

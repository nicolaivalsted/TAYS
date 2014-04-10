/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.yousee.randy.jobmon.runner;

import java.io.BufferedWriter;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import org.apache.log4j.Logger;

/**
 * Simple CSV extractor - override runExtract() to get data from the right Dao
 * into rowHandler row by row. To be used with streaming csv file output for web
 * services
 */
public abstract class CSVExtractor implements DBExtractor.ExtractorIntf {
    private final static Logger log = Logger.getLogger(CSVExtractor.class);
    private final BufferedWriter writer;
    private boolean headers = false;
    private int rows = 0;
    private long start;

    public CSVExtractor(BufferedWriter writer) {
        this.writer = writer;
    }

    @Override
    public void rowHandler(ResultSet rs) {
        try {
            // headers
            ResultSetMetaData metaData = rs.getMetaData();
            if (!headers) {
                String sep = "";
                for (int i = 1; i <= metaData.getColumnCount(); i++) {
                    writer.write(sep);
                    writer.write(metaData.getColumnLabel(i));
                    sep = ", ";
                }
                writer.newLine();
                headers = true;
            }
            // Data
            String sep = "";
            for (int i = 1; i <= metaData.getColumnCount(); i++) {
                writer.write(sep);
                String val = rs.getString(i);
                writer.write(val == null ? "" : val);
                sep = ", ";
            }
            rows++;
            writer.newLine();
        } catch (IOException ex) {
            throw new RuntimeException("Error writing streaming output rowcount=" + rows, ex);
        } catch (SQLException ex) {
            throw new RuntimeException("Error reading data for extract", ex);
        }
    }

    @Override
    public void done() {
        try {
            log.info("extract time=" + (System.currentTimeMillis() - start));
            writer.write("Lines: " + rows);
            writer.newLine();
            writer.flush();
            writer.close();
        } catch (IOException ex) {
            throw new RuntimeException("Error writing csv output", ex);
        }
    }
    
    @Override
    public void stop() {
        try {
            log.warn("Terminated time=" + (System.currentTimeMillis() - start));
            writer.write("Error: incomplete output");
            writer.newLine();
            writer.flush();
            writer.close();
        } catch (IOException ex) {
            throw new RuntimeException("Error writing csv output", ex);
        }
    }
}

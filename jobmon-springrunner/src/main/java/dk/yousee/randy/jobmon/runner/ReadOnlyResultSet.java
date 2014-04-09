/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package dk.yousee.randy.jobmon.runner;

import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Date;
import java.sql.NClob;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.RowId;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.SQLXML;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Map;

/**
 * ReadOnlyResultSet wrapper so we can guarantee in the callbacks to DBExtractor.ExtractorIntf that
 * the callee does not close() or call next() in the handleRow method. All methods are just 
 * wrapper methods except next() and close() which both throw a RuntimeException
 * @author Jacob Lorensen, TDC, March 2014
 */
public class ReadOnlyResultSet implements ResultSet {
    private final ResultSet wrapped;

    public ReadOnlyResultSet(ResultSet wrapped) {
        this.wrapped = wrapped;
    }

    @Override
    public boolean next() throws SQLException {
        throw new RuntimeException("Readonly resultset, don't call next()");
    }

    @Override
    public void close() throws SQLException {
        throw new RuntimeException("Readonly resultset, don't call close()");
    }

    @Override
    public boolean wasNull() throws SQLException {
        return wrapped.wasNull();
    }

    @Override
    public String getString(int i) throws SQLException {
        return wrapped.getString(i);
    }

    @Override
    public boolean getBoolean(int i) throws SQLException {
        return wrapped.getBoolean(i);
    }

    @Override
    public byte getByte(int i) throws SQLException {
        return wrapped.getByte(i);
    }

    @Override
    public short getShort(int i) throws SQLException {
        return wrapped.getShort(i);
    }

    @Override
    public int getInt(int i) throws SQLException {
        return wrapped.getInt(i);
    }

    @Override
    public long getLong(int i) throws SQLException {
        return wrapped.getLong(i);
    }

    @Override
    public float getFloat(int i) throws SQLException {
        return wrapped.getFloat(i);
    }

    @Override
    public double getDouble(int i) throws SQLException {
        return wrapped.getDouble(i);
    }

    @Override
    public BigDecimal getBigDecimal(int i, int i1) throws SQLException {
        return wrapped.getBigDecimal(i, i1);
    }

    @Override
    public byte[] getBytes(int i) throws SQLException {
        return wrapped.getBytes(i);
    }

    @Override
    public Date getDate(int i) throws SQLException {
        return wrapped.getDate(i);
    }

    @Override
    public Time getTime(int i) throws SQLException {
        return wrapped.getTime(i);
    }

    @Override
    public Timestamp getTimestamp(int i) throws SQLException {
        return wrapped.getTimestamp(i);
    }

    @Override
    public InputStream getAsciiStream(int i) throws SQLException {
        return wrapped.getAsciiStream(i);
    }

    @Override
    public InputStream getUnicodeStream(int i) throws SQLException {
        return wrapped.getUnicodeStream(i);
    }

    @Override
    public InputStream getBinaryStream(int i) throws SQLException {
        return wrapped.getBinaryStream(i);
    }

    @Override
    public String getString(String string) throws SQLException {
        return wrapped.getString(string);
    }

    @Override
    public boolean getBoolean(String string) throws SQLException {
        return wrapped.getBoolean(string);
    }

    @Override
    public byte getByte(String string) throws SQLException {
        return wrapped.getByte(string);
    }

    @Override
    public short getShort(String string) throws SQLException {
        return wrapped.getShort(string);
    }

    @Override
    public int getInt(String string) throws SQLException {
        return wrapped.getInt(string);
    }

    @Override
    public long getLong(String string) throws SQLException {
        return wrapped.getLong(string);
    }

    @Override
    public float getFloat(String string) throws SQLException {
        return wrapped.getFloat(string);
    }

    @Override
    public double getDouble(String string) throws SQLException {
        return wrapped.getDouble(string);
    }

    @Override
    public BigDecimal getBigDecimal(String string, int i) throws SQLException {
        return wrapped.getBigDecimal(string, i);
    }

    @Override
    public byte[] getBytes(String string) throws SQLException {
        return wrapped.getBytes(string);
    }

    @Override
    public Date getDate(String string) throws SQLException {
        return wrapped.getDate(string);
    }

    @Override
    public Time getTime(String string) throws SQLException {
        return wrapped.getTime(string);
    }

    @Override
    public Timestamp getTimestamp(String string) throws SQLException {
        return wrapped.getTimestamp(string);
    }

    @Override
    public InputStream getAsciiStream(String string) throws SQLException {
        return wrapped.getAsciiStream(string);
    }

    @Override
    public InputStream getUnicodeStream(String string) throws SQLException {
        return wrapped.getUnicodeStream(string);
    }

    @Override
    public InputStream getBinaryStream(String string) throws SQLException {
        return wrapped.getBinaryStream(string);
    }

    @Override
    public SQLWarning getWarnings() throws SQLException {
        return wrapped.getWarnings();
    }

    @Override
    public void clearWarnings() throws SQLException {
        wrapped.clearWarnings();
    }

    @Override
    public String getCursorName() throws SQLException {
        return wrapped.getCursorName();
    }

    @Override
    public ResultSetMetaData getMetaData() throws SQLException {
        return wrapped.getMetaData();
    }

    @Override
    public Object getObject(int i) throws SQLException {
        return wrapped.getObject(i);
    }

    @Override
    public Object getObject(String string) throws SQLException {
        return wrapped.getObject(string);
    }

    @Override
    public int findColumn(String string) throws SQLException {
        return wrapped.findColumn(string);
    }

    @Override
    public Reader getCharacterStream(int i) throws SQLException {
        return wrapped.getCharacterStream(i);
    }

    @Override
    public Reader getCharacterStream(String string) throws SQLException {
        return wrapped.getCharacterStream(string);
    }

    @Override
    public BigDecimal getBigDecimal(int i) throws SQLException {
        return wrapped.getBigDecimal(i);
    }

    @Override
    public BigDecimal getBigDecimal(String string) throws SQLException {
        return wrapped.getBigDecimal(string);
    }

    @Override
    public boolean isBeforeFirst() throws SQLException {
        return wrapped.isBeforeFirst();
    }

    @Override
    public boolean isAfterLast() throws SQLException {
        return wrapped.isAfterLast();
    }

    @Override
    public boolean isFirst() throws SQLException {
        return wrapped.isFirst();
    }

    @Override
    public boolean isLast() throws SQLException {
        return wrapped.isLast();
    }

    @Override
    public void beforeFirst() throws SQLException {
        wrapped.beforeFirst();
    }

    @Override
    public void afterLast() throws SQLException {
        wrapped.afterLast();
    }

    @Override
    public boolean first() throws SQLException {
        return wrapped.first();
    }

    @Override
    public boolean last() throws SQLException {
        return wrapped.last();
    }

    @Override
    public int getRow() throws SQLException {
        return wrapped.getRow();
    }

    @Override
    public boolean absolute(int i) throws SQLException {
        return wrapped.absolute(i);
    }

    @Override
    public boolean relative(int i) throws SQLException {
        return wrapped.relative(i);
    }

    @Override
    public boolean previous() throws SQLException {
        return wrapped.previous();
    }

    @Override
    public void setFetchDirection(int i) throws SQLException {
        wrapped.setFetchDirection(i);
    }

    @Override
    public int getFetchDirection() throws SQLException {
        return wrapped.getFetchDirection();
    }

    @Override
    public void setFetchSize(int i) throws SQLException {
        wrapped.setFetchSize(i);
    }

    @Override
    public int getFetchSize() throws SQLException {
        return wrapped.getFetchSize();
    }

    @Override
    public int getType() throws SQLException {
        return wrapped.getType();
    }

    @Override
    public int getConcurrency() throws SQLException {
        return wrapped.getConcurrency();
    }

    @Override
    public boolean rowUpdated() throws SQLException {
        return wrapped.rowUpdated();
    }

    @Override
    public boolean rowInserted() throws SQLException {
        return wrapped.rowInserted();
    }

    @Override
    public boolean rowDeleted() throws SQLException {
        return wrapped.rowDeleted();
    }

    @Override
    public void updateNull(int i) throws SQLException {
        wrapped.updateNull(i);
    }

    @Override
    public void updateBoolean(int i, boolean bln) throws SQLException {
        wrapped.updateBoolean(i, bln);
    }

    @Override
    public void updateByte(int i, byte b) throws SQLException {
        wrapped.updateByte(i, b);
    }

    @Override
    public void updateShort(int i, short s) throws SQLException {
        wrapped.updateShort(i, s);
    }

    @Override
    public void updateInt(int i, int i1) throws SQLException {
        wrapped.updateInt(i, i1);
    }

    @Override
    public void updateLong(int i, long l) throws SQLException {
        wrapped.updateLong(i, l);
    }

    @Override
    public void updateFloat(int i, float f) throws SQLException {
        wrapped.updateFloat(i, f);
    }

    @Override
    public void updateDouble(int i, double d) throws SQLException {
        wrapped.updateDouble(i, d);
    }

    @Override
    public void updateBigDecimal(int i, BigDecimal bd) throws SQLException {
        wrapped.updateBigDecimal(i, bd);
    }

    @Override
    public void updateString(int i, String string) throws SQLException {
        wrapped.updateString(i, string);
    }

    @Override
    public void updateBytes(int i, byte[] bytes) throws SQLException {
        wrapped.updateBytes(i, bytes);
    }

    @Override
    public void updateDate(int i, Date date) throws SQLException {
        wrapped.updateDate(i, date);
    }

    @Override
    public void updateTime(int i, Time time) throws SQLException {
        wrapped.updateTime(i, time);
    }

    @Override
    public void updateTimestamp(int i, Timestamp tmstmp) throws SQLException {
        wrapped.updateTimestamp(i, tmstmp);
    }

    @Override
    public void updateAsciiStream(int i, InputStream in, int i1) throws SQLException {
        wrapped.updateAsciiStream(i, in, i1);
    }

    @Override
    public void updateBinaryStream(int i, InputStream in, int i1) throws SQLException {
        wrapped.updateBinaryStream(i, in, i1);
    }

    @Override
    public void updateCharacterStream(int i, Reader reader, int i1) throws SQLException {
        wrapped.updateCharacterStream(i, reader, i1);
    }

    @Override
    public void updateObject(int i, Object o, int i1) throws SQLException {
        wrapped.updateObject(i, o, i1);
    }

    @Override
    public void updateObject(int i, Object o) throws SQLException {
        wrapped.updateObject(i, o);
    }

    @Override
    public void updateNull(String string) throws SQLException {
        wrapped.updateNull(string);
    }

    @Override
    public void updateBoolean(String string, boolean bln) throws SQLException {
        wrapped.updateBoolean(string, bln);
    }

    @Override
    public void updateByte(String string, byte b) throws SQLException {
        wrapped.updateByte(string, b);
    }

    @Override
    public void updateShort(String string, short s) throws SQLException {
        wrapped.updateShort(string, s);
    }

    @Override
    public void updateInt(String string, int i) throws SQLException {
        wrapped.updateInt(string, i);
    }

    @Override
    public void updateLong(String string, long l) throws SQLException {
        wrapped.updateLong(string, l);
    }

    @Override
    public void updateFloat(String string, float f) throws SQLException {
        wrapped.updateFloat(string, f);
    }

    @Override
    public void updateDouble(String string, double d) throws SQLException {
        wrapped.updateDouble(string, d);
    }

    @Override
    public void updateBigDecimal(String string, BigDecimal bd) throws SQLException {
        wrapped.updateBigDecimal(string, bd);
    }

    @Override
    public void updateString(String string, String string1) throws SQLException {
        wrapped.updateString(string, string1);
    }

    @Override
    public void updateBytes(String string, byte[] bytes) throws SQLException {
        wrapped.updateBytes(string, bytes);
    }

    @Override
    public void updateDate(String string, Date date) throws SQLException {
        wrapped.updateDate(string, date);
    }

    @Override
    public void updateTime(String string, Time time) throws SQLException {
        wrapped.updateTime(string, time);
    }

    @Override
    public void updateTimestamp(String string, Timestamp tmstmp) throws SQLException {
        wrapped.updateTimestamp(string, tmstmp);
    }

    @Override
    public void updateAsciiStream(String string, InputStream in, int i) throws SQLException {
        wrapped.updateAsciiStream(string, in, i);
    }

    @Override
    public void updateBinaryStream(String string, InputStream in, int i) throws SQLException {
        wrapped.updateBinaryStream(string, in, i);
    }

    @Override
    public void updateCharacterStream(String string, Reader reader, int i) throws SQLException {
        wrapped.updateCharacterStream(string, reader, i);
    }

    @Override
    public void updateObject(String string, Object o, int i) throws SQLException {
        wrapped.updateObject(string, o, i);
    }

    @Override
    public void updateObject(String string, Object o) throws SQLException {
        wrapped.updateObject(string, o);
    }

    @Override
    public void insertRow() throws SQLException {
        wrapped.insertRow();
    }

    @Override
    public void updateRow() throws SQLException {
        wrapped.updateRow();
    }

    @Override
    public void deleteRow() throws SQLException {
        wrapped.deleteRow();
    }

    @Override
    public void refreshRow() throws SQLException {
        wrapped.refreshRow();
    }

    @Override
    public void cancelRowUpdates() throws SQLException {
        wrapped.cancelRowUpdates();
    }

    @Override
    public void moveToInsertRow() throws SQLException {
        wrapped.moveToInsertRow();
    }

    @Override
    public void moveToCurrentRow() throws SQLException {
        wrapped.moveToCurrentRow();
    }

    @Override
    public Statement getStatement() throws SQLException {
        return wrapped.getStatement();
    }

    @Override
    public Object getObject(int i, Map<String, Class<?>> map) throws SQLException {
        return wrapped.getObject(i, map);
    }

    @Override
    public Ref getRef(int i) throws SQLException {
        return wrapped.getRef(i);
    }

    @Override
    public Blob getBlob(int i) throws SQLException {
        return wrapped.getBlob(i);
    }

    @Override
    public Clob getClob(int i) throws SQLException {
        return wrapped.getClob(i);
    }

    @Override
    public Array getArray(int i) throws SQLException {
        return wrapped.getArray(i);
    }

    @Override
    public Object getObject(String string, Map<String, Class<?>> map) throws SQLException {
        return wrapped.getObject(string, map);
    }

    @Override
    public Ref getRef(String string) throws SQLException {
        return wrapped.getRef(string);
    }

    @Override
    public Blob getBlob(String string) throws SQLException {
        return wrapped.getBlob(string);
    }

    @Override
    public Clob getClob(String string) throws SQLException {
        return wrapped.getClob(string);
    }

    @Override
    public Array getArray(String string) throws SQLException {
        return wrapped.getArray(string);
    }

    @Override
    public Date getDate(int i, Calendar clndr) throws SQLException {
        return wrapped.getDate(i, clndr);
    }

    @Override
    public Date getDate(String string, Calendar clndr) throws SQLException {
        return wrapped.getDate(string, clndr);
    }

    @Override
    public Time getTime(int i, Calendar clndr) throws SQLException {
        return wrapped.getTime(i, clndr);
    }

    @Override
    public Time getTime(String string, Calendar clndr) throws SQLException {
        return wrapped.getTime(string, clndr);
    }

    @Override
    public Timestamp getTimestamp(int i, Calendar clndr) throws SQLException {
        return wrapped.getTimestamp(i, clndr);
    }

    @Override
    public Timestamp getTimestamp(String string, Calendar clndr) throws SQLException {
        return wrapped.getTimestamp(string, clndr);
    }

    @Override
    public URL getURL(int i) throws SQLException {
        return wrapped.getURL(i);
    }

    @Override
    public URL getURL(String string) throws SQLException {
        return wrapped.getURL(string);
    }

    @Override
    public void updateRef(int i, Ref ref) throws SQLException {
        wrapped.updateRef(i, ref);
    }

    @Override
    public void updateRef(String string, Ref ref) throws SQLException {
        wrapped.updateRef(string, ref);
    }

    @Override
    public void updateBlob(int i, Blob blob) throws SQLException {
        wrapped.updateBlob(i, blob);
    }

    @Override
    public void updateBlob(String string, Blob blob) throws SQLException {
        wrapped.updateBlob(string, blob);
    }

    @Override
    public void updateClob(int i, Clob clob) throws SQLException {
        wrapped.updateClob(i, clob);
    }

    @Override
    public void updateClob(String string, Clob clob) throws SQLException {
        wrapped.updateClob(string, clob);
    }

    @Override
    public void updateArray(int i, Array array) throws SQLException {
        wrapped.updateArray(i, array);
    }

    @Override
    public void updateArray(String string, Array array) throws SQLException {
        wrapped.updateArray(string, array);
    }

    @Override
    public RowId getRowId(int i) throws SQLException {
        return wrapped.getRowId(i);
    }

    @Override
    public RowId getRowId(String string) throws SQLException {
        return wrapped.getRowId(string);
    }

    @Override
    public void updateRowId(int i, RowId rowid) throws SQLException {
        wrapped.updateRowId(i, rowid);
    }

    @Override
    public void updateRowId(String string, RowId rowid) throws SQLException {
        wrapped.updateRowId(string, rowid);
    }

    @Override
    public int getHoldability() throws SQLException {
        return wrapped.getHoldability();
    }

    @Override
    public boolean isClosed() throws SQLException {
        return wrapped.isClosed();
    }

    @Override
    public void updateNString(int i, String string) throws SQLException {
        wrapped.updateNString(i, string);
    }

    @Override
    public void updateNString(String string, String string1) throws SQLException {
        wrapped.updateNString(string, string1);
    }

    @Override
    public void updateNClob(int i, NClob nclob) throws SQLException {
        wrapped.updateNClob(i, nclob);
    }

    @Override
    public void updateNClob(String string, NClob nclob) throws SQLException {
        wrapped.updateNClob(string, nclob);
    }

    @Override
    public NClob getNClob(int i) throws SQLException {
        return wrapped.getNClob(i);
    }

    @Override
    public NClob getNClob(String string) throws SQLException {
        return wrapped.getNClob(string);
    }

    @Override
    public SQLXML getSQLXML(int i) throws SQLException {
        return wrapped.getSQLXML(i);
    }

    @Override
    public SQLXML getSQLXML(String string) throws SQLException {
        return wrapped.getSQLXML(string);
    }

    @Override
    public void updateSQLXML(int i, SQLXML sqlxml) throws SQLException {
        wrapped.updateSQLXML(i, sqlxml);
    }

    @Override
    public void updateSQLXML(String string, SQLXML sqlxml) throws SQLException {
        wrapped.updateSQLXML(string, sqlxml);
    }

    @Override
    public String getNString(int i) throws SQLException {
        return wrapped.getNString(i);
    }

    @Override
    public String getNString(String string) throws SQLException {
        return wrapped.getNString(string);
    }

    @Override
    public Reader getNCharacterStream(int i) throws SQLException {
        return wrapped.getNCharacterStream(i);
    }

    @Override
    public Reader getNCharacterStream(String string) throws SQLException {
        return wrapped.getNCharacterStream(string);
    }

    @Override
    public void updateNCharacterStream(int i, Reader reader, long l) throws SQLException {
        wrapped.updateNCharacterStream(i, reader, l);
    }

    @Override
    public void updateNCharacterStream(String string, Reader reader, long l) throws SQLException {
        wrapped.updateNCharacterStream(string, reader, l);
    }

    @Override
    public void updateAsciiStream(int i, InputStream in, long l) throws SQLException {
        wrapped.updateAsciiStream(i, in, l);
    }

    @Override
    public void updateBinaryStream(int i, InputStream in, long l) throws SQLException {
        wrapped.updateBinaryStream(i, in, l);
    }

    @Override
    public void updateCharacterStream(int i, Reader reader, long l) throws SQLException {
        wrapped.updateCharacterStream(i, reader, l);
    }

    @Override
    public void updateAsciiStream(String string, InputStream in, long l) throws SQLException {
        wrapped.updateAsciiStream(string, in, l);
    }

    @Override
    public void updateBinaryStream(String string, InputStream in, long l) throws SQLException {
        wrapped.updateBinaryStream(string, in, l);
    }

    @Override
    public void updateCharacterStream(String string, Reader reader, long l) throws SQLException {
        wrapped.updateCharacterStream(string, reader, l);
    }

    @Override
    public void updateBlob(int i, InputStream in, long l) throws SQLException {
        wrapped.updateBlob(i, in, l);
    }

    @Override
    public void updateBlob(String string, InputStream in, long l) throws SQLException {
        wrapped.updateBlob(string, in, l);
    }

    @Override
    public void updateClob(int i, Reader reader, long l) throws SQLException {
        wrapped.updateClob(i, reader, l);
    }

    @Override
    public void updateClob(String string, Reader reader, long l) throws SQLException {
        wrapped.updateClob(string, reader, l);
    }

    @Override
    public void updateNClob(int i, Reader reader, long l) throws SQLException {
        wrapped.updateNClob(i, reader, l);
    }

    @Override
    public void updateNClob(String string, Reader reader, long l) throws SQLException {
        wrapped.updateNClob(string, reader, l);
    }

    @Override
    public void updateNCharacterStream(int i, Reader reader) throws SQLException {
        wrapped.updateNCharacterStream(i, reader);
    }

    @Override
    public void updateNCharacterStream(String string, Reader reader) throws SQLException {
        wrapped.updateNCharacterStream(string, reader);
    }

    @Override
    public void updateAsciiStream(int i, InputStream in) throws SQLException {
        wrapped.updateAsciiStream(i, in);
    }

    @Override
    public void updateBinaryStream(int i, InputStream in) throws SQLException {
        wrapped.updateBinaryStream(i, in);
    }

    @Override
    public void updateCharacterStream(int i, Reader reader) throws SQLException {
        wrapped.updateCharacterStream(i, reader);
    }

    @Override
    public void updateAsciiStream(String string, InputStream in) throws SQLException {
        wrapped.updateAsciiStream(string, in);
    }

    @Override
    public void updateBinaryStream(String string, InputStream in) throws SQLException {
        wrapped.updateBinaryStream(string, in);
    }

    @Override
    public void updateCharacterStream(String string, Reader reader) throws SQLException {
        wrapped.updateCharacterStream(string, reader);
    }

    @Override
    public void updateBlob(int i, InputStream in) throws SQLException {
        wrapped.updateBlob(i, in);
    }

    @Override
    public void updateBlob(String string, InputStream in) throws SQLException {
        wrapped.updateBlob(string, in);
    }

    @Override
    public void updateClob(int i, Reader reader) throws SQLException {
        wrapped.updateClob(i, reader);
    }

    @Override
    public void updateClob(String string, Reader reader) throws SQLException {
        wrapped.updateClob(string, reader);
    }

    @Override
    public void updateNClob(int i, Reader reader) throws SQLException {
        wrapped.updateNClob(i, reader);
    }

    @Override
    public void updateNClob(String string, Reader reader) throws SQLException {
        wrapped.updateNClob(string, reader);
    }

    @Override
    public <T> T unwrap(Class<T> type) throws SQLException {
        return wrapped.unwrap(type);
    }

    @Override
    public boolean isWrapperFor(Class<?> type) throws SQLException {
        return wrapped.isWrapperFor(type);
    }

    @Override
    public int hashCode() {
        return wrapped.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        return wrapped.equals(o);
    }

    @Override
    public String toString() {
        return wrapped.toString();
    }
}

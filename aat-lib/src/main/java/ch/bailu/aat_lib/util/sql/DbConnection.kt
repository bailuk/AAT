package ch.bailu.aat_lib.util.sql;

public interface DbConnection {

    void open(String name, int version) throws DbException;
    void execSQL(String sql, Object ... params) throws DbException;
    DbResultSet query(String sqlStatement, Object ... params) throws DbException;

    void close();
}

package ch.bailu.aat_lib.util.sql;

public interface DbConnection {

    void open(String name, int version, OnModelUpdate update) throws DbException;

    interface OnModelUpdate {
        void run(DbConnection db);
    }

    void execSQL(String sql, Object ... params) throws DbException;
    DbResultSet query(String sqlStatement, Object ... params) throws DbException;

    void close();
}

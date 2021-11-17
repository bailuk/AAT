package ch.bailu.aat_lib.util.sql;

public interface Database {

    void open(String name, int version, OnModelUpdate update) throws DbException;

    void execSQL(String sql) throws DbException;

    interface OnModelUpdate {
        void run(Database db);
    }

    ResultSet query(String table, String[] keys, String selection, String[] selectionArgs, String groupBy, String having, String orderBy) throws DbException;

    void delete(String table, String where, String[] whereArgs) throws DbException;

    void insert(String table, String[] keys, String[] values) throws DbException;


    void close();
}

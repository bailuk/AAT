package ch.bailu.aat_lib.util.sql;

public interface Database {

    void open(String name, int version, OnModelUpdate update);

    interface OnModelUpdate {
        void run();
    }

    ResultSet query(String table, String[] keys, String selection, String[] selectionArgs, String groupBy, String having, String orderBy);

    void delete(String table, String where, String[] whereArgs);

    void insert(String table, String[] keys, String[] types, String[] values);


    void close();
}

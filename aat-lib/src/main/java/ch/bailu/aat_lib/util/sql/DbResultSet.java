package ch.bailu.aat_lib.util.sql;

public interface DbResultSet {
    boolean moveToFirst();
    boolean moveToNext();
    boolean moveToPrevious();
    boolean moveToPosition(int pos);

    int getPosition();
    int getCount();

    String getString(String column);
    long getLong(String column);
    float getFloat(String column);

    boolean isClosed();
    void close();
}

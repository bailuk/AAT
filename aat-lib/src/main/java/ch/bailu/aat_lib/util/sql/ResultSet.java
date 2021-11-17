package ch.bailu.aat_lib.util.sql;

public interface ResultSet {
    boolean moveToFirst();

    boolean moveToNext();

    void close();

    int getColumnIndex(String keyFilename);

    String getString(int index);

    boolean moveToPrevious();

    boolean moveToPosition(int pos);

    int getCount();

    int getPosition();

    long getLong(int index);

    float getFloat(int index);

    boolean isClosed();
}

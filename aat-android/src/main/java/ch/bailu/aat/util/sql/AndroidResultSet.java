package ch.bailu.aat.util.sql;

import android.database.Cursor;

import ch.bailu.aat_lib.util.sql.ResultSet;

public class AndroidResultSet implements ResultSet {
    private final Cursor cursor;

    public AndroidResultSet(Cursor cursor) {
        this.cursor = cursor;
    }

    @Override
    public boolean moveToFirst() {
        return cursor.moveToFirst();
    }

    @Override
    public boolean moveToNext() {
        return cursor.moveToNext();
    }

    @Override
    public void close() {
        cursor.close();
    }

    @Override
    public int getColumnIndex(String keyFilename) {
        return cursor.getColumnIndex(keyFilename);
    }

    @Override
    public String getString(int index) {
        return cursor.getString(index);
    }

    @Override
    public boolean moveToPrevious() {
        return cursor.moveToPrevious();
    }

    @Override
    public boolean moveToPosition(int pos) {
        return cursor.moveToPosition(pos);
    }

    @Override
    public int getCount() {
        return cursor.getCount();
    }

    @Override
    public int getPosition() {
        return cursor.getPosition();
    }

    @Override
    public long getLong(int index) {
        return cursor.getLong(index);
    }

    @Override
    public float getFloat(int index) {
        return cursor.getFloat(index);
    }

    @Override
    public boolean isClosed() {
        return cursor.isClosed();
    }
}

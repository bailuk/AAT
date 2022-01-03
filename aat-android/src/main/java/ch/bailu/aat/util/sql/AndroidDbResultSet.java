package ch.bailu.aat.util.sql;

import android.database.Cursor;

import ch.bailu.aat_lib.util.sql.DbResultSet;

public class AndroidDbResultSet implements DbResultSet {
    private final Cursor cursor;

    public AndroidDbResultSet(Cursor cursor) {
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
    public String getString(String column) {
        return cursor.getString(cursor.getColumnIndex(column));
    }

    @Override
    public long getLong(String column) {
        return cursor.getLong(cursor.getColumnIndex(column));
    }

    @Override
    public float getFloat(String column) {
        return cursor.getFloat(cursor.getColumnIndex(column));
    }

    @Override
    public int getPosition() {
        return cursor.getPosition();
    }

    @Override
    public boolean isClosed() {
        return cursor.isClosed();
    }
}

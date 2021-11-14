package ch.bailu.aat.util.sql;

import android.database.Cursor;

import ch.bailu.aat_lib.util.sql.ResultSet;

public class AndroidResultSet implements ResultSet {
    private final Cursor cursor;

    public AndroidResultSet(Cursor cursor) {
        this.cursor = cursor;
    }
}

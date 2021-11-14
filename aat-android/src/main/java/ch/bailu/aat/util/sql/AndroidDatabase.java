package ch.bailu.aat.util.sql;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import ch.bailu.aat_lib.util.sql.Database;
import ch.bailu.aat_lib.util.sql.ResultSet;

public class AndroidDatabase implements Database {

    private final SQLiteDatabase database;

    public AndroidDatabase(Context context, String path, int version, OnModelUpdate update) {
        database =  new OpenHelper(context, path, null , version, update).getReadableDatabase();
    }


    @Override
    public void open(String path, int version, OnModelUpdate update) {}

    @Override
    public ResultSet query(String table, String[] keys, String selection, String[] selectionArgs, String groupBy, String having, String orderBy) {
        Cursor cursor = database.query(
                table,
                keys,
                selection,
                selectionArgs, groupBy, having,
                orderBy);
        return new AndroidResultSet(cursor);
    }

    @Override
    public void delete(String table, String where, String[] whereArgs) {
        database.delete(table, where, whereArgs);
    }

    @Override
    public void insert(String table, String[] keys, String[] types, String[] values) {

    }

    @Override
    public void close() {
        database.close();
    }

     private static class OpenHelper extends SQLiteOpenHelper {

        private final OnModelUpdate onUpdate;
        public OpenHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version, OnModelUpdate update) {
            super(context, name, factory, version);
            onUpdate=update;
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            onUpdate.run();
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            onUpdate.run();
        }
    }
}

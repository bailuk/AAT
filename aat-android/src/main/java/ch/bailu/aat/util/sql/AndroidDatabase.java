package ch.bailu.aat.util.sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import ch.bailu.aat_lib.util.sql.Database;
import ch.bailu.aat_lib.util.sql.DbException;
import ch.bailu.aat_lib.util.sql.ResultSet;

public class AndroidDatabase implements Database {

    private SQLiteDatabase database;
    private boolean needsUpdate = false;
    private final Context context;

    public AndroidDatabase(Context context) {
        this.context = context;
    }


    @Override
    public void open(String path, int version, OnModelUpdate update) {
        try {
            close();
            database = new OpenHelper(context, path, null, version).getReadableDatabase();
            if (needsUpdate) {
                update.run(this);
                needsUpdate = false;
            }
        } catch (Exception e) {
            throw new DbException(e);
        }

    }

    @Override
    public void execSQL(String sql) {
        try {
            if (isOpen()) {
                database.execSQL(sql);
            }
        } catch (Exception e) {
            throw new DbException(e);
        }
    }

    @Override
    public ResultSet query(String table, String[] keys, String selection, String[] selectionArgs, String groupBy, String having, String orderBy) {
        try {
            if (isOpen()) {
                Cursor cursor = database.query(
                        table,
                        keys,
                        selection,
                        selectionArgs, groupBy, having,
                        orderBy);
                return new AndroidResultSet(cursor);
            }
            return null;
        } catch (Exception e) {
            throw new DbException(e);
        }
    }

    @Override
    public void delete(String table, String where, String[] whereArgs) {
        try {
            if (isOpen())
                database.delete(table, where, whereArgs);
        } catch (Exception e) {
            throw new DbException(e);
        }
    }

    @Override
    public void insert(String table, String[] keys, String[] values) {
        try {
            if (isOpen() && keys.length == values.length) {
                ContentValues contentValues = new ContentValues();
                for (int i = 0; i < keys.length; i++) {
                    contentValues.put(keys[i], values[i]);
                }
                database.insert(table, null, contentValues);
            }
        } catch (Exception e) {
            throw new DbException(e);
        }
    }

    @Override
    public void close() {
        try {
            if (isOpen()) {
                database.close();
                database = null;
            }
        } catch (Exception e) {
            throw new DbException(e);
        }
    }

     private class OpenHelper extends SQLiteOpenHelper {

        public OpenHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            needsUpdate=true;
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            needsUpdate=true;
        }

         @Override
         public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
             needsUpdate=true;
         }
    }

    private boolean isOpen() {
        return database != null;
    }
}

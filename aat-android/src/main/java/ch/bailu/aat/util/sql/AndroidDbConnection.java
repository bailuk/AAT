package ch.bailu.aat.util.sql;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import ch.bailu.aat_lib.util.sql.DbConnection;
import ch.bailu.aat_lib.util.sql.DbException;
import ch.bailu.aat_lib.util.sql.DbResultSet;

public class AndroidDbConnection implements DbConnection {

    private SQLiteDatabase database;
    private boolean needsUpdate = false;
    private final Context context;

    public AndroidDbConnection(Context context) {
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
    public void execSQL(String sql, Object ... bindArgs) {
        try {
            if (isOpen()) {
                if (bindArgs.length > 0) {
                    database.execSQL(sql, toStringArgs(bindArgs));
                } else {
                    database.execSQL(sql);
                }
            }
        } catch (Exception e) {
            throw new DbException(e);
        }
    }

    @Override
    public DbResultSet query(String sql, Object ... bindArgs) {
        try {
            if (isOpen()) {
                Cursor cursor = database.rawQuery(sql, toStringArgs(bindArgs));
                return new AndroidDbResultSet(cursor);
            }
            return null;
        } catch (Exception e) {
            throw new DbException(e);
        }
    }

    private String[] toStringArgs(Object[] bindArgs) {
        String[] res = new String[bindArgs.length];

        int index = 0;
        for (Object o: bindArgs) {
            res [index++] = o.toString();
        }
        return res;
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

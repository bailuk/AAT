package ch.bailu.aat.services.directory;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteCantOpenDatabaseException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import java.io.IOException;

import ch.bailu.aat.services.ServiceContext;
import ch.bailu.util_java.foc.Foc;


public class GpxDatabase extends AbsDatabase{

    private final Context context;

    private final String[] keys;

    private SQLiteDatabase database;



    public GpxDatabase (ServiceContext sc, String path, String[] k)
            throws IOException, SQLiteCantOpenDatabaseException{

        keys = k;
        context=sc.getContext();
        database = openDatabase(path);
    }

    public GpxDatabase (ServiceContext sc, String path)
            throws IOException, SQLiteCantOpenDatabaseException {

        this(sc, path, GpxDbConstants.KEY_LIST_NEW);
    }


    private SQLiteDatabase openDatabase(String path) throws IOException, SQLiteCantOpenDatabaseException {
        return new GpxDbOpenHelper(context, path).getReadableDatabase();
    }

    @Override
    public Cursor query(String selection) {
        return database.query(
                GpxDbConstants.DB_TABLE,
                keys,
                selection,
                null, null, null,
                GpxDbConstants.KEY_START_TIME+ " DESC");
    }


    @Override
    public void close() {
        database.close();
    }

    @Override
    public void deleteEntry(Foc file) throws SQLiteException {
        final String where = GpxDbConstants.KEY_FILENAME + "=?";
        database.delete(GpxDbConstants.DB_TABLE, where,
                        new String[]{file.getName()});
    }

    public void insert(ContentValues content) {
        database.insert(GpxDbConstants.DB_TABLE, null, content);
    }
}

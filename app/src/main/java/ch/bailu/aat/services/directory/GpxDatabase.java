package ch.bailu.aat.services.directory;

import java.io.File;
import java.io.IOException;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import ch.bailu.aat.services.ServiceContext;


public class GpxDatabase extends AbsDatabase{
    private final Context context;

    private SQLiteDatabase database;

    private final File path;



    public GpxDatabase (ServiceContext sc, File p) throws IOException {
        context=sc.getContext();
        path=p;

        database = openDatabase(path);
    }


    private SQLiteDatabase openDatabase(File path) throws IOException {
        path.getParentFile().mkdirs();
        return new GpxDbOpenHelper(context, path).getReadableDatabase();    
    }

    @Override
    public Cursor query(String selection) {
        final Cursor cursor = database.query(GpxDbConstants.DB_TABLE, GpxDbConstants.KEY_LIST, selection, null, null, null, GpxDbConstants.KEY_START_TIME+ " DESC");
        return cursor;
    }


    @Override
    public void close() {
        database.close();
    }

    @Override
    public void deleteEntry(String pathName) {
        String where = GpxDbConstants.KEY_PATHNAME + "=\'" + pathName + "\'"; 
        database.delete(GpxDbConstants.DB_TABLE, where, null);
    }

}

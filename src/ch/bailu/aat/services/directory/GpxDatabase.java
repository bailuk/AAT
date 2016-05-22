package ch.bailu.aat.services.directory;

import java.io.File;
import java.io.IOException;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import ch.bailu.aat.services.cache.CacheService;


public class GpxDatabase extends AbsDatabase{
    private final Context context;

    private SQLiteDatabase database;
    private final AbsIterator iterator;

    private final File path;
    private String selection;



    public GpxDatabase (Context c, CacheService.Self loader, File p, String s) throws IOException {

        selection=s;
        context=c;
        path=p;

        database = openDatabase(path);


        iterator = new GpxIterator(openCursor(database, selection), loader);
    }


    private SQLiteDatabase openDatabase(File path) throws IOException {
        path.getParentFile().mkdirs();
        return new GpxDbOpenHelper(context, path).getReadableDatabase();    
    }

    private Cursor openCursor(SQLiteDatabase database, String selection) {
        return database.query(GpxDbConstants.DB_TABLE, GpxDbConstants.KEY_LIST, selection, null, null, null, GpxDbConstants.KEY_START_TIME+ " DESC");
    }

    public AbsIterator getIterator() {
        return iterator;
    }

    public void reopenCursor(String s) {
        selection = s;
        reopenCursor();
    }

    public void reopenCursor() {
        iterator.setCursor(openCursor(database, selection));
    }


    public void reopenDatabase(String s) throws IOException {
        selection=s;
        reopenDatabase();
    }

    public void reopenDatabase() throws IOException {
        database.close();
        database = openDatabase(path);
        iterator.setCursor(openCursor(database, selection));
    }


    @Override
    public void close() {
        iterator.close();
        database.close();
    }

    @Override
    public void deleteEntry(String pathName) {
        String where = GpxDbConstants.KEY_PATHNAME + "=\'" + pathName + "\'"; 
        database.delete(GpxDbConstants.DB_TABLE, where, null);
    }

}

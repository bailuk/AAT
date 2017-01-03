package ch.bailu.aat.providers;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.provider.OpenableColumns;

import java.io.File;
import java.io.FileNotFoundException;

import ch.bailu.aat.util.ui.AppLog;



public class GpxProvider extends ContentProvider {
    private static final UnsupportedOperationException UNSUPORTED=
            new UnsupportedOperationException("Not supported by this provider");
    

    @Override
    public ParcelFileDescriptor openFile(Uri uri, String mode) throws FileNotFoundException {
        final File file =  new File(uri.getPath());

        AppLog.d(uri, uri.toString());
        AppLog.d(this, file.toString());
        
        if (file.exists()) {
            return (ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY));
        }
        throw UNSUPORTED;
    }

    
    @Override
    public boolean onCreate() {
        
        
        return true;
    }

    @Override
    public Cursor query(Uri uri, String[] strings, String s, String[] strings2, String s2) {
        
        MatrixCursor cursor = new MatrixCursor(new String[]{OpenableColumns.DISPLAY_NAME, OpenableColumns.SIZE});
        
        
        final File file =  new File(uri.getPath());

        cursor.addRow(new Object[]{file.getName(), file.length()});
        return cursor;
    }

    @Override
    public String getType(Uri uri) {
        final File file =  new File(uri.getPath());
        return mimeTypeFromFileName(file.getName());
    }
    
    public static String mimeTypeFromFileName(String name) {
        if (name.endsWith(".gpx")) return "application/gpx+xml";
        else if (name.endsWith(".osm")) return "application/xml";
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        throw UNSUPORTED;
    }

    @Override
    public int delete(Uri uri, String s, String[] strings) {
        throw UNSUPORTED;
    }

    @Override
    public int update(Uri uri, ContentValues contentValues, String s, String[] strings) {
        throw UNSUPORTED;
    }
    
}

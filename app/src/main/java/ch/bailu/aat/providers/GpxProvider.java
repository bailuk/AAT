package ch.bailu.aat.providers;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import android.provider.OpenableColumns;
import android.support.annotation.NonNull;

import java.io.File;
import java.io.FileNotFoundException;


public class GpxProvider extends ContentProvider {
    private static final UnsupportedOperationException UNSUPORTED=
            new UnsupportedOperationException("Not supported by this provider");
    

    @Override
    public ParcelFileDescriptor openFile(@NonNull Uri uri, @NonNull String mode) throws FileNotFoundException {

        final String path = uri.getPath();

        if (path != null) {
            final File file = new File(uri.getPath());

            if (file.exists()) {
                return (ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY));
            }
        }
        throw UNSUPORTED;
    }

    
    @Override
    public boolean onCreate() {
        
        
        return true;
    }

    @Override
    public Cursor query(@NonNull Uri uri, String[] strings, String s, String[] strings2, String s2) {
        
        MatrixCursor cursor = new MatrixCursor(new String[]{OpenableColumns.DISPLAY_NAME, OpenableColumns.SIZE});
        
        
        final File file =  new File(uri.getPath());

        cursor.addRow(new Object[]{file.getName(), file.length()});
        return cursor;
    }

    @Override
    public String getType(@NonNull Uri uri) {
        final File file =  new File(uri.getPath());
        return mimeTypeFromFileName(file.getName());
    }
    
    public static String mimeTypeFromFileName(String name) {
        if (name.endsWith(".gpx")) return "application/gpx+xml";
        else if (name.endsWith(".osm")) return "application/xml";
        return null;
    }

    @Override
    public Uri insert(@NonNull Uri uri, ContentValues contentValues) {
        throw UNSUPORTED;
    }

    @Override
    public int delete(@NonNull Uri uri, String s, String[] strings) {
        throw UNSUPORTED;
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues contentValues, String s, String[] strings) {
        throw UNSUPORTED;
    }
    
}

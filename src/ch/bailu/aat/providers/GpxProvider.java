package ch.bailu.aat.providers;

import java.io.File;
import java.io.FileNotFoundException;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import ch.bailu.aat.helpers.AppLog;



public class GpxProvider extends ContentProvider {
    private static final UnsupportedOperationException UNSUPORTED=new UnsupportedOperationException("Not supported by this provider");
    public  static final String TYPE="application/gpx+xml";


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
        final File file =  new File(uri.getPath());

        AppLog.d(uri, uri.toString());
        AppLog.d(this, file.toString());

        AppLog.d(this,  s);
        AppLog.d(this, s2);
        
        
        
        throw UNSUPORTED;
    }

    @Override
    public String getType(Uri uri) {
        return TYPE;
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

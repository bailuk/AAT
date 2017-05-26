package ch.bailu.aat.util.fs;

import android.content.Context;
import android.net.Uri;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStream;

import ch.bailu.simpleio.io.Access;
import ch.bailu.simpleio.io.FileAccess;

public class UriAccess extends Access {
    private final Uri uri;
    private final Context context;

    public UriAccess(Context c, Uri u) {
        uri = u;
        context = c;
    }

    public UriAccess(Context c, File f) {
        this (c, Uri.fromFile(f));
    }

    public static Access factory(Context c, String id) {
        if (id.length()>0 && id.charAt(0) == '/') {
            return new FileAccess(new File(id));
        } else {
            return new UriAccess(c, Uri.parse(id));
        }
    }


    /*
    public static UriAccess toScopedUri(Context c, File f) {

    }
    */


    @Override
    public InputStream open_r() throws FileNotFoundException {
        return context.getContentResolver().openInputStream(uri);

    }

    @Override
    public OutputStream open_w() throws FileNotFoundException {
        return context.getContentResolver().openOutputStream(uri);
    }

    @Override
    public long lastModified() {
        return System.currentTimeMillis();
    }



}

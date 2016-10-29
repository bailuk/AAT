package ch.bailu.aat.helpers.file;

import android.content.Context;
import android.net.Uri;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.OutputStream;

public class UriAccess extends AbsAccess {
    private final Uri uri;
    private final Context context;

    public UriAccess(Context c, Uri u) {
        uri = u;
        context = c;
    }


    public UriAccess(Context c, File f) {
        this (c, Uri.fromFile(f));
    }

    @Override
    public InputStream open_r() throws FileNotFoundException {
        return context.getContentResolver().openInputStream(uri);

    }

    @Override
    public OutputStream open_w() throws FileNotFoundException {
        return context.getContentResolver().openOutputStream(uri);
    }



    @Override
    public File toFile() {
        String path = uri.getPath();
        if (path != null) {
            final File file = new File(path);
            if (file.exists()) {
                return file;
            }
        }
        return null;
    }
}

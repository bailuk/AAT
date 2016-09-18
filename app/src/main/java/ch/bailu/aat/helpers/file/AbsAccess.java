package ch.bailu.aat.helpers.file;

import android.content.Context;
import android.net.Uri;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public abstract class AbsAccess {
    public abstract InputStream open_r() throws FileNotFoundException;
    public abstract OutputStream open_w() throws FileNotFoundException;


    public static AbsAccess factory(Context c, String id) {
        if (id.length()>0 && id.charAt(0) == '/') {
            return new FileAccess(new File(id));
        } else {
            return new UriAccess(c, Uri.parse(id));
        }
    }

    public String contentToString() throws IOException {
        BufferedInputStream in = new BufferedInputStream(open_r());
        StringBuilder out = new StringBuilder();


        try {
            int c;
            while ((c = in.read()) > -1) {
                out.append((char)c);
            }

        } finally {
            in.close();

        }
        return out.toString();
    }




    public void copy(File dest) throws Exception {
        InputStream in = null;
        OutputStream out = null;

        try {

            in = open_r();
            out = new FileOutputStream(dest);
            copy(in, out);
            
        } finally {
            if (in != null) in.close();
            if (out != null) out.close();
        }
    }

    
    private static void copy(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[4096];
        int count;
        while ((count = in.read(buffer)) > 0) {
            out.write(buffer,0,count);
        }
    }


   public abstract File toFile();
    
    
    public long lastModified() {
        File file = toFile();
        if (file != null) {
            return file.lastModified();
        }
        return System.currentTimeMillis();
    }
}

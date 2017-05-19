package ch.bailu.simpleio.io;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public abstract class Access {
    public abstract InputStream open_r() throws IOException;
    public abstract OutputStream open_w() throws IOException;

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




    public void copyTo(File fout) throws Exception {
        OutputStream out = null;

        try {
            out = new FileOutputStream(fout);
            copyTo(out);

        } finally {
            if (out != null) out.close();
        }
    }

    public void copyTo(OutputStream out) throws Exception {
        InputStream in = null;

        try {
            in = open_r();
            copy(in, out);

        } finally {
            if (in != null) in.close();
        }
    }


    public static void copy(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[4096];
        int count;
        while ((count = in.read(buffer)) > 0) {
            out.write(buffer,0,count);
        }
    }


    public abstract long lastModified();
}

package ch.bailu.aat.util.fs;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


public class FileAccess extends AbsAccess {
    private final File file;

    
    public FileAccess(File f) {
        file = f;
    }



    @Override
    public InputStream open_r() throws FileNotFoundException {
        return new FileInputStream(file);

    }

    @Override
    public OutputStream open_w() throws IOException {
        return openOutput(file);
    }

    public File toFile() {
        return file;
    }

    public static OutputStream openOutput(File file) throws IOException {
        new File(file.getParent()).mkdirs();
        return new FileOutputStream(file);
    }


    public static void close(Closeable c) {
        if (c != null) {
            try {
                c.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}

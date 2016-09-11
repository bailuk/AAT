package ch.bailu.aat.helpers.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;


public class FileAccess extends AbsContentAccess {
    private final File file;

    
    public FileAccess(File f) {
        file = f;
    }


    @Override
    public InputStream open_r() throws FileNotFoundException {
        return new FileInputStream(file);

    }

    @Override
    public OutputStream open_w() throws FileNotFoundException {
        return new FileOutputStream(file);
    }




    public File toFile() {
        return file;
    }
}

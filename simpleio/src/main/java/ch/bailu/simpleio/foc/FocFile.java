package ch.bailu.simpleio.foc;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class FocFile extends Foc {

    final protected File file;


    public FocFile(String f) {
        file = new File(f);
    }

    public FocFile(File f) {
        file = f;
    }


    @Override
    public boolean rm() {
        try {
            return file.delete();
        } catch (SecurityException e) {
            return false;
        }
    }


    @Override
    public boolean mkdirs() {
        return file.mkdirs();
    }

    @Override
    public boolean mkdir() {
        return file.mkdir();
    }

    @Override
    public Foc parent() {
        File p = file.getParentFile();

        if (p != null) return new FocFile(p);
        return null;
    }

    @Override
    public boolean mv(Foc target) {
        if (target instanceof FocFile) {
            FocFile targetFile = (FocFile) target;

            return file.renameTo(targetFile.file);
        }
        return false;
    }

    @Override
    public Foc child(String name) {
        return new FocFile(new File(file, name));
    }

    @Override
    public String getName() {
        return file.getName();
    }

    @Override
    public long length() {
        return file.length();
    }


    @Override
    public String toString() {
        return file.getAbsolutePath();
    }


    @Override
    public void foreach(final Execute e) {
        file.listFiles(new FileFilter() {
            @Override
            public boolean accept(File file) {
                e.execute(new FocFile(file));
                return false;
            }
        });
    }

    @Override
    public void foreachFile(final Execute e) {
        file.listFiles(new FileFilter() {
            @Override
            public boolean accept(File file) {
                if (file.isFile())
                    e.execute(new FocFile(file));
                return false;
            }
        });
    }


    @Override
    public void foreachDir(final Execute e) {
        file.listFiles(new FileFilter() {
            @Override
            public boolean accept(File file) {
                if (file.isDirectory())
                    e.execute(new FocFile(file));
                return false;
            }
        });
    }

    @Override
    public boolean isDir() {
        return file.isDirectory();
    }

    @Override
    public boolean isFile() {
        return file.isFile();
    }

    @Override
    public boolean isReachable() {
        return file.exists();
    }

    @Override
    public boolean canRead() {
        return file.canRead();
    }

    @Override
    public boolean canWrite() {
        return file.canWrite();
    }

    @Override
    public long lastModified() {
        return file.lastModified();
    }


    @Override
    public InputStream openR() throws IOException {
        return new FileInputStream(file);
    }

    @Override
    public OutputStream openW() throws IOException {
        return new FileOutputStream(file);
    }
}

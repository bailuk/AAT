package ch.bailu.foc;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class FocFile extends Foc {

    private final File file;


    public FocFile(String f) {
        file = new File(f);
    }
    public FocFile(File f) {
        file = f;
    }


    @Override
    public boolean remove() throws SecurityException {
        return file.delete();
    }


    @Override
    public boolean mkdirs() {
        return isDir() || file.mkdirs();
    }

    @Override
    public boolean mkdir() {
        return isDir() || file.mkdir();
    }

    @Override
    public Foc parent() {
        File p = file.getParentFile();

        if (p != null) return new FocFile(p);
        return null;
    }

    @Override
    public boolean move(Foc target) throws IOException {
        if (target instanceof FocFile) {
            FocFile targetFile = (FocFile) target;

            return file.renameTo(targetFile.file) || super.move(target);
        }
        return super.move(target);
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
    public String getPath() {
        try {
            return file.getCanonicalPath();
        } catch (Exception e) {
            return file.getPath();
        }
    }



    @Override
    public void foreach(final Execute e) {
        file.listFiles(file -> {
            e.execute(new FocFile(file));
            return false;
        });
    }

    @Override
    public void foreachFile(final Execute e) {
        file.listFiles(file -> {
            if (file.isFile())
                e.execute(new FocFile(file));
            return false;
        });
    }


    @Override
    public void foreachDir(final Execute e) {
        file.listFiles(file -> {
            if (file.isDirectory())
                e.execute(new FocFile(file));
            return false;
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
    public boolean exists() {
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
        if (mkParents())
            return new FileOutputStream(file);

        throw new IOException();
    }
}

package ch.bailu.foc;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public abstract class FocAbstractName extends Foc {
    @Override
    public boolean remove() throws SecurityException {
        return false;
    }

    @Override
    public boolean mkdir() {
        return false;
    }

    @Override
    public Foc parent() {
        return null;
    }

    @Override
    public Foc child(String name) {
        return null;
    }


    @Override
    public void foreach(Execute e) {

    }

    @Override
    public void foreachFile(Execute e) {

    }

    @Override
    public void foreachDir(Execute e) {

    }

    @Override
    public boolean isDir() {
        return false;
    }

    @Override
    public boolean isFile() {
        return false;
    }

    @Override
    public boolean exists() {
        return false;
    }

    @Override
    public boolean canRead() {
        return false;
    }

    @Override
    public boolean canWrite() {
        return false;
    }

    @Override
    public long length() {
        return 0;
    }

    @Override
    public long lastModified() {
        return 0;
    }

    @Override
    public InputStream openR() throws IOException, SecurityException {
        throw new IOException(getPathName());
    }

    @Override
    public OutputStream openW() throws IOException, SecurityException {
        throw new IOException(getPathName());
    }
}

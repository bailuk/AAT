package ch.bailu.simpleio.foc;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class FocName extends Foc {

    private final String name;

    public FocName(String n) {
        name = n;
    }

    @Override
    public boolean remove() throws IOException, SecurityException {
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
    public Foc child(String child_name) {
        return new FocName(name + "/" + child_name);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getPath() {
        return name;
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
        return System.currentTimeMillis();
    }

    @Override
    public InputStream openR() throws IOException, SecurityException {
        return null;
    }

    @Override
    public OutputStream openW() throws IOException, SecurityException {
        return null;
    }
}

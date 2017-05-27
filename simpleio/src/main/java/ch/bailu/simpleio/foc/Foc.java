package ch.bailu.simpleio.foc;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public abstract class Foc {

    public abstract boolean rm();
    public boolean rmdir() {
        return isDir() && rm();
    }

    public boolean rmdirs() {
        foreachDir(new Execute() {
            @Override
            public void execute(Foc child) {
                child.rmdirs();
            }
        });
        return rmdir();
    }

    public boolean rmRecoursive() {
        foreach(new Execute() {
            @Override
            public void execute(Foc child) {
                rmRecoursive();
            }
        });
        return rm();
    }


    public abstract boolean mkdir();
    public boolean mkdirs() {
        if (isReachable() == false) {
            Foc parent = parent();
            if (parent != null && parent.mkdirs())
                return mkdir();
        }

        return isDir();
    }

    public boolean mkParents() {
        Foc parent = parent();
        return parent != null && parent.mkdirs();
    }

    public abstract Foc parent();

    public abstract boolean mv(Foc target);

    public boolean cp(Foc copy) {
        try {
            copy(copy);
        } catch (Exception e) {
            return false;
        }
        return true;
    }


    public void copy(Foc copy) throws Exception {
        OutputStream out = null;

        try {
            out = copy.openW();
            copy(out);

        } finally {
            if (out != null) out.close();
        }
    }

    private void copy(OutputStream out) throws Exception {
        InputStream in = null;

        try {
            in = openR();
            copy(in, out);

        } finally {
            if (in != null) in.close();
        }
    }


    private static void copy(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[4096];
        int count;
        while ((count = in.read(buffer)) > 0) {
            out.write(buffer,0,count);
        }
    }

    public abstract Foc child(String name);

    public abstract String getName();

    public boolean canOnlyRead() {
        return canRead() && ! canWrite();
    }

    public abstract long length();



    public abstract static class Execute {
        public abstract void execute(Foc child);
    }

    public abstract void foreach(Execute e);
    public abstract void foreachFile(Execute e);
    public abstract void foreachDir(Execute e);


    public abstract boolean isDir();
    public abstract boolean isFile();
    public abstract boolean isReachable();

    public abstract boolean canRead();
    public abstract boolean canWrite();

    public abstract long lastModified();

    public abstract InputStream openR() throws IOException;
    public abstract OutputStream openW() throws IOException;


    public static void close(Closeable toClose) {
        try {
            if (toClose != null)
                toClose.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

package ch.bailu.simpleio.foc;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public abstract class Foc {

    public abstract boolean remove() throws IOException, SecurityException;

    public boolean rm() {
        try {
            return remove();
        } catch (IOException e) {
            return false;
        }
    }

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
        if (exists()) return isDir();

        Foc parent = parent();
        return parent != null && parent.mkdirs() && mkdir();
    }

    public boolean mkParents() {
        Foc parent = parent();
        return parent != null && parent.mkdirs();
    }

    public abstract Foc parent();



    public boolean move(Foc target) throws IOException , SecurityException {
        return copy(target) && remove();
    }


    public boolean mv(Foc target) {
        try {
            return move(target);
        } catch (Exception e) {
            return false;
        }
    }




    public boolean copy(Foc copy) throws IOException, SecurityException {
        OutputStream out = null;

        try {
            out = copy.openW();
            copy(out);
            return true;

        } finally {
            if (out != null) out.close();
        }
    }

    public boolean cp(Foc copy) {
        try {
            return copy(copy);
        } catch (Exception e) {
            return false;
        }
    }



    private void copy(OutputStream out) throws IOException, SecurityException {
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

    public abstract String getPath();
    public String getPathName() {
        return getPath();
    }

    public abstract static class Execute {
        public abstract void execute(Foc child);
    }

    public abstract void foreach(Execute e);
    public abstract void foreachFile(Execute e);
    public abstract void foreachDir(Execute e);


    public abstract boolean isDir();
    public abstract boolean isFile();
    public abstract boolean exists();

    public boolean canOnlyRead() {
        return canRead() && ! canWrite();
    }
    public abstract boolean canRead();
    public abstract boolean canWrite();

    public abstract long length();
    public abstract long lastModified();

    public abstract InputStream openR() throws IOException, SecurityException;
    public abstract OutputStream openW() throws IOException, SecurityException;

    public static void close(Closeable toClose) {
        try {
            if (toClose != null)
                toClose.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @Override
    public boolean equals(Object o)  {
        return o instanceof Foc && equals(getPath(), ((Foc) o).getPath());

    }

    private static boolean equals(String s1, String s2) {
        return s1 == s2 || (s1 != null && s1.equals(s2));
    }


    @Override
    public String toString() {
        return getPath();
    }


    @Override
    public int hashCode() {
        return getPath().hashCode();
    }
}

package ch.bailu.foc;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import ch.bailu.util.Objects;

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
        final boolean[] ok = {true};

        foreachDir(new Execute() {
            @Override
            public void execute(Foc child) {
                if (!child.rmdirs()) {
                    ok[0] = false;
                }
            }
        });
        return ok[0] && rmdir();
    }

    public boolean rmRecoursive() {
        final boolean[] ok = {true};

        foreach(new Execute() {
            @Override
            public void execute(Foc child) {
                if (!child.rmRecoursive()) {
                    ok[0] = false;
                }
            }
        });
        return ok[0] && rm();
    }



    public abstract boolean mkdir();

    public boolean mkdirs() {
        return isDir() || (mkParents() && mkdir());
    }

    public boolean mkParents() {
        Foc parent = parent();
        return parent != null && parent.mkdirs();
    }

    public boolean hasParent() {
        return parent() != null;
    }

    public abstract Foc parent();



    public boolean move(Foc target) throws IOException , SecurityException {
        copy(target);
        return  remove();
    }


    public boolean mv(Foc target) {
        try {
            return move(target);
        } catch (Exception e) {
            return false;
        }
    }


    public boolean cp(Foc copy) {
        try {
            copy(copy);
            return true;

        } catch (Exception e) {
            return false;
        }
    }


    public void copy(Foc copy) throws IOException, SecurityException {
        OutputStream out = null;
        InputStream in = null;

        try {
            in = openR();
            out = copy.openW();
            copy(in, out);

        } finally {
            Foc.close(in);
            Foc.close(out);
        }
    }


    private static void copy(InputStream in, OutputStream out) throws IOException {
        byte[] buffer = new byte[4096];
        int count;
        while ((count = in.read(buffer)) > 0) {
            out.write(buffer,0,count);
        }
    }


    public Foc descendant(String path) {
        Foc descendant = this;

        String[] descendants = path.split("/");

        for (String child : descendants) {
            descendant = descendant.child(child);
        }

        return descendant;
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

    public void update() {}

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
        return o instanceof Foc && Objects.equals(getPath(), ((Foc) o).getPath());

    }


    @Override
    public String toString() {
        return getPathName();
    }


    @Override
    public int hashCode() {
        return getPath().hashCode();
    }

}

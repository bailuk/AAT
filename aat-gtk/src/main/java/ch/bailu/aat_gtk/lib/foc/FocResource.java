package ch.bailu.aat_gtk.lib.foc;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import ch.bailu.aat_lib.logger.AppLog;
import ch.bailu.foc.Foc;

public class FocResource extends Foc {
    private final String resource;

    private List<String> children = new ArrayList<>(0);
    private final Check isDirectory = new Check();
    private final Check isFile = new Check();

    public FocResource(String resourcePath) {
        this.resource = resourcePath;
        AppLog.d(this, "FocResource: " + resource);
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
        String path = new File(resource).getParent();
        if (path != null) {
            return new FocResource(path);
        }
        return null;    }

    @Override
    public Foc child(String name) {
        return new FocResource(new File(resource, name).getPath());
    }

    @Override
    public String getName() {
        return new File(resource).getName();
    }

    @Override
    public String getPath() {
        return resource;
    }

    @Override
    public void foreach(OnHaveFoc onHaveFoc) {
        checkAndLoadDirectory();
        for (String child : children) {
            onHaveFoc.run(child(child));
        }
    }

    @Override
    public void foreachFile(OnHaveFoc onHaveFoc) {
        foreach(child -> {
            if (child.isFile()) {
                onHaveFoc.run(child);
            }
        });
    }

    @Override
    public void foreachDir(OnHaveFoc onHaveFoc) {
        foreach(child -> {
            if (child.isDir()) {
                onHaveFoc.run(child);
            }
        });
    }

    @Override
    public boolean isDir() {
        checkAndLoadDirectory();
        return isDirectory.get();
    }

    @Override
    public boolean isFile() {
        checkIsFile();
        return isFile.get();
    }

    @Override
    public boolean exists() {
        return isDir() || isFile();
    }

    @Override
    public boolean canRead() {
        return exists();
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
    public InputStream openR() throws IOException {
        var result =  this.getClass().getClassLoader().getResourceAsStream(resource);
        if (result == null) {
            throw new IOException();
        }
        return result;
    }

    @Override
    public OutputStream openW() throws IOException {
        throw new IOException();
    }

    private void checkIsFile() {
        if (!isFile.isSet()) {
            InputStream toClose = null;
            try {
                toClose = openR();
                isFile.set(true);
                isDirectory.set(false);
            } catch (Exception e) {
                isFile.set(false);
            } finally {
                close(toClose);
            }
        }
    }

    private void checkAndLoadDirectory() {
        checkIsFile();
        if (!isDirectory.isSet()) {
            try {
                children = listFile();
                isDirectory.set(true);
            } catch (IOException | URISyntaxException e) {
                children = new ArrayList<>(0);
                isDirectory.set(false);
            }
        }
    }


    private List<String> listFile() throws IOException, URISyntaxException {
        ArrayList<String> result = new ArrayList<>();

        Path dirPath = Paths.get(getResourceURI(resource));
        Files.list(dirPath).forEach(p -> result.add(p.toString()));
        return result;
    }


    private URI getResourceURI(String resource) throws IOException, URISyntaxException {
        var res = FocResource.class.getResource(resource);
        if (res == null) {
            throw new IOException("Failed to get URI from '" + resource + "'");
        }
        return res.toURI();
    }


    // TODO share code with FocAsset
    private static class Check {
        private Boolean check = null;

        public void set(boolean b) {
            if (check == null) {
                check = b;
            }
        }

        public boolean get() {
            return (check != null && check);
        }

        public  boolean isSet() {
            return check != null;
        }
    }
}

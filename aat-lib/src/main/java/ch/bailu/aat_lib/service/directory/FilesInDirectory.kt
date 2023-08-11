package ch.bailu.aat_lib.service.directory;

import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

import ch.bailu.foc.Foc;

public final class FilesInDirectory {

    private final NavigableMap<String,Foc> files;


    public FilesInDirectory(Foc directory) {
        files = getFileList(directory);

    }


    private static NavigableMap<String,Foc> getFileList(Foc directory) {
        final NavigableMap<String, Foc> files = new TreeMap<>();

        directory.foreachFile(child -> files.put(child.getName(), child));

        return files;
    }


    public Foc findItem(String name) {
        return files.get(name);
    }


    public Foc pollItem(Foc file) {
        return files.remove(file.getName());
    }


    public Foc pollItem() {
        Map.Entry<String, Foc> e = files.pollLastEntry();

        if (e != null)
            return e.getValue();

        return null;
    }
}

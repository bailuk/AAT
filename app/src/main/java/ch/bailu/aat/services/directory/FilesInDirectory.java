package ch.bailu.aat.services.directory;

import java.io.IOException;
import java.util.Map;
import java.util.NavigableMap;
import java.util.TreeMap;

import ch.bailu.util_java.foc.Foc;

public class FilesInDirectory {

    final NavigableMap<String,Foc> files;


    public FilesInDirectory(Foc directory) throws IOException {
        files = getFileList(directory);

    }



    private static NavigableMap<String,Foc> getFileList(Foc directory) {
        final NavigableMap<String, Foc> files = new TreeMap<>();

        directory.foreachFile(new Foc.Execute() {
            @Override
            public void execute(Foc child) {
                files.put(child.getName(), child);
            }
        });

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

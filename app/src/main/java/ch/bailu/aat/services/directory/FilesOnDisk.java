package ch.bailu.aat.services.directory;

import java.io.IOException;
import java.util.ArrayList;

import ch.bailu.util_java.foc.Foc;

public class FilesOnDisk {
    private final ArrayList<Foc> files;
    
    public FilesOnDisk(Foc directory) throws IOException {
        files = getFileList(directory);
        
    //    removeUninterestingItems();
    }
    
/*
    private void removeUninterestingItems() {
        for (int i=files.size()-1; i>-1; i--) {
            if (files.get(i).isDir() || files.get(i).isHidden()) {
                files.remove(i);
            }
        }
    }
  */
    
    private static ArrayList<Foc> getFileList(Foc directory) {
        final ArrayList<Foc> files = new ArrayList<>(100);

        directory.foreachFile(new Foc.Execute() {
            @Override
            public void execute(Foc child) {
                files.add(child);
            }
        });

        return files;
    }
    
    
    public Foc findItem(String name) {
        for (int i=0; i<files.size(); i++) {
            if (name.equalsIgnoreCase(files.get(i).getName())) {
                return files.get(i);
            }
        }
        return null;
    }
    
    
    public Foc popItem(Foc file) {
        if (files.remove(file)) {
            return file;
        } else {
            return null;
        }
        
    }
    
    
    public Foc popItem() {
        Foc file = null;
        
        if (files.size()>0) {
            file = files.get(files.size()-1);
            files.remove(files.size()-1);
        }
        
        return file;
    }
}

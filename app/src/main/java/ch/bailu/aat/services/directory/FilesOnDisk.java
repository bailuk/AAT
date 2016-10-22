package ch.bailu.aat.services.directory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class FilesOnDisk {
    private final ArrayList<File> files;
    
    public FilesOnDisk(File directory) throws IOException {
        files = getFileList(directory);
        
        removeUninterestingItems();
    }
    
    
    private void removeUninterestingItems() {
        for (int i=files.size()-1; i>-1; i--) {
            if (files.get(i).isDirectory() || files.get(i).isHidden()) {
                files.remove(i);
            }
        }
    }
    
    
    private static ArrayList<File> getFileList(File directory) throws IOException {
        return new ArrayList<>(Arrays.asList(directory.listFiles()));
    }
    
    
    public File findItem(String name) {
        for (int i=0; i<files.size(); i++) {
            if (name.equalsIgnoreCase(files.get(i).getName())) {
                return files.get(i);
            }
        }
        return null;
    }
    
    
    public File popItem(File file) {
        if (files.remove(file)) {
            return file;
        } else {
            return null;
        }
        
    }
    
    
    public File popItem() {
        File file = null;
        
        if (files.size()>0) {
            file = files.get(files.size()-1);
            files.remove(files.size()-1);
        }
        
        return file;
    }
    
    
}

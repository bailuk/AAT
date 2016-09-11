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
        return new ArrayList<File>(Arrays.asList(directory.listFiles()));
    }
    
    
    public File findItem(String path) {
        File file = null;
        
        for (int i=0; i<files.size(); i++) {
            if (path.equalsIgnoreCase(files.get(i).getAbsolutePath())) {
                return files.get(i);
            }
        }
        return file;
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

package ch.bailu.aat.helpers;

/*
public class FileList {
    private final File directory;
    
    private File[] files;
    private int index=0;


    
    public FileList(File d) {
        directory = d;
        listFiles();
    }

    


    public boolean listFiles() {
        files = directory.listFiles();
        return validateIndex();
    }
    
    
    public boolean next() {
        index++;
        
        return validateIndex();
    }


    public boolean previous() {
        index--;
        return validateIndex();
    }


    public boolean select(int i) {
        index=i;
        return validateIndex();
    }
    

    public boolean select(String fileName) {
        for (int i=0; i<files.length; i++) { 
            if (files[i].getName().equals(fileName)) {
                index=i;
                return true;
            }
        }
        return validateIndex();
    }

    
    
    
    public void delete() {
        if (isIndexValid()) {
            files[index].delete();
            listFiles();
        }
    }


    
    public File getFile() {
        if (isIndexValid()) {
            return files[index];
        }
        return new File("/dev/null");
    }
    
    
    public String getFileName() {
        return getFile().getName();
    }






    public boolean isIndexValid() {
        return index < files.length && index > -1;
    }

    
    private boolean validateIndex() {
        if (isIndexValid()) {
            return true;
            
        } else {
            index = Math.min(files.length-1, index);
            index = Math.max(0, index);
            return false;
            
        }
        
    }


    public void rename(File target) {
        if (isIndexValid()) {
            files[index].renameTo(target);
            listFiles();
        }
    }


    public int size() {
        return files.length;
    }
}
*/
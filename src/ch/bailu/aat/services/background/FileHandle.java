package ch.bailu.aat.services.background;

public abstract class FileHandle extends ProcessHandle {
    private final String file;
    
    
    
    public FileHandle(String f) {
        file = f;
    }
    

    @Override 
    public String toString() {
        return file;
    }
}

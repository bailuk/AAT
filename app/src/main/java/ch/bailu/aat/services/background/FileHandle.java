package ch.bailu.aat.services.background;

import ch.bailu.simpleio.foc.Foc;

public abstract class FileHandle extends ProcessHandle {
    public final Foc file;
    
    
    
    public FileHandle(Foc f) {
        file = f;
    }
    

    @Override 
    public String toString() {
        return file.toString();
    }

    public Foc getFile() {
        return file;
    }

    public String getID() {
        return file.getPath();
    }
}

package ch.bailu.aat.services.directory;

import java.io.Closeable;

import ch.bailu.aat.gpx.GpxInformation;

public class Iterator implements Closeable {
    public static final Iterator NULL = new Iterator();
    
    
    public boolean moveToPrevious() {return false;}
    
    
    public boolean moveToNext() {return false;}
    
    
    public boolean moveToPosition(int pos) {return false;}

    
    public int getCount() {
        return 0;
    }


    public int getPosition() {
        return 0;
    }


    public GpxInformation getInfo() {
        return GpxInformation.NULL;
    }
    
    
    public void query(String s) {}


    @Override
    public void close() {}

}

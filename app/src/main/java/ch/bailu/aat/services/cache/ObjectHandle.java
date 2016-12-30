package ch.bailu.aat.services.cache;

import org.mapsforge.core.graphics.TileBitmap;

import java.io.File;

import ch.bailu.aat.services.ServiceContext;


public abstract class ObjectHandle implements ObjectBroadcastReceiver{
    public static final int MIN_SIZE=10;

    public static final ObjectHandle NULL = new ObjectHandle(""){
        
        @Override
        public void onDownloaded(String id, String url, ServiceContext cs) {}

        @Override
        public void onChanged(String id, ServiceContext cs) {}
    };

    private final String ID;
    
    private long accessTime=System.currentTimeMillis();
    private int lock=0;

    
    public ObjectHandle(String id) {
        ID=id;
    }

    @Override
    public String toString() {
        return ID;
    }
    public File toFile() {return new File(ID);}
    
    
    public boolean isLocked() {
        return lock > 0;
    }

    
    public void onInsert(ServiceContext sc) {}
    public void onRemove(ServiceContext sc) {}

    
    public synchronized void lock() {
        lock++;
        access();
    }

    public void free() {
        lock--;
    }
    

    public boolean isReady() {
        return true;
    }
    
    public long getSize() {
        return MIN_SIZE;
    }

    
    public synchronized void access() {
        accessTime=System.currentTimeMillis();
    }
    
    
    
    
    public static long makeOld(long time) {
        return time-30000;
    }
    
    public synchronized long getAccessTime() {
        return accessTime;
    }
    
    
    @Override
    public int hashCode() {
        return toString().hashCode();
    }



    public static class Factory {
        public ObjectHandle factory(String id, ServiceContext cs) {
            return NULL;
        }
    }


}

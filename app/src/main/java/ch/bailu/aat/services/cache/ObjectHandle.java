package ch.bailu.aat.services.cache;

import android.content.Context;

import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat.util.fs.foc.FocAndroid;
import ch.bailu.simpleio.foc.Foc;


public abstract class ObjectHandle implements ObjectBroadcastReceiver{
    public static final int MIN_SIZE=10;

    public static final ObjectHandle NULL = new ObjectHandle(""){

        @Override
        public long getSize() {
            return MIN_SIZE;
        }

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
    public Foc toFile(Context c) {return FocAndroid.factory(c, ID);}
    
    
    public boolean isLocked() {
        return lock > 0;
    }

    
    public void onInsert(ServiceContext sc) {}
    public void onRemove(ServiceContext sc) {}

    
    public synchronized void lock(ServiceContext sc) {
        lock++;
        access();
    }

    public void free() {
        lock--;
    }
    

    public boolean isReadyAndLoaded() {
        return true;
    }
    
    public abstract long getSize();
    
    public synchronized void access() {
        accessTime=System.currentTimeMillis();
    }
    
    
    
    
    public synchronized void makeOld() {
        accessTime-=30000;
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

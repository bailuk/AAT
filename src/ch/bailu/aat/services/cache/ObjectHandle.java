package ch.bailu.aat.services.cache;

import ch.bailu.aat.services.cache.CacheService.SelfOn;


public abstract class ObjectHandle implements ObjectBroadcastReceiver{
    public static final int MIN_SIZE=10;

    public static final ObjectHandle NULL = new ObjectHandle(""){
        
        @Override
        public void onDownloaded(String id, String url, SelfOn self) {}

        @Override
        public void onChanged(String id, SelfOn self) {}
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
    
    
    public boolean isLocked() {
        return lock > 0;
    }

    
    public void onInsert(SelfOn self) {}
    public void onRemove(SelfOn self) {}

    
    public synchronized void lock(SelfOn self) {
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
        public ObjectHandle factory(String id, SelfOn self) {
            return NULL;
        }
    }


}

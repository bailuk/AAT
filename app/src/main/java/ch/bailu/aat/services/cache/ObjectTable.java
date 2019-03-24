package ch.bailu.aat.services.cache;

import android.content.Intent;

import java.util.HashMap;

import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat.services.cache.ObjectHandle.Factory;
import ch.bailu.aat.util.AppIntent;
import ch.bailu.aat.util.MemSize;


public class ObjectTable  {
    private final static int INITIAL_CAPACITY = 1000;

    private long limit = MemSize.MB;
    private long totalMemorySize = 0;


    private static class Container {
        public static final Container NULL = new Container(ObjectHandle.NULL);

        public final ObjectHandle obj;
        public long size;

        public Container(ObjectHandle o) {
            obj = o;
            size = o.getSize();
        }
    }

    private final HashMap<String, Container> hashMap = new HashMap<>(INITIAL_CAPACITY);


    public synchronized ObjectHandle getHandle(String key, ServiceContext sc) {
        ObjectHandle obj=getFromCache(key);

        if (obj == null) {
            obj = ObjectHandle.NULL;
        }

        obj.lock(sc);
        return obj;
    }


    public synchronized ObjectHandle getHandle(String id, Factory factory, CacheService self) {
        ObjectHandle h =  getHandle(id, factory, self.scontext);
        trim(self);
        return h;
    }



    public synchronized ObjectHandle getHandle(String id, Factory factory, ServiceContext scontext) {
        ObjectHandle h=getFromCache(id);

        if (h == null) {
            h = factory.factory(id, scontext);

            putIntoCache(h);

            h.lock(scontext);
            h.onInsert(scontext);

        } else {
            h.lock(scontext);
        }
        return h;
    }


    private synchronized void putIntoCache(ObjectHandle obj) {

        hashMap.put(obj.toString(),new Container(obj));
        totalMemorySize += obj.getSize();

        log();
    }


    private synchronized ObjectHandle getFromCache(String key) {
        Container c = hashMap.get(key);
        if (c != null)
            return c.obj;

        return null;
    }





    private synchronized boolean updateSize(String id) {
        Container c = hashMap.get(id);

        if (c != null ) {
            long oSize = c.size;
            long nSize = c.obj.getSize();

            totalMemorySize -= oSize;
            c.size = nSize;
            totalMemorySize += nSize;

            return nSize > oSize;
        }
        return false;
    }



    public void onObjectChanged(Intent intent, CacheService self) {
        if (updateSize(toID(intent)))
            trim(self);

    }


    private String toID(Intent intent) {
        return AppIntent.getFile(intent);
    }



    public synchronized void limit(CacheService self, long l) {
        //AppLog.w(this, "Limit: " + MemSize.describe(new StringBuilder(), l).toString());
        limit = l;
        trim(self);
    }


    private void trim(CacheService self) {
        while ((totalMemorySize > limit) && removeOldest(self));
    }



    private boolean removeOldest(CacheService self) {
        return removeFromTable(findOldest(), self);
    }


    public void close(CacheService self)  {
        for (Container current : hashMap.values()) {
            current.obj.onRemove(self.scontext);
        }
        hashMap.clear();
    }


    private synchronized boolean removeFromTable(String id, CacheService self) {
        Container remove = hashMap.get(id);

        if (remove !=null && remove.obj.isLocked() == false) {
            self.broadcaster.delete(remove.obj);
            hashMap.remove(id);
            totalMemorySize -= remove.size;
            remove.obj.onRemove(self.scontext);
            return true;
        }
        return false;
    }




    private synchronized String findOldest() {
        Container oldest = new Container(ObjectHandle.NULL);
        oldest.obj.access();

        for (Container current : hashMap.values()) {
            if ((!current.obj.isLocked()) && (current.obj.getAccessTime() < oldest.obj.getAccessTime())) {
                oldest = current;
            }
        }

        return oldest.obj.getID();
    }




    public synchronized void appendStatusText(StringBuilder builder) {

        builder.append("<p>Runtime:");

        builder.append("<br>Maximum memory: ");
        MemSize.describe(builder, Runtime.getRuntime().maxMemory());
        builder.append("<br>Total memory: ");
        MemSize.describe(builder, Runtime.getRuntime().totalMemory());
        builder.append("<br>Free memory: ");
        MemSize.describe(builder, Runtime.getRuntime().freeMemory());
        builder.append("<br>Used memory: ");
        MemSize.describe(builder, Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory());
        builder.append("</p>");

        builder.append("<p> FileCache:");
        builder.append("<br>Used: ");
        MemSize.describe(builder, totalMemorySize);
        builder.append("<br>Limit: ");
        MemSize.describe(builder, limit);
        builder.append("<br>Free: ");
        MemSize.describe(builder, limit-totalMemorySize);
        builder.append("</p>");


        int locked=0,free=0;

        for (Container current : hashMap.values()) {
            if (current.obj.isLocked()) locked++;
            else free++;
        }

        builder.append("<br>LOCKED cache entries: ");
        builder.append(locked);

        builder.append("<br>FREE cache entries: ");
        builder.append(free);

        builder.append("<br>TOTAL cache entries: ");
        builder.append(hashMap.size());
        builder.append("</p>");
    }

    public void logLocked() {
/*        int locked=0;

        for (Container current : hashMap.values()) {
            if (current.obj.isLocked()){
                AppLog.d(this, current.obj.toString());
                locked++;
            }
        }
        AppLog.d(this, "Still locked: " + locked);*/
    }


    private int logCount=0;
    public void log() {
 /*       logCount++;

        if (logCount > 10) {
            logCount = 0;
        } else {
            return;
        }


        int locked=0;

        for (Container current : hashMap.values()) {

            if (current.obj.isLocked()){
                locked++;
            }
        }


        AppLog.d(this,
                        totalMemorySize/MB + "/" +
                        limit/MB + "MB - l:" + locked +
                        " f:" + (hashMap.size()-locked)
                        );*/
    }
}

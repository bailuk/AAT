package ch.bailu.aat_lib.service.cache;

import java.util.HashMap;

import ch.bailu.aat_lib.app.AppContext;
import ch.bailu.aat_lib.dispatcher.BroadcastData;
import ch.bailu.aat_lib.service.cache.Obj.Factory;
import ch.bailu.aat_lib.util.MemSize;


public final class ObjectTable  {
    private final static int INITIAL_CAPACITY = 1000;

    private long limit = MemSize.MB;
    private long totalMemorySize = 0;


    private static final class Container {
        public static final Container NULL = new Container(ObjNull.NULL);

        public final Obj obj;
        public long size;

        public Container(Obj o) {
            obj = o;
            size = o.getSize();
        }
    }

    private final HashMap<String, Container> hashMap = new HashMap<>(INITIAL_CAPACITY);


    public synchronized Obj getHandle(String key, AppContext sc) {
        Obj obj=getFromCache(key);

        if (obj == null) {
            obj = ObjNull.NULL;
        }

        obj.lock(sc);
        return obj;
    }


    public synchronized Obj getHandle(String id, Factory factory, CacheService self) {
        Obj h =  getHandle(id, factory, self.appContext);
        trim(self);
        return h;
    }



    public synchronized Obj getHandle(String id, Factory factory, AppContext appContext) {
        Obj h=getFromCache(id);

        if (h == null) {
            h = factory.factory(id, appContext);

            putIntoCache(h);

            h.lock(appContext);
            h.onInsert(appContext);

        } else {
            h.lock(appContext);
        }
        return h;
    }


    private synchronized void putIntoCache(Obj obj) {

        hashMap.put(obj.toString(),new Container(obj));
        totalMemorySize += obj.getSize();
    }


    private synchronized Obj getFromCache(String key) {
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



    public void onObjectChanged(CacheService self, String...objs) {
        if (updateSize(toID(objs)))
            trim(self);

    }


    private String toID(String... objs) {
        return BroadcastData.getFile(objs);
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
            current.obj.onRemove(self.appContext);
        }
        hashMap.clear();
    }


    private synchronized boolean removeFromTable(String id, CacheService self) {
        Container remove = hashMap.get(id);

        if (remove !=null && remove.obj.isLocked() == false) {
            self.broadcaster.delete(remove.obj);
            hashMap.remove(id);
            totalMemorySize -= remove.size;
            remove.obj.onRemove(self.appContext);
            return true;
        }
        return false;
    }




    private synchronized String findOldest() {
        Container oldest = new Container(ObjNull.NULL);
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
}

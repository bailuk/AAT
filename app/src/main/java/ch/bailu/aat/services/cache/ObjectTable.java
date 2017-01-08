package ch.bailu.aat.services.cache;

import android.content.Intent;
import android.util.SparseArray;

import ch.bailu.aat.util.AppIntent;
import ch.bailu.aat.util.ui.AppLog;
import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat.services.cache.ObjectHandle.Factory;


public class ObjectTable {
    private final static int INITIAL_CAPACITY=2000;

    private final static int MB = 1024*1024;
    private final static long MIN_SIZE=MB*3;


    private final static long MAX_SIZE=Math.max((Runtime.getRuntime().maxMemory()/5), MB*10);


    private long limit=MAX_SIZE;
    private long totalMemorySize=0;



    private static class Container {
        public static final Container NULL = new Container(ObjectHandle.NULL);

        public final ObjectHandle obj;
        public long size;

        public Container(ObjectHandle o) {
            obj= o;
            size = o.getSize();
        }

        @Override
        public int hashCode() {
            return obj.hashCode();
        }
    }

    private final SparseArray<Container> table = new SparseArray<>(INITIAL_CAPACITY);


    public synchronized ObjectHandle getHandle(String id, Factory factory, CacheService son) {
        ObjectHandle h=getFromCache(id);

        if (h == null) {
            h = factory.factory(id, son.scontext);

            putIntoCache(h);

            h.lock(son.scontext);
            h.onInsert(son.scontext);

            trim(son);

        } else {
            h.lock(son.scontext);
        }
        return h;
    }


    private void putIntoCache(ObjectHandle h) {
        table.put(h.hashCode(),new Container(h));
        totalMemorySize+=h.getSize();

        log();
    }


    private ObjectHandle getFromCache(String id) {
        Container c = table.get(id.hashCode());
        if (c != null)
            return c.obj;

        return null;
    }

    public synchronized ObjectHandle getHandle(String id, ServiceContext sc) {
        ObjectHandle h=getFromCache(id);

        if (h == null) {
            h = ObjectHandle.NULL;
        }

        h.lock(sc);
        return h;
    }




    private void updateSize(ObjectHandle handle) {
        Container c = table.get(handle.hashCode());

        if (c != null) {
            totalMemorySize -= c.size;
            c.size=handle.getSize();
            totalMemorySize += c.size;
        }
    }


    public synchronized void onObjectChanged(Intent intent, CacheService self) {
        ObjectHandle handle = getHandle(intent);
        onObjectChanged(handle, self);
    }


    public synchronized void onObjectChanged(ObjectHandle handle, CacheService self) {
        updateSize(handle);
        trim(self);
    }





    private ObjectHandle getHandle(Intent intent) {
        String string = AppIntent.getFile(intent);
        Container c = table.get(string.hashCode());

        if (c == null) {
            c=Container.NULL;
        }
        return c.obj;
    }


    public synchronized void onLowMemory(CacheService self) {
        limit = MIN_SIZE;
        trim(self);
    }


    private synchronized void trim(CacheService self) {

        while ((totalMemorySize > limit) && removeOldest(self));
    }



    private boolean removeOldest(CacheService self) {
        final Container oldest = findOldest();
        return removeFromTable(oldest, self);
    }



    private boolean removeFromTable(Container remove, CacheService self) {
        remove = table.get(remove.hashCode());

        if (remove !=null) {
            self.broadcaster.delete(remove.obj);
            table.remove(remove.hashCode());
            totalMemorySize -= remove.size;
            remove.obj.onRemove(self.scontext);
            return true;
        }
        return false;
    }


    private Container findOldest() {
        Container oldest = new Container(ObjectHandle.NULL);
        oldest.obj.access();


        for (int i=0; i<table.size(); i++) {
            final Container current = table.valueAt(i);
            if ((!current.obj.isLocked()) && (current.obj.getAccessTime() < oldest.obj.getAccessTime())) {
                oldest = current;
            }
        }
        return oldest;
    }




    public synchronized void appendStatusText(StringBuilder builder) {

        builder.append("<p>Runtime:");

        builder.append("<br>Maximum memory: ");
        builder.append(Runtime.getRuntime().maxMemory()/MB);
        builder.append(" MB");
        builder.append("<br>Total memory: ");
        builder.append(Runtime.getRuntime().totalMemory()/MB);
        builder.append(" MB");
        builder.append("<br>Free memory: ");
        builder.append(Runtime.getRuntime().freeMemory()/MB);
        builder.append(" MB");
        builder.append("<br>Used memory: ");
        builder.append((Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory())/MB);
        builder.append(" MB");
        builder.append("</p>");

        builder.append("<p> FileCache:");
        builder.append("<br>Used: ");
        builder.append(totalMemorySize/MB);
        builder.append(" MB");
        builder.append("<br>Limit: ");
        builder.append(limit/MB);
        builder.append(" MB");
        builder.append("<br>Free: ");
        builder.append((limit-totalMemorySize)/MB);
        builder.append(" MB");
        builder.append("</p>");


        int locked=0,free=0;

        for (int i=0; i<table.size(); i++) {
            ObjectHandle current;
            current = table.valueAt(i).obj;

            if (current.isLocked()) locked++;
            else free++;
        }

        builder.append("<br>LOCKED cache entries: ");
        builder.append(locked);

        builder.append("<br>FREE cache entries: ");
        builder.append(free);

        builder.append("<br>TOTAL cache entries: ");
        builder.append(table.size());
        builder.append("</p>");
    }

    public void logLocked() {
        ObjectHandle current;
        int locked=0;

        for (int i=0; i<table.size(); i++) {
            current = table.valueAt(i).obj;

            if (current.isLocked()){
                AppLog.d(this, current.toString());
                locked++;
            }
        }
        AppLog.d(this, "Still locked: " + locked);
    }

    public void log() {
        int locked=0;
        ObjectHandle current;
        for (int i=0; i<table.size(); i++) {
            current = table.valueAt(i).obj;

            if (current.isLocked()){
                locked++;
            }
        }


        AppLog.d(this,
                        "T" + totalMemorySize/MB +
                        " L " + limit/MB +
                        " t " + table.size() +
                        " l " + locked +
                        " f " + (table.size()-locked));
    }

}

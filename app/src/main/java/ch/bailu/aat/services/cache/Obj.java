package ch.bailu.aat.services.cache;

import android.support.annotation.NonNull;

import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat.util.ui.AppLog;
import ch.bailu.util_java.foc.Foc;
import ch.bailu.util_java.foc.FocName;


public abstract class Obj implements ObjBroadcastReceiver {
    public final static Obj NULL = new ObjNull();

    public static final int MIN_SIZE=100;

    private final String ID;

    private long accessTime=System.currentTimeMillis();
    private int lock=0;

    private Exception exception = null;

    public Obj(String id) {
        ID=id;
    }


    @NonNull
    @Override
    public String toString() {
        return ID;
    }


    public String getID() {
        return ID;
    }

    public Foc getFile() {
        AppLog.w(this, "Default implementation of getFile() called!");
        return new FocName(ID);
    }


    protected void setException(Exception e) {
        exception = e;
    }


    public boolean hasException() {
        return exception != null;
    }


    public Exception getException() {
        return exception;
    }


    public boolean isLocked() {
        return lock > 0;
    }


    public void onInsert(ServiceContext sc) {}
    public void onRemove(ServiceContext sc) {}


    public synchronized void lock(ServiceContext sc) {
        lock++;
        access();
    }

    public synchronized void free() {
        lock--;
    }


    public boolean isReadyAndLoaded() {
        return true;
    }


    public boolean isLoaded() {
        return isReadyAndLoaded() || hasException();
    }


    public abstract long getSize();

    public synchronized void access() {
        accessTime=System.currentTimeMillis();
    }





    public synchronized long getAccessTime() {
        return accessTime;
    }


    @Override
    public int hashCode() {
        return toString().hashCode();
    }



    public static class Factory {
        public Obj factory(String id, ServiceContext cs) {
            return ObjNull.NULL;
        }
    }

}

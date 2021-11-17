package ch.bailu.aat_lib.service.cache;

import javax.annotation.Nonnull;

import ch.bailu.aat_lib.app.AppContext;
import ch.bailu.aat_lib.logger.AppLog;
import ch.bailu.foc.Foc;
import ch.bailu.foc.FocName;


public abstract class Obj implements ObjBroadcastReceiver {

    public static final int MIN_SIZE=100;

    private final String ID;

    private long accessTime=System.currentTimeMillis();
    private int lock=0;

    private Exception exception = null;

    public Obj(String id) {
        ID=id;
    }


    @Nonnull
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


    public void onInsert(AppContext appContext) {}
    public void onRemove(AppContext appContext) {}


    public synchronized void lock(AppContext appContext) {
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
        public Obj factory(String id, AppContext appContext) {
            return ObjNull.NULL;
        }
    }
}

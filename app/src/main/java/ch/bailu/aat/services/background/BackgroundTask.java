package ch.bailu.aat.services.background;

import android.content.Context;

import ch.bailu.aat.services.ServiceContext;

public abstract class BackgroundTask implements ThreadControl {

    public static final BackgroundTask NULL = new BackgroundTask() {

        @Override
        public long bgOnProcess(ServiceContext sc) {
            return 0;
        }
    };


    private Exception exception;

    private boolean processing = true;


    @Override
    public boolean canContinue() {
        return processing;
    }



    public abstract long bgOnProcess(ServiceContext sc);


    public synchronized void stopProcessing() {
        processing =false;
    }



    public ThreadControl getThreadControl() {
        return this;
    }

    public void onInsert(Context c) {}
    public void onRemove(Context c) {}

    protected void setException(Exception e) {
        exception = e;
    }

    public  Exception getException() {
        return exception;
    }
}

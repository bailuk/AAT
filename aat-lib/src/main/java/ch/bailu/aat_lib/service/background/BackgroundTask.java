package ch.bailu.aat_lib.service.background;

import ch.bailu.aat_lib.app.AppContext;

public abstract class BackgroundTask implements ThreadControl {

    public static final BackgroundTask NULL = new BackgroundTask() {

        @Override
        public long bgOnProcess(AppContext appContext) {
            return 0;
        }
    };


    public static final BackgroundTask STOP = new BackgroundTask() {
        @Override
        public long bgOnProcess(AppContext appContext) {
            return 0;
        }

        @Override
        public boolean canContinue() {
            return false;
        }
    };


    private Exception exception;

    private boolean processing = true;


    @Override
    public boolean canContinue() {
        return processing;
    }



    public abstract long bgOnProcess(AppContext appContext);


    public synchronized void stopProcessing() {
        processing =false;
    }



    public ThreadControl getThreadControl() {
        return this;
    }

    public void onInsert() {}
    public void onRemove() {}

    protected void setException(Exception e) {
        exception = e;
    }

    public  Exception getException() {
        return exception;
    }
}

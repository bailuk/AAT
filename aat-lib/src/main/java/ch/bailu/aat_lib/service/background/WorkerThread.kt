package ch.bailu.aat_lib.service.background;

import ch.bailu.aat_lib.app.AppContext;

public class WorkerThread extends ProcessThread {

    private final AppContext appContext;


    public WorkerThread(String name, AppContext appContext, int limit) {
        super(name, limit);
        this.appContext = appContext;
    }


    public WorkerThread(String name, AppContext appContext, HandleStack q) {
        super(name, q);
        this.appContext = appContext;
    }

    @Override
    public void bgOnHandleProcessed(BackgroundTask handle, long size) {}

    @Override
    public void bgProcessHandle(final BackgroundTask handle) {
        if (handle.canContinue()) {
            appContext.getServices().insideContext(()-> {
                long size = handle.bgOnProcess(appContext);
                bgOnHandleProcessed(handle, size);
            });
        }
    }
}

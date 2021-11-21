package ch.bailu.aat.services.background;

import ch.bailu.aat_lib.app.AppContext;
import ch.bailu.aat_lib.service.InsideContext;
import ch.bailu.aat_lib.service.background.BackgroundTask;

public class WorkerThread extends ProcessThread {

    private final AppContext appContext;


    public WorkerThread(String name, AppContext sc, int limit) {
        super(name, limit);
        appContext = sc;
    }


    public WorkerThread(String name, AppContext sc, HandleStack q) {
        super(name, q);
        appContext = sc;
    }

    @Override
    public void bgOnHandleProcessed(BackgroundTask handle, long size) {}

    @Override
    public void bgProcessHandle(final BackgroundTask handle) {
        if (handle.canContinue()) {

            new InsideContext(appContext.getServices()) {
                @Override
                public void run() {
                    long size = handle.bgOnProcess(appContext);
                    bgOnHandleProcessed(handle, size);

                }
            };
        }
    }


}

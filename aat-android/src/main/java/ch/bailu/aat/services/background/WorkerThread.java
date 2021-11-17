package ch.bailu.aat.services.background;

import ch.bailu.aat_lib.service.InsideContext;
import ch.bailu.aat_lib.service.ServicesInterface;
import ch.bailu.aat_lib.service.background.BackgroundTask;

public class WorkerThread extends ProcessThread {

    private final ServicesInterface scontext;


    public WorkerThread(String name, ServicesInterface sc, int limit) {
        super(name, limit);
        scontext = sc;
    }


    public WorkerThread(String name, ServicesInterface sc, HandleStack q) {
        super(name, q);
        scontext = sc;
    }

    @Override
    public void bgOnHandleProcessed(BackgroundTask handle, long size) {}

    @Override
    public void bgProcessHandle(final BackgroundTask handle) {
        if (handle.canContinue()) {

            new InsideContext(scontext) {
                @Override
                public void run() {
                    long size = handle.bgOnProcess(scontext);
                    bgOnHandleProcessed(handle, size);

                }
            };
        }
    }


}

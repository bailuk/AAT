package ch.bailu.aat.services.background;

import ch.bailu.aat.services.InsideContext;
import ch.bailu.aat.services.ServiceContext;

public class WorkerThread extends ProcessThread {

    private final ServiceContext scontext;


    public WorkerThread(String name, ServiceContext sc, int limit) {
        super(name, sc.getContext(), limit);
        scontext = sc;
    }


    public WorkerThread(String name, ServiceContext sc, HandleStack q) {
        super(name, sc.getContext(), q);
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
                    long size;

                    //handle.bgLock();
                    size = handle.bgOnProcess(scontext);
                    //handle.bgUnlock();

                    bgOnHandleProcessed(handle, size);

                }
            };
        }
    }


}

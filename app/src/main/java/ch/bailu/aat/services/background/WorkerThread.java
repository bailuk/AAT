package ch.bailu.aat.services.background;

import ch.bailu.aat.services.InsideContext;
import ch.bailu.aat.services.ServiceContext;

public class WorkerThread extends ProcessThread {

    private final ServiceContext scontext;


    public WorkerThread(ServiceContext sc, int limit) {
        super(limit);
        scontext = sc;
    }


    public WorkerThread(ServiceContext sc, HandleStack q) {
        super(q);
        scontext = sc;
    }

    @Override
    public void bgOnHandleProcessed(ProcessHandle handle, long size) {}

    @Override
    public void bgProcessHandle(final ProcessHandle handle) {
        if (handle.canContinue()) {

            new InsideContext(scontext) {
                @Override
                public void run() {
                    long size;

                    handle.bgLock();
                    size = handle.bgOnProcess(scontext);
                    handle.bgUnlock();

                    bgOnHandleProcessed(handle, size);

                }
            };
        }
    }


}

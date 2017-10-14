package ch.bailu.aat.services.background;

import ch.bailu.aat.services.ServiceContext;

public class WorkerThread extends ProcessThread {

    private final ServiceContext scontext;


    public WorkerThread(ServiceContext sc, int limit) {
        super(limit);
        scontext = sc;
    }


    public WorkerThread(ServiceContext sc, HandleQueue q) {
        super(q);
        scontext = sc;
    }

    @Override
    public void bgOnHandleProcessed(ProcessHandle handle, long size) {}

    @Override
    public void bgOnHaveHandle(ProcessHandle handle) {
        if (handle.canContinue() && scontext.lock()) {
            long size;

            handle.bgLock();
            size = handle.bgOnProcess(scontext);
            handle.bgUnlock();

            bgOnHandleProcessed(handle, size);

            scontext.free();
        }
    }


}

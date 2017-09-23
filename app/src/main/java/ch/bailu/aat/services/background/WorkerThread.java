package ch.bailu.aat.services.background;

import ch.bailu.aat.services.ServiceContext;

public class WorkerThread extends ProcessThread {


    private final ServiceContext scontext;

    public WorkerThread(ServiceContext sc, HandleQueue q) {
        super(q);
        scontext = sc;
    }

    @Override
    public void bgOnHaveHandle(ProcessHandle handle) {
        if (handle.canContinue() && scontext.lock()) {
            handle.bgLock();
            handle.bgOnProcess(scontext);
            handle.bgUnlock();


            scontext.free();
        }
    }
}

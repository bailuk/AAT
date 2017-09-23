package ch.bailu.aat.services.background;

import ch.bailu.aat.services.ServiceContext;

public abstract class ProcessHandle implements ThreadControl {
    
    public static final ProcessHandle NULL = new ProcessHandle() {

        @Override
        public long bgOnProcess(ServiceContext sc) {
            return 0;
        }


    };


    private boolean lock=false;
    private boolean continueLoading=true;
    
    
    @Override
    public boolean canContinue() {
        return continueLoading;
    }
    
    
    public void bgLock() {
        lock=true;
    }
    
    public void bgUnlock() {
        lock=false;
    }
    
    
    public abstract long bgOnProcess(ServiceContext sc);
 
    
    public synchronized void stopLoading() {
        continueLoading=false;
    }

    
    public boolean isLocked()  {
        return lock;
    }


    public ThreadControl getThreadControl() {
        return this;
    }
}

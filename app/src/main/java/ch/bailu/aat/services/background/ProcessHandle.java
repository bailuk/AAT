package ch.bailu.aat.services.background;

import ch.bailu.aat.services.ServiceContext;

public abstract class ProcessHandle implements ThreadControl {
    
    public static final ProcessHandle NULL = new ProcessHandle() {

        @Override
        public long bgOnProcess(ServiceContext sc) {
            return 0;
        }
    };


    public static final ProcessHandle NULL_PROCESSED = new ProcessHandle() {

        @Override
        public long bgOnProcess(ServiceContext sc) {
            return 0;
        }

        public boolean canContinue() {
            return false;
        }
    };


    private boolean lock=false;
    private boolean processing =true;
    
    
    @Override
    public boolean canContinue() {
        return processing;
    }
    
    
    public void bgLock() {
        lock=true;
    }
    
    public void bgUnlock() {
        lock=false;
    }
    
    
    public abstract long bgOnProcess(ServiceContext sc);
 
    
    public synchronized void stopProcessing() {
        processing =false;
    }

    public boolean isLocked()  {
        return lock;
    }


    public ThreadControl getThreadControl() {
        return this;
    }

    public void onInsert() {}
    public void onRemove() {}
}

package ch.bailu.aat.services.background;

import android.content.Context;

public abstract class ProcessHandle implements ThreadControl {
    
    public static final ProcessHandle NULL = new ProcessHandle() {

        @Override
        public long bgOnProcess() {
            return 0;
        }

        @Override
        public void broadcast(Context context) {
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
    
    
    public abstract long bgOnProcess();
 
    
    public synchronized void stopLoading() {
        continueLoading=false;
    }

    
    public boolean isLocked()  {
        return lock;
    }


    public abstract void broadcast(Context context);


    public ThreadControl getThreadControl() {
        return this;
    }
}

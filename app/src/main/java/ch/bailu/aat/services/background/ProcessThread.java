package ch.bailu.aat.services.background;

import java.io.Closeable;

public abstract class ProcessThread extends Thread implements Closeable, ThreadControl {

    private boolean continueThread=true;
    private final HandleQueue queue;


    public ProcessThread(HandleQueue q) {
        queue = q;
        start();
    }


    public ProcessThread(int limit) {
        this(new HandleQueue(limit));
    }


    @Override
    public void run() {
        while(canContinue()) {

            try {
                queue.takeAndProcess(this);

            } catch (InterruptedException e) {
                continueThread=false;
                e.printStackTrace();
            }
        }
    }



    public abstract void bgOnHandleProcessed(ProcessHandle handle, long size);


    public abstract void bgProcessHandle(ProcessHandle handle);

    public void process(ProcessHandle handle) {
        if (canContinue()) queue.offer(handle);
    }

    
    @Override
    public void close() {
        continueThread=false;

        queue.stopCurrent();
        queue.clear();
        queue.offer(ProcessHandle.NULL);
    }


    @Override
    public boolean canContinue() {
        return continueThread;
    }
}

package ch.bailu.aat.services.background;

import java.io.Closeable;

public abstract class ProcessThread extends Thread implements Closeable, ThreadControl {

    private boolean continueThread=true;
    private final HandleQueue queue;


    private ProcessHandle current = ProcessHandle.NULL;


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
                current = queue.take();
                bgProcessHandle(current);
                current.onRemove();
                current = ProcessHandle.NULL;


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
        current.stopProcessing();
    }

    @Override
    public boolean canContinue() {
        return continueThread;
    }
}

package ch.bailu.aat.services.background;

import java.io.Closeable;
import java.util.concurrent.ArrayBlockingQueue;

public abstract class ProcessThread extends Thread implements Closeable, ThreadControl {
    
    private boolean continueThread=true;
    
    private final ArrayBlockingQueue<ProcessHandle> queue;

    private final int SIZE;
    
    private ProcessHandle currentHandle=ProcessHandle.NULL;


    public ProcessThread(int size) {
        SIZE=size;
        queue = new ArrayBlockingQueue<ProcessHandle>(SIZE, true);
        start();
    }


    @Override
    public void run() {
        while(canContinue()) {

            try {
                ProcessHandle newHandle = queue.take();

                if (newHandle.canContinue()) {
                    synchronized(currentHandle) {
                        currentHandle=newHandle;
                    }
                    bgOnHaveHandle(currentHandle);
                }

            } catch (InterruptedException e) {
                continueThread=false;
                e.printStackTrace();
            }
        }
    }


    public abstract void bgOnHaveHandle(ProcessHandle handle);
    
    public void process(ProcessHandle handle) {
        while (queue.size()>=SIZE) queue.poll();
        queue.offer(handle);
    }

    
    @Override
    public void close() {
        continueThread=false;

        queue.clear();

        synchronized(currentHandle) {
            currentHandle.stopLoading();
        }

        process(ProcessHandle.NULL);
    }


    @Override
    public boolean canContinue() {
        return continueThread;
    }
}

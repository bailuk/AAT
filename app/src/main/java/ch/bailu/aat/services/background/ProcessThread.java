package ch.bailu.aat.services.background;

import java.io.Closeable;

public abstract class ProcessThread extends Thread implements Closeable, ThreadControl {

    private boolean continueThread=true;
    private final HandleQueue queue;



    
    private final BackgroundHandle background = new BackgroundHandle();

    private class BackgroundHandle {
        private ProcessHandle handle = ProcessHandle.NULL;

        public void process(ProcessHandle h) {
            if (h.canContinue()) {
                synchronized(this) {
                    handle = h;
                }
                bgOnHaveHandle(handle);
            }
        }


        public void stop() {
            synchronized(this) {
                handle.stopLoading();
            }
        }
    }


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
                ProcessHandle newHandle = queue.take();
                background.process(newHandle);

            } catch (InterruptedException e) {
                continueThread=false;
                e.printStackTrace();
            }
        }
    }

    public abstract void bgOnHandleProcessed(ProcessHandle handle, long size);


    public abstract void bgOnHaveHandle(ProcessHandle handle);

    public void process(ProcessHandle handle) {
        queue.offer(handle);
    }

    
    @Override
    public void close() {
        continueThread=false;

        queue.clear();

        background.stop();

        process(ProcessHandle.NULL);
    }


    @Override
    public boolean canContinue() {
        return continueThread;
    }
}

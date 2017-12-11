package ch.bailu.aat.services.background;

import java.io.Closeable;

public abstract class ProcessThread extends Thread implements Closeable, ThreadControl {

    private boolean continueThread=true;
    private final HandleStack queue;


    private BackgroundTask current = BackgroundTask.NULL;


    public ProcessThread(HandleStack q) {
        queue = q;
        start();
    }


    public ProcessThread(int limit) {
        this(new HandleStack(limit));
    }


    @Override
    public void run() {
        while(canContinue()) {

            try {
                current = queue.take();
                bgProcessHandle(current);
                current.onRemove();
                current = BackgroundTask.NULL;


            } catch (InterruptedException e) {
                continueThread=false;
                e.printStackTrace();
            }
        }
    }



    public abstract void bgOnHandleProcessed(BackgroundTask handle, long size);


    public abstract void bgProcessHandle(BackgroundTask handle);

    public void process(BackgroundTask handle) {
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

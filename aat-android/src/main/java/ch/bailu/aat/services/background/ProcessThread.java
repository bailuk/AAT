package ch.bailu.aat.services.background;

import java.io.Closeable;

import ch.bailu.aat_lib.service.background.BackgroundTask;
import ch.bailu.aat_lib.service.background.ThreadControl;

public abstract class ProcessThread extends Thread implements Closeable, ThreadControl {

    private boolean continueThread=true;
    private final HandleStack queue;

    private BackgroundTask current = BackgroundTask.NULL;


    public ProcessThread(String name, HandleStack q) {
        super(name);
        queue = q;
        start();
    }


    public ProcessThread(String name, int limit) {
        this(name, new HandleStack(limit));
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
        queue.offer(BackgroundTask.STOP);
    }


    @Override
    public boolean canContinue() {
        return continueThread;
    }
}

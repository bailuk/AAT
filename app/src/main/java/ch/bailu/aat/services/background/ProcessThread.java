package ch.bailu.aat.services.background;

import android.content.Context;

import java.io.Closeable;

public abstract class ProcessThread extends Thread implements Closeable, ThreadControl {

    private boolean continueThread=true;
    private final HandleStack queue;


    private BackgroundTask current = BackgroundTask.NULL;


    private final Context context;

    public ProcessThread(Context c, HandleStack q) {
        queue = q;
        context = c;
        start();
    }


    public ProcessThread(Context c, int limit) {
        this(c, new HandleStack(c, limit));
    }


    @Override
    public void run() {
        while(canContinue()) {

            try {
                current = queue.take();
                bgProcessHandle(current);
                current.onRemove(context);
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

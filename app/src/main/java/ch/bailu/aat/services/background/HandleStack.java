package ch.bailu.aat.services.background;

import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

public class HandleStack {
    private final static int DEFAULT_LIMIT = 5000;

    private final BlockingDeque<BackgroundTask> queue;
    private final int limit;

    public HandleStack() {
        this(DEFAULT_LIMIT);
    }


    public HandleStack(int l) {
        limit = l;
        queue = new LinkedBlockingDeque<>(limit);
    }



    public BackgroundTask take() throws InterruptedException {
        return queue.takeFirst();
    }


    public void offer(BackgroundTask handle) {
        while (queue.size() >= limit) remove();
        insert(handle);
    }


    public void close(int i) {
        while(remove() != null);
        while(i>0) {
            queue.offerFirst(BackgroundTask.NULL);
            i--;
        }
    }


    private void insert(BackgroundTask handle) {
        handle.onInsert();
        queue.offerFirst(handle);
    }


    private BackgroundTask remove() {
        BackgroundTask handle = queue.pollLast();

        if (handle != null) {
            handle.onRemove();
        }
        return handle;
    }
}

package ch.bailu.aat.services.background;

import java.util.concurrent.ArrayBlockingQueue;

public class HandleQueue {
    private final static int DEFAULT_LIMIT = 5000;

    private final ArrayBlockingQueue<ProcessHandle> queue;
    private final int limit;


    public HandleQueue() {
        this(DEFAULT_LIMIT);
    }


    public HandleQueue(int l) {
        limit = l;
        queue = new ArrayBlockingQueue<>(limit, true);
    }


    public ProcessHandle take() throws InterruptedException {
        return queue.take();
    }

    public void offer(ProcessHandle handle) {
        while (queue.size() >= limit) queue.poll();
        queue.offer(handle);
    }


    public void clear() {
        queue.clear();
    }
}

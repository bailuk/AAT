package ch.bailu.aat.services.background;

import java.util.concurrent.ArrayBlockingQueue;

public class HandleQueue {
    private final static int DEFAULT_LIMIT = 5000;

    private final ArrayBlockingQueue<ProcessHandle> queue;
    private final int limit;

    private ProcessHandle current = ProcessHandle.NULL;

    public HandleQueue() {
        this(DEFAULT_LIMIT);
    }


    public HandleQueue(int l) {
        limit = l;
        queue = new ArrayBlockingQueue<>(limit, true);
    }



    public void takeAndProcess(ProcessThread t) throws InterruptedException {
        current = queue.take();

        t.bgProcessHandle(current);

        current.onRemove();
        current = ProcessHandle.NULL;
    }


    public void offer(ProcessHandle handle) {
        while (queue.size() >= limit) remove();
        insert(handle);
    }


    public void clear() {
        while(remove() != null);
    }


    private void insert(ProcessHandle handle) {
        handle.onInsert();
        queue.offer(handle);
    }


    private ProcessHandle remove() {
        ProcessHandle handle = queue.poll();

        if (handle != null) {
            handle.onRemove();
        }
        return handle;
    }


    public void stopCurrent() {
        current.stopProcessing();
    }
}

package ch.bailu.aat.services;

/*
public abstract class FileHandleThread extends Thread implements CleanUp, ThreadControl {
    private boolean continueThread=true;
    private final ArrayBlockingQueue<FileHandle> queue = 
            new ArrayBlockingQueue<FileHandle>(200, true);

    private FileHandle currentHandle=FileHandle.NULL_HANDLE;

    public FileHandleThread() {
        start();
    }


    @Override
    public void run() {
        while(canContinue()) {

            try {
                FileHandle newHandle = queue.take();

                if (newHandle.isLocked()) {
                    synchronized(currentHandle) {
                        currentHandle=newHandle;
                    }
                    onHaveHandle(currentHandle);
                }

            } catch (InterruptedException e) {
                continueThread=false;
                e.printStackTrace();
            }
        }
    }


    public abstract void onHaveHandle(FileHandle handle);
    
    public void process(FileHandle handle) {
        
        synchronized(currentHandle) {
            currentHandle.stopLoading();
        }
        queue.offer(handle);
    }


    @Override
    public void cleanUp() {
        continueThread=false;

        queue.clear();

        synchronized(currentHandle) {
            while (currentHandle.isLocked()) currentHandle.free();
            currentHandle.stopLoading();
        }

        process(FileHandle.NULL_HANDLE);
    }


    @Override
    public boolean canContinue() {
        return continueThread;
    }
}
*/

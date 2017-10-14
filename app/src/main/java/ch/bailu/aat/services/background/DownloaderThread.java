package ch.bailu.aat.services.background;

import ch.bailu.aat.services.ServiceContext;

public class DownloaderThread  extends WorkerThread {
    private final static int DOWNLOAD_QUEUE_SIZE=100;
    private final String server;
    private final DownloadStatistics statistics = new DownloadStatistics();

    public DownloaderThread(ServiceContext sc, String s) {
        super(sc, DOWNLOAD_QUEUE_SIZE);
        server = s;
    }


    @Override
    public void bgOnHandleProcessed(ProcessHandle handle, long size) {
        if (size > 0) {
            statistics.success(size);
        } else {
            statistics.failure();
        }
    }


    @Override
    public void bgProcessHandle(ProcessHandle handle) {
        if (statistics.isReady()) {
            super.bgProcessHandle(handle);
        }
    }


    public void appendStatusText(StringBuilder builder) {
        statistics.appendStatusText(builder,server);
    }
}

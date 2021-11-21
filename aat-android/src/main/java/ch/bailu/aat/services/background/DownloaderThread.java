package ch.bailu.aat.services.background;

import ch.bailu.aat_lib.app.AppContext;
import ch.bailu.aat_lib.service.background.BackgroundTask;

public final class DownloaderThread  extends WorkerThread {
    private final static int DOWNLOAD_QUEUE_SIZE=100;
    private final String server;
    private final DownloadStatistics statistics = new DownloadStatistics();

    private static long totalSize = 0;



    public DownloaderThread(AppContext sc, String s) {
        super("DT_" + s, sc, DOWNLOAD_QUEUE_SIZE);
        server = s;
    }


    @Override
    public void bgOnHandleProcessed(BackgroundTask handle, long size) {
        totalSize += size;

        if (size > 0) {
            statistics.success(size);
        } else {
            statistics.failure();
        }
    }


    @Override
    public void bgProcessHandle(BackgroundTask handle) {
        if (statistics.isReady()) {
            super.bgProcessHandle(handle);
        }
    }


    public void appendStatusText(StringBuilder builder) {
        statistics.appendStatusText(builder,server);
    }


    public static long getTotalSize() {
        return totalSize;
    }
}

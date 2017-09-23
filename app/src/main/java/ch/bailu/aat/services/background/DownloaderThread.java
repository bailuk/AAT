package ch.bailu.aat.services.background;

import ch.bailu.aat.services.ServiceContext;

public class DownloaderThread  extends ProcessThread {
    private final static int DOWNLOAD_QUEUE_SIZE=100;
    private final String server;
    private final ServiceContext scontext;
    private final DownloadStatistics statistics = new DownloadStatistics();

    public DownloaderThread(ServiceContext sc, String s) {
        super(DOWNLOAD_QUEUE_SIZE);
        scontext = sc;
        server = s;
    }

    
    
    @Override
    public void bgOnHaveHandle(ProcessHandle handle) {
        if (statistics.isReady()) {
            handle.bgLock();
            final long size = handle.bgOnProcess(scontext);
            handle.bgUnlock();
            
            if (size > 0) {
                statistics.success(size);
            } else {
                statistics.failure();
            }
        }
    }


    public void appendStatusText(StringBuilder builder) {
        statistics.appendStatusText(builder,server);
    }
}

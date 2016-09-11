package ch.bailu.aat.services.background;

import android.content.Context;

public class DownloaderThread  extends ProcessThread {
    private final static int DOWNLOAD_QUEUE_SIZE=10;
    private final String server;
    private final Context context;
    private final DownloadStatistics statistics = new DownloadStatistics();

    
    public DownloaderThread(Context c, String s) {
        super(DOWNLOAD_QUEUE_SIZE);
        context = c;
        server = s;
    }

    
    
    @Override
    public void bgOnHaveHandle(ProcessHandle handle) {
        
        if (statistics.isReady()) {
            handle.bgLock();
            final long size = handle.bgOnProcess();
            handle.bgUnlock();
            
            if (size > 0) {
                statistics.success(size);
                handle.broadcast(context);
            }
            
            
        }
    }


    public void appendStatusText(StringBuilder builder) {
        statistics.appendStatusText(builder,server);
    }
}

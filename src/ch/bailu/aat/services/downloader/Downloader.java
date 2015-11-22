package ch.bailu.aat.services.downloader;

/*
public class Downloader extends FileHandleThread {

    private final String server;
    private final Context context;
    private final DownloadStatistics statistics = new DownloadStatistics();

    
    public Downloader(Context c, String s) {
        context = c;
        server = s;
    }

    
    
    @Override
    public void onHaveHandle(FileHandle h) {
        DownloadableFileHandle handle = (DownloadableFileHandle) h;
        
        
        if (statistics.isReady()) {
            try {
                long size;

                if ((size = handle.download()) > 0) {
                    statistics.success(size);

                    AppBroadcaster.broadcast(context, 
                            AppBroadcaster.FILE_DOWNLOADED, handle.getFile());
                }

            } catch (IOException e) {
                statistics.failure();
                AppLog.i(context, handle.getFile().getName() + ": " + e.getMessage());

            }
        }
    }


    public void appendStatusText(StringBuilder builder) {
        statistics.appendStatusText(builder,server);
    }
}
*/

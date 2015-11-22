package ch.bailu.aat.services.downloader;

/*
public class xDownloaderService extends AbsService {

    private final SparseArray<Downloader> downloaders = new SparseArray<Downloader>();
// FIXME: needs rewrite    //private MapFeaturesDownloader mapFeaturesDownloader;
    private BroadcastReceiver onFileDownloaded = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            AppLog.i(DownloaderService.this, AppBroadcaster.getExtraFile(intent).getAbsolutePath());
        }
    };


    @Override
    public void onCreate() {
        super.onCreate();

        //mapFeaturesDownloader = new MapFeaturesDownloader(this);
        AppBroadcaster.register(this, onFileDownloaded, AppBroadcaster.FILE_DOWNLOADED);
    }

    
    
    
    public void download(DownloadableFileHandle handle) {
        URL url = handle.getURL();
        
        if (url != null) {
            String host = url.getHost();
            Downloader downloader = downloaders.get(host.hashCode());
        
            if (downloader == null) {
                downloader = new Downloader(this, host);
                downloaders.put(host.hashCode(), downloader);
            }
            downloader.process(handle);
        }
    }
    
    
    public DownloadableFileHandle download(URL source, File target) {
        DownloadableFileHandle handle = new DownloadableFileHandle(source, target);
        handle.lock();
        download(handle);
        return handle;
    }

    
    
    @Override
    public void onDestroy() {
        unregisterReceiver(onFileDownloaded);

        //mapFeaturesDownloader.cleanUp();
        
        for (int i=0; i<downloaders.size(); i++)
            downloaders.valueAt(i).cleanUp();
        
        super.onDestroy();
    }


    public void appendStatusText(StringBuilder builder) {
        super.appendStatusText(builder);
        
        for (int i=0; i<downloaders.size(); i++)
            downloaders.valueAt(i).appendStatusText(builder);
    }

    @Override
    public void onServicesUp() {}

    
    public void downloadMapFeatures() {
        //mapFeaturesDownloader.download();
    }
}
*/

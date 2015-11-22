package ch.bailu.aat.services.background;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.SparseArray;
import ch.bailu.aat.helpers.AppBroadcaster;
import ch.bailu.aat.helpers.AppLog;
import ch.bailu.aat.services.AbsService;

public class BackgroundService extends AbsService{
    private final static int PROCESS_QUEUE_SIZE=500;
    
    private final SparseArray<DownloaderThread> downloaders = new SparseArray<DownloaderThread>();
    private final SparseArray<LoaderThread> loaders = new SparseArray<LoaderThread>();
    private ProcessThread process;
    
    
   private MapFeaturesDownloader mapFeaturesDownloader;
    
    private BroadcastReceiver onFileDownloaded = new BroadcastReceiver() {
         @Override
         public void onReceive(Context context, Intent intent) {
             AppLog.i(BackgroundService.this, AppBroadcaster.getFile(intent));
         }
     };


     @Override
     public void onCreate() {
         
         super.onCreate();

         
         mapFeaturesDownloader = new MapFeaturesDownloader(this);
         AppBroadcaster.register(this, onFileDownloaded, AppBroadcaster.FILE_CHANGED_ONDISK);
         
         
         process =new ProcessThread(PROCESS_QUEUE_SIZE) {

            @Override
            public void bgOnHaveHandle(ProcessHandle handle) {
                if (handle.canContinue()) {
                    handle.bgLock();
                    handle.bgOnProcess();
                    handle.bgUnlock();
                    handle.broadcast(BackgroundService.this);

                }
            }
         };
     }

     
     
     
     public void process(ProcessHandle handle) {
         process.process(handle);
     }
     
     
     public void download(ProcessHandle handle) {
        URL url;
        try {
            url = new URL(handle.toString());
        } catch (MalformedURLException e) {
            url = null;
        }
         
         if (url != null) {
             String host = url.getHost();
             DownloaderThread downloader = downloaders.get(host.hashCode());
         
             if (downloader == null) {
                 downloader = new DownloaderThread(this, host);
                 downloaders.put(host.hashCode(), downloader);
             }
             downloader.process(handle);
         }
     }
     

     public void load(ProcessHandle handle) {
         final String base = getBaseDirectory(handle.toString());
         
         LoaderThread loader = loaders.get(base.hashCode());
     
         if (loader == null) {
             loader = new LoaderThread(this, base);
             loaders.put(base.hashCode(), loader);
         }
         loader.process(handle);
     }
     
     private String getBaseDirectory(String id) {
         File p1 = new File (id);
         File r = p1;
         
         int c=0;
         final int t=3;
         
         while (p1!=null) {
             p1=p1.getParentFile();
             
             if (c<t) {
                 c++;
                     
             } else {
                 r=r.getParentFile();
                 
             }
             
         }
         
        return r.getAbsolutePath();
    }




    /*
     public DownloadableFileHandle download(URL source, File target) {
         DownloadableFileHandle handle = new DownloadableFileHandle(source, target);
         handle.lock();
         download(handle);
         return handle;
     }*/

     
     
     @Override
     public void onDestroy() {
         unregisterReceiver(onFileDownloaded);

         mapFeaturesDownloader.cleanUp();

         for (int i=0; i<loaders.size(); i++)
             loaders.valueAt(i).cleanUp();
         loaders.clear();
         
         for (int i=0; i<downloaders.size(); i++)
             downloaders.valueAt(i).cleanUp();
         downloaders.clear();
         
         process.cleanUp();
         process=null;
         
         super.onDestroy();
     }


     public void appendStatusText(StringBuilder builder) {
         super.appendStatusText(builder);

         for (int i=0; i<loaders.size(); i++)
             loaders.valueAt(i).appendStatusText(builder);
         
         for (int i=0; i<downloaders.size(); i++)
             downloaders.valueAt(i).appendStatusText(builder);
     }

     @Override
     public void onServicesUp() {}

     
     public void downloadMapFeatures() {
         mapFeaturesDownloader.download();
     }
}

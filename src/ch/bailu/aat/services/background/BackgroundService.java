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
    

    public static final Self NULL_SELF=new Self();
    
    private Self self=NULL_SELF;
    
    private BroadcastReceiver onFileDownloaded = new BroadcastReceiver() {
         @Override
         public void onReceive(Context context, Intent intent) {
             AppLog.i(BackgroundService.this, AppBroadcaster.getFile(intent));
         }
     };

     
     public static class Self {
         public void process(ProcessHandle handle) {}
         public void download(ProcessHandle handle) {}
         public void load(ProcessHandle handle) {}
         public void downloadMapFeatures() {}
     }
     
     
     public class SelfOn extends Self {
         @Override
         public void process(ProcessHandle handle) {
             process.process(handle);
         }
         
         @Override
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
                      downloader = new DownloaderThread(BackgroundService.this, host);
                      downloaders.put(host.hashCode(), downloader);
                  }
                  downloader.process(handle);
              }
         }
         
         @Override
         public void load(ProcessHandle handle) {
             final String base = getBaseDirectory(handle.toString());
             
             LoaderThread loader = loaders.get(base.hashCode());
         
             if (loader == null) {
                 loader = new LoaderThread(BackgroundService.this, base);
                 loaders.put(base.hashCode(), loader);
             }
             loader.process(handle);
         }
         
         @Override
         public void downloadMapFeatures() {
             mapFeaturesDownloader.download();
         }

     }
     

     public Self getSelf() {
         return self;
     }
     
     
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
         
         self = new SelfOn();
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
         self = NULL_SELF;
         
         unregisterReceiver(onFileDownloaded);

         mapFeaturesDownloader.close();

         for (int i=0; i<loaders.size(); i++)
             loaders.valueAt(i).close();
         loaders.clear();
         
         for (int i=0; i<downloaders.size(); i++)
             downloaders.valueAt(i).close();
         downloaders.clear();
         
         process.close();
         process=null;
         
         super.onDestroy();
     }


     @Override
     public void appendStatusText(StringBuilder builder) {
         super.appendStatusText(builder);

         for (int i=0; i<loaders.size(); i++)
             loaders.valueAt(i).appendStatusText(builder);
         
         for (int i=0; i<downloaders.size(); i++)
             downloaders.valueAt(i).appendStatusText(builder);
     }

     @Override
     public void onServicesUp() {}


}

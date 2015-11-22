package ch.bailu.aat.services.background;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import ch.bailu.aat.helpers.AppBroadcaster;
import ch.bailu.aat.helpers.AppDirectory;
import ch.bailu.aat.helpers.AppLog;
import ch.bailu.aat.helpers.CleanUp;
import ch.bailu.aat.osm_features.MapFeaturesPreparser;

public class MapFeaturesDownloader implements CleanUp {
    private final static String SOURCE_URL = "http://wiki.openstreetmap.org/wiki/Map_Features";

    private final BackgroundService downloader;
    

    private ArrayList<DownloadHandle> pendingImages=new ArrayList<DownloadHandle>();
    
    
    public MapFeaturesDownloader(BackgroundService d) {
        downloader = d;
        AppBroadcaster.register(downloader, onFileDownloaded, AppBroadcaster.FILE_CHANGED_ONDISK);
    }


    
    private State state = new StateIdle();

    
    public void download() {
        setState(new StateDownloadIndex());
    }


    private final BroadcastReceiver  onFileDownloaded = new BroadcastReceiver () {
        @Override
        public void onReceive(Context context, Intent intent) {
            state.ping(AppBroadcaster.getUrl(intent));
        }
    };

    
    @Override
    public void cleanUp() {
        downloader.unregisterReceiver(onFileDownloaded);
        terminate();
    }


    private void terminate() {
        setState(new StateIdle());
    }


    private void setState(State s) {
        state = s;
        state.start();
    }


    /////////////////////////////////////////////////////////////////////
    private abstract class State {
        public abstract void start();
        public abstract void ping(String file);
    }
    
    
    
    //////////////////////////////////////////////
    private class StateIdle extends State {

        @Override
        public void start() {}

        @Override
        public void ping(String f) {}
    }
    

    //////////////////////////////////////////////////
    private class StateDownloadIndex extends State {
        private DownloadHandle request;
        
        @Override
        public void start() {

            try {
                final File file = AppDirectory.getMapFeatureIndex(downloader);
                request = new DownloadHandle(SOURCE_URL, file);
                downloader.download(request);

            } catch (Exception e) {
                AppLog.e(downloader, e);
                terminate();
            }

        }

        @Override
        public void ping(String url) {
            if (url.equals(request.toString())) {
                try {
                    pendingImages = new MapFeaturesPreparser(downloader).getImageList();
                    setState(new StateDownloadPendingImage());
                } catch (IOException e) {
                    AppLog.e(downloader, e);
                    terminate();
                }

            }
        }
        
    }
    
    

    //////////////////////////////////////////////////////////////
    private class StateDownloadPendingImage extends State {

        DownloadHandle request;
        
        @Override
        public void start() {
            if (pendingImages.size()==0) {
                terminate();
            } else { 
                request = pendingImages.get(pendingImages.size()-1);
                pendingImages.remove(pendingImages.size()-1);
                
                File target = request.getFile();
                if (target.exists()==false) {
                    downloader.download(request);
                }
            }
        }

        @Override
        public void ping(String url) {
            if (url.equals(request.toString())) {
                setState(new StateDownloadPendingImage());
            }
            
        }
        
    }
}

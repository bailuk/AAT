package ch.bailu.aat.services.background;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import ch.bailu.aat.helpers.AppBroadcaster;
import ch.bailu.aat.helpers.AppDirectory;
import ch.bailu.aat.helpers.AppLog;
import ch.bailu.aat.osm_features.MapFeaturesPreparser;
import ch.bailu.aat.services.ServiceContext;

public class MapFeaturesDownloader implements Closeable {
    private final static String SOURCE_URL = "http://wiki.openstreetmap.org/wiki/Map_Features";

    private final ServiceContext scontext;
    

    private ArrayList<DownloadHandle> pendingImages=new ArrayList<DownloadHandle>();
    
    
    public MapFeaturesDownloader(ServiceContext sc) {
        scontext=sc;
        AppBroadcaster.register(sc.getContext(), onFileDownloaded, AppBroadcaster.FILE_CHANGED_ONDISK);
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
    public void close() {
        scontext.getContext().unregisterReceiver(onFileDownloaded);
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
                final File file = AppDirectory.getMapFeatureIndex(scontext.getContext());
                request = new DownloadHandle(SOURCE_URL, file);
                scontext.getBackgroundService().download(request);

            } catch (Exception e) {
                AppLog.e(this, e);
                terminate();
            }

        }

        @Override
        public void ping(String url) {
            if (url.equals(request.toString())) {
                try {
                    pendingImages = new MapFeaturesPreparser(scontext.getContext()).getImageList();
                    setState(new StateDownloadPendingImage());
                } catch (IOException e) {
                    AppLog.e(this, e);
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
                    scontext.getBackgroundService().download(request);
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

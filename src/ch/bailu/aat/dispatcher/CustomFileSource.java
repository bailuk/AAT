package ch.bailu.aat.dispatcher;

import java.io.File;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import ch.bailu.aat.gpx.GpxFileWrapper;
import ch.bailu.aat.helpers.AppBroadcaster;
import ch.bailu.aat.services.cache.CacheService;
import ch.bailu.aat.services.cache.GpxObject;
import ch.bailu.aat.services.cache.GpxObjectStatic;

public class CustomFileSource extends ContentSource {
    private final CacheService cacheService;
    private final GpxObject    gpxObject;


    private final BroadcastReceiver  onChangedInCache = new BroadcastReceiver () {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (AppBroadcaster.hasFile(intent, gpxObject.toString())) {
                forceUpdate();
            }
        }
    };


    public CustomFileSource(CacheService s, String fileID) {
        cacheService = s;

        gpxObject = (GpxObject) cacheService.getObject(fileID, new GpxObjectStatic.Factory());
        AppBroadcaster.register(cacheService, onChangedInCache, AppBroadcaster.FILE_CHANGED_INCACHE);
    }   

    @Override
    public void cleanUp() {
        cacheService.unregisterReceiver(onChangedInCache);
        gpxObject.free();
    }


    @Override
    public void forceUpdate() {
        updateGpxContent(new GpxFileWrapper(new File(gpxObject.toString()), gpxObject.getGpxList()));
    }
}

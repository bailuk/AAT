package ch.bailu.aat.dispatcher;

import java.io.File;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import ch.bailu.aat.gpx.GpxFileWrapper;
import ch.bailu.aat.helpers.AppBroadcaster;
import ch.bailu.aat.services.MultiServiceLink.ServiceContext;
import ch.bailu.aat.services.cache.GpxObject;
import ch.bailu.aat.services.cache.GpxObjectStatic;

public class CustomFileSource extends ContentSource {
    private final ServiceContext scontext;
    private final GpxObject    gpxObject;


    private final BroadcastReceiver  onChangedInCache = new BroadcastReceiver () {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (AppBroadcaster.hasFile(intent, gpxObject.toString())) {
                forceUpdate();
            }
        }
    };


    public CustomFileSource(ServiceContext sc, String fileID) {
        scontext=sc;

        gpxObject = (GpxObject) scontext.getCacheService().getObject(fileID, new GpxObjectStatic.Factory());
        AppBroadcaster.register(sc.getContext(), onChangedInCache, AppBroadcaster.FILE_CHANGED_INCACHE);
    }   

    @Override
    public void close() {
        scontext.getContext().unregisterReceiver(onChangedInCache);
        gpxObject.free();
    }


    @Override
    public void forceUpdate() {
        updateGpxContent(new GpxFileWrapper(new File(gpxObject.toString()), gpxObject.getGpxList()));
    }
}

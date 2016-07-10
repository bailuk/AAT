package ch.bailu.aat.dispatcher;

import java.io.File;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import ch.bailu.aat.gpx.GpxFileWrapper;
import ch.bailu.aat.helpers.AppBroadcaster;
import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat.services.cache.GpxObject;
import ch.bailu.aat.services.cache.GpxObjectStatic;
import ch.bailu.aat.services.cache.ObjectHandle;

public class CustomFileSource extends ContentSource {
    private final ServiceContext scontext;
    private ObjectHandle   handle=ObjectHandle.NULL;
    private final String fileID;

    private final BroadcastReceiver  onChangedInCache = new BroadcastReceiver () {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (AppBroadcaster.hasFile(intent, handle.toString())) {
                forceUpdate();
            }
        }
    };


    public CustomFileSource(ServiceContext sc, String fID) {
        scontext=sc;
        fileID=fID;

        AppBroadcaster.register(sc.getContext(), onChangedInCache, AppBroadcaster.FILE_CHANGED_INCACHE);
    }   

    
    @Override
    public void close() {
        scontext.getContext().unregisterReceiver(onChangedInCache);
        handle.free();
    }



    @Override
    public void forceUpdate() {
        ObjectHandle h =  scontext.getCacheService().getObject(fileID, new GpxObjectStatic.Factory());

        handle.free();
        handle=h;

        if (GpxObject.class.isInstance(h) && h.isReady()) {
            updateGpxContent(new GpxFileWrapper(new File(h.toString()), ((GpxObject)h).getGpxList()));
        }

    }
}

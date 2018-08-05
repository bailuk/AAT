package ch.bailu.aat.dispatcher;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import ch.bailu.aat.gpx.GpxFileWrapper;
import ch.bailu.aat.gpx.GpxInformation;
import ch.bailu.aat.gpx.InfoID;
import ch.bailu.aat.services.InsideContext;
import ch.bailu.aat.util.AppBroadcaster;
import ch.bailu.aat.util.AppIntent;
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
            if (AppIntent.hasFile(intent, fileID)) {
                requestUpdate();
            }
        }
    };


    public CustomFileSource(ServiceContext sc, String fID) {
        scontext=sc;
        fileID=fID;
    }


    @Override
    public void requestUpdate() {
        new InsideContext(scontext) {
            @Override
            public void run() {
                ObjectHandle h = scontext.getCacheService().getObject(fileID, new GpxObjectStatic.Factory());

                handle.free();
                handle = h;

                if (h instanceof GpxObject && h.isReadyAndLoaded()) {
                    sendUpdate(InfoID.FILEVIEW, new GpxFileWrapper(h.getFile(), ((GpxObject) h).getGpxList()));
                }

            }
        };
    }



    @Override
    public void onPause() {
        scontext.getContext().unregisterReceiver(onChangedInCache);
        handle.free();
        handle = ObjectHandle.NULL;
    }


    @Override
    public void onResume() {
        AppBroadcaster.register(scontext.getContext(), onChangedInCache,
                AppBroadcaster.FILE_CHANGED_INCACHE);
    }

    @Override
    public int getIID() {
        return InfoID.FILEVIEW;
    }

    @Override
    public GpxInformation getInfo() {
        return GpxInformation.NULL;
    }
}

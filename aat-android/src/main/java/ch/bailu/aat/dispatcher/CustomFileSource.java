package ch.bailu.aat.dispatcher;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import ch.bailu.aat_lib.gpx.GpxFileWrapper;
import ch.bailu.aat_lib.service.InsideContext;
import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat_lib.service.cache.Obj;
import ch.bailu.aat_lib.service.cache.ObjGpx;
import ch.bailu.aat_lib.service.cache.ObjGpxStatic;
import ch.bailu.aat_lib.service.cache.ObjNull;
import ch.bailu.aat.util.AppIntent;
import ch.bailu.aat.util.OldAppBroadcaster;
import ch.bailu.aat_lib.dispatcher.AppBroadcaster;
import ch.bailu.aat_lib.dispatcher.ContentSource;
import ch.bailu.aat_lib.gpx.GpxInformation;
import ch.bailu.aat_lib.gpx.InfoID;

public class CustomFileSource extends ContentSource {
    private final ServiceContext scontext;
    private Obj handle= ObjNull.NULL;
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
                Obj h = scontext.getCacheService().getObject(fileID, new ObjGpxStatic.Factory());

                handle.free();
                handle = h;

                if (h instanceof ObjGpx && h.isReadyAndLoaded()) {
                    sendUpdate(InfoID.FILEVIEW, new GpxFileWrapper(h.getFile(), ((ObjGpx) h).getGpxList()));
                }

            }
        };
    }



    @Override
    public void onPause() {
        scontext.getContext().unregisterReceiver(onChangedInCache);
        handle.free();
        handle = ObjNull.NULL;
    }


    @Override
    public void onResume() {
        OldAppBroadcaster.register(scontext.getContext(), onChangedInCache,
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

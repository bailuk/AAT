package ch.bailu.aat.dispatcher;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import ch.bailu.aat.gpx.GpxInformation;
import ch.bailu.aat.services.cache.GpxObjectEditable;
import ch.bailu.aat.util.AppBroadcaster;
import ch.bailu.aat.util.AppIntent;
import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat.services.editor.EditorHelper;

public class EditorSource extends ContentSource {

    private final ServiceContext scontext;
    private final EditorHelper edit;


    private final BroadcastReceiver onFileEdited = new BroadcastReceiver () {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (AppIntent.hasFile(intent, edit.getVID())) {
                requestUpdate();
            }
        }
    };

    
    public EditorSource (ServiceContext sc, EditorHelper e) {
        scontext = sc;
        edit = e;
    }



    @Override
    public void requestUpdate() {
        sendUpdate(edit.getInfoID(), edit.getInformation());
    }


    @Override
    public void onPause() {
        scontext.getContext().unregisterReceiver(onFileEdited);
        edit.onPause();
    }


    @Override
    public void onResume() {
        AppBroadcaster.register(scontext.getContext(), onFileEdited, AppBroadcaster.FILE_CHANGED_INCACHE);
        edit.onResume();
    }
}

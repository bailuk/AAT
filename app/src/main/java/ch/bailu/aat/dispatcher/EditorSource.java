package ch.bailu.aat.dispatcher;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import ch.bailu.aat.gpx.GpxInformation;
import ch.bailu.aat.helpers.AppBroadcaster;
import ch.bailu.aat.helpers.AppIntent;
import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat.services.editor.EditorHelper;

public class EditorSource extends ContentSource {

    private final ServiceContext scontext;
    private final EditorHelper edit;


    private final BroadcastReceiver onFileEdited = new BroadcastReceiver () {
        @Override
        public void onReceive(Context context, Intent intent) {
            update(intent, edit.getInformation());
        }

        private void update(Intent intent, GpxInformation info) {
            String id=info.getPath();
            
            if (AppIntent.hasFile(intent, id)) {
                forceUpdate();
            }
        }

    };

    
    public EditorSource (ServiceContext sc, EditorHelper e) {
        scontext = sc;
        edit = e;
    }


    @Override
    public void close() {
    }

    
    @Override
    public void forceUpdate() {
        onContentUpdated(edit.getInformation());
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

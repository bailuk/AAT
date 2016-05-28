package ch.bailu.aat.dispatcher;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import ch.bailu.aat.gpx.GpxInformation;
import ch.bailu.aat.helpers.AppBroadcaster;
import ch.bailu.aat.services.ServiceContext;

public class EditorSource extends ContentSource {

    private final ServiceContext scontext;
    private final int ID;


    private BroadcastReceiver onFileEdited = new BroadcastReceiver () {
        @Override
        public void onReceive(Context context, Intent intent) {
            GpxInformation info_a = scontext.getEditorService().getInformation(GpxInformation.ID.INFO_ID_EDITOR_OVERLAY);
            GpxInformation info_b = scontext.getEditorService().getInformation(GpxInformation.ID.INFO_ID_EDITOR_DRAFT);

            update(intent, info_a);
            update(intent, info_b);
        }

        private void update(Intent intent, GpxInformation info) {
            String id=info.getPath();
            
            if (AppBroadcaster.hasFile(intent, id)) {
                updateIfRequested(info);
            }
        }

  
    };

    
    public EditorSource (ServiceContext sc, int id) {
        ID = id;
        scontext = sc;
        AppBroadcaster.register(scontext.getContext(), onFileEdited, AppBroadcaster.FILE_CHANGED_INCACHE);
    }


    @Override
    public void close() {
        scontext.getContext().unregisterReceiver(onFileEdited);
    }

    
    @Override
    public void forceUpdate() {
        updateIfRequested(scontext.getEditorService().getInformation(GpxInformation.ID.INFO_ID_EDITOR_DRAFT));
        updateIfRequested(scontext.getEditorService().getInformation(GpxInformation.ID.INFO_ID_EDITOR_OVERLAY));
    }
    
    private void updateIfRequested(GpxInformation info) {
        if (info.getID()== ID) {
            updateGpxContent(info);
        }
        
    }
}

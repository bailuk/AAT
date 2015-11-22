package ch.bailu.aat.dispatcher;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import ch.bailu.aat.gpx.GpxInformation;
import ch.bailu.aat.helpers.AppBroadcaster;
import ch.bailu.aat.services.editor.EditorService;

public class EditorSource extends ContentSource {

    private final EditorService editor;
    private final int ID;


    private BroadcastReceiver onFileEdited = new BroadcastReceiver () {
        @Override
        public void onReceive(Context context, Intent intent) {
            GpxInformation info_a = editor.getOverlayInformation();
            GpxInformation info_b = editor.getDraftIntormation();

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

    
    public EditorSource (EditorService e, int id) {
        ID = id;
        editor = e;
        AppBroadcaster.register(editor, onFileEdited, AppBroadcaster.FILE_CHANGED_INCACHE);
    }


    @Override
    public void cleanUp() {
        editor.unregisterReceiver(onFileEdited);
    }

    
    @Override
    public void forceUpdate() {
        updateIfRequested(editor.getDraftIntormation());
        updateIfRequested(editor.getOverlayInformation());
    }
    
    private void updateIfRequested(GpxInformation info) {
        if (info.getID()== ID) {
            updateGpxContent(info);
        }
        
    }
}

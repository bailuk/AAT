package ch.bailu.aat.dispatcher;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import ch.bailu.aat.gpx.GpxInformation;
import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat.services.editor.EditorHelper;
import ch.bailu.aat.services.editor.EditorInterface;
import ch.bailu.aat.util.AppBroadcaster;
import ch.bailu.aat.util.AppIntent;
import ch.bailu.util_java.foc.Foc;

public class EditorSource extends ContentSource implements  EditorSourceInterface{

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

    
    public EditorSource (ServiceContext sc) {
        scontext = sc;
        edit = new EditorHelper(sc);
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

    @Override
    public boolean isEditing() {
        return true;
    }

    @Override
    public EditorInterface getEditor() {
        return edit.getEditor();
    }

    @Override
    public int getIID() {
        return edit.getInfoID();
    }

    @Override
    public GpxInformation getInfo() {
        return edit.getInformation();
    }

    @Override
    public Foc getFile() {
        return edit.getFile();
    }

    @Override
    public void edit() {

    }

    public int getInfoID() {
        return edit.getInfoID();
    }

    public void edit(Foc file) {
        edit.edit(file);
    }
}

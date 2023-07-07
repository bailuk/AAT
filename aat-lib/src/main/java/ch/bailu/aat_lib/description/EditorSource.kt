package ch.bailu.aat_lib.description;

import javax.annotation.Nonnull;

import ch.bailu.aat_lib.app.AppContext;
import ch.bailu.aat_lib.dispatcher.AppBroadcaster;
import ch.bailu.aat_lib.dispatcher.BroadcastData;
import ch.bailu.aat_lib.dispatcher.BroadcastReceiver;
import ch.bailu.aat_lib.dispatcher.ContentSource;
import ch.bailu.aat_lib.dispatcher.EditorSourceInterface;
import ch.bailu.aat_lib.gpx.GpxFileWrapper;
import ch.bailu.aat_lib.gpx.GpxInformation;
import ch.bailu.aat_lib.gpx.GpxList;
import ch.bailu.aat_lib.service.editor.EditorHelper;
import ch.bailu.aat_lib.service.editor.EditorInterface;
import ch.bailu.foc.Foc;

public class EditorSource extends ContentSource implements EditorSourceInterface {

    private final AppContext appContext;
    private final EditorHelper edit;


    private final BroadcastReceiver onFileEdited = new BroadcastReceiver () {
        @Override
        public void onReceive(@Nonnull String... args) {
            if (BroadcastData.has(args, edit.getVID())) {
                requestUpdate();
            }
        }
    };

    public EditorSource (AppContext appContext) {
        this.appContext = appContext;
        edit = new EditorHelper(appContext);
    }

    @Override
    public void requestUpdate() {
        sendUpdate(edit.getInfoID(), edit.getInfo());
    }

    @Override
    public void onPause() {
        appContext.getBroadcaster().unregister(onFileEdited);
        edit.onPause();
    }

    @Override
    public void onResume() {
        appContext.getBroadcaster().register(onFileEdited, AppBroadcaster.FILE_CHANGED_INCACHE);
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
        return edit.getInfo();
    }

    @Override
    public Foc getFile() {
        return edit.getFile();
    }

    @Override
    public void edit() {}

    public void edit(Foc file) {
        sendUpdate(edit.getInfoID(), new GpxFileWrapper(getFile(), GpxList.NULL_ROUTE));
        edit.edit(file);
        requestUpdate();
    }

    public void editDraft() {
        sendUpdate(edit.getInfoID(), new GpxFileWrapper(getFile(), GpxList.NULL_ROUTE));
        edit.editDraft();
        requestUpdate();
    }
}

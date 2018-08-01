package ch.bailu.aat.dispatcher;

import ch.bailu.aat.gpx.GpxInformation;
import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat.services.editor.EditorHelper;
import ch.bailu.util_java.foc.Foc;

public class EditorOrBackupSource extends ContentSource implements OnContentUpdatedInterface {
    private final EditorHelper editorHelper;
    private final EditorSource editorSource;
    private final ContentSource backupSource;

    private Foc file = null;

    private boolean isEditing = false;


    public EditorOrBackupSource(ServiceContext sc, ContentSource source) {
        editorHelper = new EditorHelper(sc);
        editorSource = new EditorSource(sc, editorHelper);
        backupSource = source;

        backupSource.setTarget(this);
        editorSource.setTarget(this);

    }


    public void releaseEditor() {
        if (isEditing) {
            isEditing = false;

            editorSource.onPause();
            backupSource.onResume();

            requestUpdate();
        }

    }

    public void edit() {
        if (file != null && !isEditing) {
            isEditing = true;

            editorHelper.edit(file);
            editorSource.onResume();
            backupSource.onPause();

            requestUpdate();
        }
    }


    public boolean isEditing() {
        return isEditing;
    }


    @Override
    public void requestUpdate() {
        if (isEditing) editorSource.requestUpdate();
        else backupSource.requestUpdate();
    }


    @Override
    public void onPause() {
        if (isEditing) editorSource.onPause();
        else backupSource.onPause();
    }


    @Override
    public void onResume() {
        if (isEditing) editorSource.onResume();
        else backupSource.onResume();
    }


    @Override
    public void onContentUpdated(int iid, GpxInformation info) {
        if (iid == editorHelper.getInfoID()) {
            if (isEditing)  sendUpdate(iid, info);
            else sendUpdate(iid, GpxInformation.NULL);
        } else {
            file = info.getFile();

            if (isEditing) sendUpdate(iid, GpxInformation.NULL);
            else sendUpdate(iid, info);
        }
    }
}

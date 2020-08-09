package ch.bailu.aat.dispatcher;

import ch.bailu.aat.gpx.GpxFileWrapper;
import ch.bailu.aat.gpx.GpxInformation;
import ch.bailu.aat.gpx.GpxList;
import ch.bailu.aat.gpx.InfoID;
import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat.services.editor.EditorInterface;
import ch.bailu.foc.Foc;

public class EditorOrBackupSource extends ContentSource implements EditorSourceInterface {

    private final EditorSource editorSource;
    private final ContentSource backupSource;

    private boolean isEditing = false;


    public EditorOrBackupSource(ServiceContext sc, ContentSource source) {
        editorSource = new EditorSource(sc);
        backupSource = source;

    }

    public boolean isModified() {
        return isEditing && getEditor().isModified();
    }

    public void releaseEditorSave() {
        if (isEditing) {
            getEditor().save();
            releaseEditorDiscard();
        }
    }


    @Override
    public void setTarget(OnContentUpdatedInterface t) {
        super.setTarget(t);
        editorSource.setTarget(t);
        backupSource.setTarget(t);
    }


    public void releaseEditorDiscard() {
        if (isEditing) {
            isEditing = false;

            editorSource.onPause();
            backupSource.onResume();

            requestUpdate();
        }

    }

    public void edit() {
        Foc file = backupSource.getInfo().getFile();
        if (file != null && !isEditing) {
            isEditing = true;

            editorSource.edit(file);
            editorSource.onResume();
            backupSource.onPause();

            requestUpdate();
        }
    }


    public boolean isEditing() {
        return isEditing;
    }

    @Override
    public EditorInterface getEditor() {
        return editorSource.getEditor();
    }

    @Override
    public int getIID() {
        return editorSource.getIID();
    }

    @Override
    public GpxInformation getInfo() {
        if (isEditing) return editorSource.getInfo();
        return backupSource.getInfo();
    }


    @Override
    public void requestUpdate() {
        if (isEditing) {
            requestBackupNullUpdate();
            editorSource.requestUpdate();


        }  else {
            requestEditorNullUpdate();
            backupSource.requestUpdate();

        }
    }



    @Override
    public void onPause() {
        if (isEditing) editorSource.onPause();
        else backupSource.onPause();
    }


    @Override
    public void onResume() {
        if (isEditing) {
            requestBackupNullUpdate();
            editorSource.onResume();

        } else {
            requestEditorNullUpdate();
            backupSource.onResume();

        }
    }


    @Override
    public Foc getFile() {
        if (isEditing) return editorSource.getFile();
        return backupSource.getInfo().getFile();
    }

    private void requestEditorNullUpdate() {
        sendUpdate(InfoID.EDITOR_OVERLAY, new GpxFileWrapper(getFile(), GpxList.NULL_ROUTE));
    }

    private void requestBackupNullUpdate() {
        sendUpdate(backupSource.getIID(), new GpxFileWrapper(getFile(), GpxList.NULL_ROUTE));
    }

}

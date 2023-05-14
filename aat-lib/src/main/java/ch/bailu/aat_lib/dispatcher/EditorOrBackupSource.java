package ch.bailu.aat_lib.dispatcher;

import javax.annotation.Nonnull;

import ch.bailu.aat_lib.app.AppContext;
import ch.bailu.aat_lib.description.EditorSource;
import ch.bailu.aat_lib.gpx.GpxFileWrapper;
import ch.bailu.aat_lib.gpx.GpxInformation;
import ch.bailu.aat_lib.gpx.GpxList;
import ch.bailu.aat_lib.service.editor.EditorInterface;
import ch.bailu.foc.Foc;

public class EditorOrBackupSource implements EditorSourceInterface, ContentSourceInterface {

    private final EditorSource editorSource;
    private final ContentSourceInterface backupSource;
    private boolean isEditing = false;

    public EditorOrBackupSource(AppContext appContext, ContentSourceInterface source) {
        editorSource = new EditorSource(appContext);
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

    public void releaseEditorDiscard() {
        if (isEditing) {
            isEditing = false;

            editorSource.onPause();
            backupSource.onResume();
            requestUpdate();
        }
    }

    @Override
    public void setTarget(@Nonnull OnContentUpdatedInterface target) {
        editorSource.setTarget(target);
        backupSource.setTarget(target);
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
            requestNullUpdate(backupSource);
            editorSource.requestUpdate();
        }  else {
            requestNullUpdate(editorSource);
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
            requestNullUpdate(backupSource);
            editorSource.onResume();
        } else {
            requestNullUpdate(editorSource);
            backupSource.onResume();
        }
    }

    @Override
    public Foc getFile() {
        if (isEditing) return editorSource.getFile();
        return backupSource.getInfo().getFile();
    }

    private void requestNullUpdate(ContentSourceInterface source) {
        editorSource.sendUpdate(source.getIID(), new GpxFileWrapper(getFile(), GpxList.NULL_ROUTE));
    }
}

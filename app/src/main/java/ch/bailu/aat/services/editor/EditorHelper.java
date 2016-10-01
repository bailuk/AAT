package ch.bailu.aat.services.editor;

import java.io.File;

import ch.bailu.aat.gpx.GpxInformation;
import ch.bailu.aat.helpers.AppDirectory;
import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat.services.cache.GpxObjectEditable;
import ch.bailu.aat.services.cache.ObjectHandle;

public class EditorHelper {
    private final ServiceContext scontext;


    private ObjectHandle handle = ObjectHandle.NULL;

    private int IID=GpxInformation.ID.INFO_ID_EDITOR_DRAFT;
    private String ID;



    public EditorHelper(ServiceContext sc) {
        scontext = sc;

        IID = GpxInformation.ID.INFO_ID_EDITOR_DRAFT;
        ID = AppDirectory.getEditorDraft(scontext.getContext()).getAbsolutePath();
    }


    public void edit(File f) {
        IID = GpxInformation.ID.INFO_ID_EDITOR_OVERLAY;
        ID = f.getAbsolutePath();
        onResume();
    }


    public void onResume() {
        ObjectHandle new_handle = GpxObjectEditable.loadEditor(scontext, ID, IID);

        handle.free();
        handle = new_handle;
    }

    public void onPause() {
        if (IID==GpxInformation.ID.INFO_ID_EDITOR_DRAFT) save();

        handle.free();
        handle = ObjectHandle.NULL;

    }


/*
    public int getIID() {
        return IID;
    }
  */
    public GpxInformation getInformation() {
        if (GpxObjectEditable.class.isInstance(handle)) {
            return ((GpxObjectEditable)handle).editor;
        }
        return GpxInformation.NULL;
    }


    public EditorInterface getEditor() {
        if (GpxObjectEditable.class.isInstance(handle)) {
            return ((GpxObjectEditable)handle).editor;
        }
        return EditorInterface.NULL;
    }


    public void save() {
        EditorInterface e = getEditor();
        if (e.isModified()) { 
            e.save();
        }

    }

}

package ch.bailu.aat.services.editor;

import java.io.Closeable;
import java.io.File;

import ch.bailu.aat.gpx.GpxInformation;
import ch.bailu.aat.helpers.AppDirectory;
import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat.services.cache.GpxObjectEditable;
import ch.bailu.aat.services.cache.ObjectHandle;

public class EditorHelper implements Closeable {

    private final ServiceContext scontext;
    private ObjectHandle handle = ObjectHandle.NULL;
    private final int IID;
    
    public EditorHelper(ServiceContext sc) {
        IID=GpxInformation.ID.INFO_ID_EDITOR_DRAFT;
        scontext = sc;
    }
    
    
    public EditorHelper(ServiceContext sc, int iid) {
        IID=iid;
        scontext = sc;
    }
    

    public void edit(File f) {
        ObjectHandle new_handle = GpxObjectEditable.loadEditor(scontext, 

                f.getAbsolutePath(), 
                IID);

        handle.free();
        handle = new_handle;
    }



    public void edit() {
        edit(AppDirectory.getEditorDraft(scontext.getContext()));
    }

    
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

    
    @Override
    public void close() {
        handle.free();
        handle = ObjectHandle.NULL;
    }
}

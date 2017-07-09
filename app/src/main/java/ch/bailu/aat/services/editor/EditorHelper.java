package ch.bailu.aat.services.editor;

import ch.bailu.aat.gpx.GpxInformation;
import ch.bailu.aat.gpx.InfoID;
import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat.services.cache.GpxObjectEditable;
import ch.bailu.aat.services.cache.ObjectHandle;
import ch.bailu.aat.util.fs.AppDirectory;
import ch.bailu.simpleio.foc.Foc;

public class EditorHelper {
    private final ServiceContext scontext;


    private ObjectHandle handle = ObjectHandle.NULL;

    private int IID= InfoID.EDITOR_DRAFT;
    private Foc file;



    public EditorHelper(ServiceContext sc) {
        scontext = sc;

        IID = InfoID.EDITOR_DRAFT;
        file = AppDirectory.getEditorDraft(scontext.getContext());
    }


    public void edit(Foc f) {
        IID = InfoID.EDITOR_OVERLAY;
        file = f;
        onResume();
    }


    public void onResume() {
        ObjectHandle new_handle = GpxObjectEditable.loadEditor(scontext, file);

        handle.free();
        handle = new_handle;
    }

    public void onPause() {
        if (IID==InfoID.EDITOR_DRAFT) save();

        handle.free();
        handle = ObjectHandle.NULL;

    }


    public int getInfoID() {
        return IID;
    }

    public GpxInformation getInformation() {
        if (handle instanceof GpxObjectEditable) {
            return ((GpxObjectEditable)handle).getEditor();
        }
        return GpxInformation.NULL;
    }


    public EditorInterface getEditor() {
        if (GpxObjectEditable.class.isInstance(handle)) {
            return ((GpxObjectEditable)handle).getEditor();
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

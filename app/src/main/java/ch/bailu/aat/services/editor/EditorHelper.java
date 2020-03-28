package ch.bailu.aat.services.editor;

import ch.bailu.aat.gpx.GpxInformation;
import ch.bailu.aat.gpx.InfoID;
import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat.services.cache.ObjGpxEditable;
import ch.bailu.aat.services.cache.Obj;
import ch.bailu.aat.util.fs.AppDirectory;
import ch.bailu.util_java.foc.Foc;

public final class EditorHelper {
    private final ServiceContext scontext;


    private Obj handle = Obj.NULL;

    private int IID;
    private Foc file;
    private String vid;


    public EditorHelper(ServiceContext sc) {
        scontext = sc;

        IID = InfoID.EDITOR_DRAFT;
        file = AppDirectory.getEditorDraft(scontext.getContext());
        vid = ObjGpxEditable.getVirtualID(file);
    }


    public void edit(Foc f) {
        IID = InfoID.EDITOR_OVERLAY;
        file = f;
        vid = ObjGpxEditable.getVirtualID(file);

        onResume();
    }


    public void onResume() {
        Obj new_handle = scontext.getCacheService().getObject(
                vid,
                new ObjGpxEditable.Factory(file));

        handle.free();
        handle = new_handle;
    }

    public void onPause() {
        if (IID==InfoID.EDITOR_DRAFT) save();

        handle.free();
        handle = Obj.NULL;

    }


    public int getInfoID() {
        return IID;
    }
    public String getVID() { return vid; }

    public GpxInformation getInformation() {
        if (handle instanceof ObjGpxEditable) {
            return ((ObjGpxEditable)handle).getEditor();
        }
        return GpxInformation.NULL;
    }


    public EditorInterface getEditor() {
        if (ObjGpxEditable.class.isInstance(handle)) {
            return ((ObjGpxEditable)handle).getEditor();
        }
        return EditorInterface.NULL;
    }


    public void save() {
        EditorInterface e = getEditor();
        if (e.isModified()) {
            e.save();
        }
    }

    public Foc getFile() {
        return file;
    }
}

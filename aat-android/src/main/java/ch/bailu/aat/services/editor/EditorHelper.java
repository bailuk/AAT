package ch.bailu.aat.services.editor;

import ch.bailu.aat.preferences.system.AndroidSolidDataDirectory;
import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat.services.cache.ObjGpxEditable;
import ch.bailu.aat_lib.service.cache.Obj;
import ch.bailu.aat_lib.service.cache.ObjNull;
import ch.bailu.aat_lib.gpx.GpxInformation;
import ch.bailu.aat_lib.gpx.InfoID;
import ch.bailu.aat_lib.util.fs.AppDirectory;
import ch.bailu.foc.Foc;

public final class EditorHelper {
    private final ServiceContext scontext;


    private Obj handle = ObjNull.NULL;

    private int IID;
    private Foc file;
    private String vid;


    public EditorHelper(ServiceContext sc) {
        scontext = sc;

        IID = InfoID.EDITOR_DRAFT;
        file = AppDirectory.getEditorDraft(new AndroidSolidDataDirectory(scontext.getContext()));
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
        if (IID== InfoID.EDITOR_DRAFT) save();

        handle.free();
        handle = ObjNull.NULL;

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

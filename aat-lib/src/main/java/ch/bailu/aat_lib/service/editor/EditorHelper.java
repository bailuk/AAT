package ch.bailu.aat_lib.service.editor;

import ch.bailu.aat_lib.app.AppContext;
import ch.bailu.aat_lib.gpx.GpxInformation;
import ch.bailu.aat_lib.gpx.InfoID;
import ch.bailu.aat_lib.service.cache.Obj;
import ch.bailu.aat_lib.service.cache.ObjGpxEditable;
import ch.bailu.aat_lib.service.cache.ObjNull;
import ch.bailu.aat_lib.util.fs.AppDirectory;
import ch.bailu.foc.Foc;

public final class EditorHelper {
    private final AppContext appContext;

    private Obj handle = ObjNull.NULL;

    private int IID;
    private Foc file;
    private String vid;


    public EditorHelper(AppContext sc) {
        appContext = sc;

        IID = InfoID.EDITOR_DRAFT;
        file = AppDirectory.getEditorDraft(appContext.getDataDirectory());
        vid = ObjGpxEditable.getVirtualID(file);
    }


    public void edit(Foc f) {
        IID = InfoID.EDITOR_OVERLAY;
        file = f;
        vid = ObjGpxEditable.getVirtualID(file);

        onResume();
    }


    public void onResume() {
        Obj new_handle = appContext.getServices().getCacheService().getObject(
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
        if (handle instanceof ObjGpxEditable) {
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

package ch.bailu.aat_lib.service.editor;

import ch.bailu.aat_lib.app.AppContext;
import ch.bailu.aat_lib.gpx.GpxInformation;
import ch.bailu.aat_lib.gpx.GpxInformationProvider;
import ch.bailu.aat_lib.gpx.InfoID;
import ch.bailu.aat_lib.service.cache.Obj;
import ch.bailu.aat_lib.service.cache.ObjNull;
import ch.bailu.aat_lib.service.cache.gpx.ObjGpx;
import ch.bailu.aat_lib.service.cache.gpx.ObjGpxEditable;
import ch.bailu.aat_lib.util.fs.AppDirectory;
import ch.bailu.foc.Foc;

public final class EditorHelper implements GpxInformationProvider {
    private final AppContext appContext;

    private Obj handle = ObjNull.NULL;

    private int IID;
    private Foc file;
    private String vid;


    public EditorHelper(AppContext context) {
        appContext = context;
        edit(getDraft(), InfoID.EDITOR_DRAFT);
    }

    public void editDraft() {
        edit(getDraft(), InfoID.EDITOR_DRAFT);
        onResume();
    }

    public void edit(Foc file) {
        edit(file, InfoID.EDITOR_OVERLAY);
        onResume();
    }

    private void edit(Foc file, int iid) {
        this.IID = iid;
        this.file = file;
        this.vid = ObjGpxEditable.getVirtualID(file);
    }

    public void onResume() {
        Obj newHandle = appContext.getServices().getCacheService().getObject(
                vid,
                new ObjGpxEditable.Factory(file));

        handle.free();
        handle = newHandle;
    }

    public void onPause() {
        if (IID == InfoID.EDITOR_DRAFT) save();

        handle.free();
        handle = ObjGpx.NULL;
    }

    public int getInfoID() {
        return IID;
    }

    public String getVID() { return vid; }

    @Override
    public GpxInformation getInfo() {
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
        EditorInterface editor = getEditor();
        if (editor.isModified()) {
            editor.save();
        }
    }

    public Foc getFile() {
        return file;
    }

    private Foc getDraft() {
        return AppDirectory.getEditorDraft(appContext.getDataDirectory());
    }

}

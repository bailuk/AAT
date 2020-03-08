package ch.bailu.aat.services.cache;

import android.content.Context;

import ch.bailu.aat.coordinates.BoundingBoxE6;
import ch.bailu.aat.gpx.GpxInformation;
import ch.bailu.aat.gpx.GpxList;
import ch.bailu.aat.gpx.GpxPoint;
import ch.bailu.aat.gpx.GpxPointNode;
import ch.bailu.aat.gpx.interfaces.GpxType;
import ch.bailu.aat.gpx.writer.GpxListWriter;
import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat.services.editor.EditorInterface;
import ch.bailu.aat.services.editor.GpxEditor;
import ch.bailu.aat.util.AppBroadcaster;
import ch.bailu.aat.util.fs.AppDirectory;
import ch.bailu.aat.util.ui.AppLog;
import ch.bailu.util_java.foc.Foc;

public final class GpxObjectEditable extends  GpxObject {

    private GpxObject currentHandle=NULL;
    private final Foc file;

    private final GpxListEditor editor;


    public GpxObjectEditable(String _id, Foc _file, ServiceContext sc) {
        super(_id);
        file = _file;

        editor = new GpxListEditor(sc.getContext());
        sc.getCacheService().addToBroadcaster(this);
    }

    public GpxListEditor getEditor() {
        return editor;
    }

    @Override
    public void onInsert(ServiceContext sc) {
        ObjectHandle handle = sc.getCacheService().getObject(file.getPath(), new GpxObjectStatic.Factory());

        if (handle instanceof GpxObject) {
            currentHandle = (GpxObject) handle;
        } else {
            currentHandle = GpxObject.NULL;
        }
        editor.loadIntoEditor(currentHandle.getGpxList());
    }


    @Override
    public void onRemove(ServiceContext sc) {
        currentHandle.free();
        currentHandle=NULL;
    }


    public boolean isReadyAndLoaded() {
        return currentHandle.isReadyAndLoaded();
    }

    @Override
    public long getSize() {
        return MIN_SIZE;
    }


    @Override
    public void onDownloaded(String id, String url, ServiceContext sc) {}


    @Override
    public void onChanged(String id, ServiceContext sc) {
        if (id.equals(file.getPath())) {
            editor.loadIntoEditor(currentHandle.getGpxList());
        }
    }





    public class GpxListEditor extends GpxInformation implements EditorInterface {
        private GpxEditor editor = new GpxEditor(GpxList.NULL_ROUTE);

        private boolean modified = false;

        private final Context context;

        public GpxListEditor(Context c) {
            context=c;
        }


        public void loadIntoEditor(GpxList list) {
            editor = new GpxEditor(list);
            modified = false;
            modified(false);
        }

        @Override
        public void setType(GpxType type) {
            editor.setType(type);
            modified(true);
        }


        @Override
        public void remove() {
            editor.unlinkSelectedNode();
            modified(true);
        }


        @Override
        public void add(GpxPoint point) {
            editor.insertNode(point);
            modified(true);
        }



        @Override
        public void up() {
            editor.moveSelectedUp();
            modified(true);
        }


        @Override
        public void down() {
            editor.moveSelectedDown();
            modified(true);
        }

        @Override
        public void clear() {
            editor.clear();
            modified(true);
        }


        @Override
        public void undo() {
            if (editor.undo()) modified(true);
        }
        @Override
        public void redo() {
            if (editor.redo()) modified(true);
        }

        private void modified(boolean m) {
            modified = modified || m;

            setVisibleTrackPoint(editor.getSelectedPoint());
            setVisibleTrackSegment(editor.getList().getDelta());

            AppBroadcaster.broadcast(context, AppBroadcaster.FILE_CHANGED_INCACHE, getID());
        }

        @Override
        public boolean isModified() {
            return modified;
        }



        @Override
        public void select(GpxPointNode point) {
            editor.select(point, editor.getList());
            modified(false);
        }


        @Override
        public void save() {
            try {
                new GpxListWriter(editor.getList(),file).close();
                modified=false;

                AppBroadcaster.broadcast(context, AppBroadcaster.FILE_CHANGED_ONDISK, file.getPath(), getID());
            } catch (Exception e) {
                AppLog.e(context, this, e);
            }
        }


        @Override
        public void inverse() {
            editor.inverse();
            modified(true);
        }

        @Override
        public void attach(GpxList toAttach) {
            editor.attach(toAttach);
            modified(true);

        }

        @Override
        public void fix() {
            editor.fix();
            modified(true);

        }

        @Override
        public void simplify() {
            editor.simplify();
            modified(true);
        }

        @Override
        public void cutPreceding() {
            editor.cutPreceding();
            modified(true);
        }

        @Override
        public void cutRemaining() {
            editor.cutRemaining();
            modified(true);
        }


        @Override
        public void saveTo(Foc destDir) {
            String prefix = AppDirectory.parsePrefix(file);

            try {
                final Foc file =
                        AppDirectory.generateUniqueFilePath(
                                destDir,
                                prefix,
                                AppDirectory.GPX_EXTENSION);

                new GpxListWriter(editor.getList(),file).close();

                AppBroadcaster.broadcast(context, AppBroadcaster.FILE_CHANGED_ONDISK,
                        file.getPath(), getID());

            } catch (Exception e) {
                AppLog.e(context, this, e);
            }
        }


        @Override
        public GpxPointNode getSelected() {
            return editor.getSelectedPoint();
        }


        @Override
        public GpxList getGpxList() {
            return editor.getList();
        }

        @Override
        public Foc getFile() {
            return file;
        }

        @Override
        public boolean isLoaded() {
            return true;
        }


        @Override
        public BoundingBoxE6 getBoundingBox() {
            return editor.getList().getDelta().getBoundingBox();
        }

    }


    public static class Factory extends ObjectHandle.Factory {
        private final Foc file;


        public Factory(Foc f) {
            file=f;
        }




        @Override
        public ObjectHandle factory(String id, ServiceContext sc) {
            return new GpxObjectEditable(id, file, sc);
        }



    }


    public static String getVirtualID(Foc file) {
        return getVirtualID(file.getPath());
    }

    private static String getVirtualID(String cID) {
        return GpxObjectEditable.class.getSimpleName()+cID;
    }


    @Override
    public GpxList getGpxList() {
        return editor.getGpxList();
    }
}

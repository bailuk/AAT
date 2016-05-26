package ch.bailu.aat.services.cache;

import java.io.File;
import java.io.IOException;

import android.content.Context;
import ch.bailu.aat.coordinates.BoundingBox;
import ch.bailu.aat.gpx.GpxInformation;
import ch.bailu.aat.gpx.GpxList;
import ch.bailu.aat.gpx.GpxPoint;
import ch.bailu.aat.gpx.GpxPointNode;
import ch.bailu.aat.gpx.writer.GpxListWriter;
import ch.bailu.aat.helpers.AppBroadcaster;
import ch.bailu.aat.helpers.AppDirectory;
import ch.bailu.aat.helpers.AppLog;
import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat.services.editor.EditorInterface;
import ch.bailu.aat.services.editor.GpxEditor;

public class GpxObjectEditable extends  GpxObject {

    private GpxObject currentHandle=NULL;
    private final String path;
    private final String vid;
    
    public final GpxListEditor editor;
    
    
    public GpxObjectEditable(String id, String p, ServiceContext sc, int iID) {
        super(id);
        path=p;
        vid=id;
        
        editor = new GpxListEditor(sc.getContext(), iID);
        sc.getCacheService().addToBroadcaster(this);        
    }


    @Override
    public void onInsert(ServiceContext sc) {
        ObjectHandle handle = sc.getCacheService().getObject(path, new GpxObjectStatic.Factory());
        
        if (GpxObject.class.isInstance(handle)) {
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


    @Override
    public boolean isReady() {
        return currentHandle.isReady();
    }

    
    @Override
    public void onDownloaded(String id, String url, ServiceContext sc) {}


    @Override
    public void onChanged(String id, ServiceContext sc) {
        if (id.equals(path)) {
            editor.loadIntoEditor(currentHandle.getGpxList());
        }
    }


    

    
    public class GpxListEditor extends GpxInformation implements EditorInterface {
        private GpxEditor editor = new GpxEditor(GpxList.NULL_ROUTE);

        private boolean modified = false;
        private final int ID;
        
        private final Context context;

        public GpxListEditor(Context c, int iid) {
            ID=iid;
            context=c;
        }
        
        
        public void loadIntoEditor(GpxList list) {
            editor = new GpxEditor(list);
            modified = false;
            modified(false);
        }
        
        @Override
        public void toggle() {
            editor.toggleType();
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
            //AppBroadcaster.broadcast(context, AppBroadcaster.REQUEST_ELEVATION_UPDATE, vid);
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

            AppBroadcaster.broadcast(context, AppBroadcaster.FILE_CHANGED_INCACHE, vid);
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
                final File file = new File(path);

                new GpxListWriter(editor.getList(),file).close();
                modified=false;
                
                AppBroadcaster.broadcast(context, AppBroadcaster.FILE_CHANGED_ONDISK, path, vid);
            } catch (IOException e) {
                AppLog.e(context, this, e);
            }    
        }


        @Override
        public void discard() {
            loadIntoEditor(currentHandle.getGpxList());
        }
        
        
        @Override
        public void saveAs() {
            final File x = new File(path);
            String prefix = AppDirectory.parsePrefix(x);

            try {
                final File file = 
                        AppDirectory.generateUniqueFilePath(
                                AppDirectory.getDataDirectory(context, AppDirectory.DIR_OVERLAY), 
                                prefix, 
                                AppDirectory.GPX_EXTENSION);
                
                new GpxListWriter(editor.getList(),file).close();
                
                AppBroadcaster.broadcast(context, AppBroadcaster.FILE_CHANGED_ONDISK, file.toString(), vid);
            } catch (IOException e) {
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
        public int getID() {
            return ID;
        }


        @Override
        public String getName() {
            return new File(path).getName();
        }


        @Override
        public String getPath() {
            return vid;
        }


        @Override
        public boolean isLoaded() {
            return true;
        }


        @Override
        public BoundingBox getBoundingBox() {
            return editor.getList().getDelta().getBoundingBox();
        }
        
    };


    public static class Factory extends ObjectHandle.Factory {
        private final int infoID;
        private final String path;
        

        public Factory(String p, int iID) {
            path=p;
            infoID=iID;
        }


 

        @Override
        public ObjectHandle factory(String id, ServiceContext sc) {
            return new GpxObjectEditable(id,path, sc, infoID);
        }



    }
    
    public static GpxObjectEditable loadEditor(ServiceContext c, String path, int iID) {
        return (GpxObjectEditable) c.getCacheService().getObject(getVirtualID(path), new Factory(path, iID));
    }

    private static String getVirtualID(String cID) {
        return GpxObjectEditable.class.getSimpleName()+cID;
    }


    @Override
    public GpxList getGpxList() {
        return editor.getGpxList();
    }

}

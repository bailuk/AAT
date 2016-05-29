package ch.bailu.aat.services.editor;

import java.io.Closeable;
import java.io.File;

import ch.bailu.aat.gpx.GpxInformation;
import ch.bailu.aat.helpers.AppDirectory;
import ch.bailu.aat.helpers.AppFile;
import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat.services.VirtualService;
import ch.bailu.aat.services.cache.GpxObjectEditable;
import ch.bailu.aat.services.cache.ObjectHandle;


public class EditorService extends VirtualService {

    private final Self self;
    public Self getSelf() {
        return self;
    }


    public EditorService(ServiceContext sc) {
        super(sc);
        self = new SelfOn();
    }


    @Override
    public void close() {
        self.close();
    }




    public static class Self implements Closeable {

        public void editOverlay(File file) {}

        public GpxInformation getInformation(int id) {
            return GpxInformation.NULL;
        }


        public EditorInterface getEditor(int id) {
            return EditorInterface.NULL;
        }


        @Override
        public void close() {}

    };


    public class SelfOn extends Self {
        private ObjectHandle draft = ObjectHandle.NULL;
        private ObjectHandle overlay = ObjectHandle.NULL;
        private File file = AppFile.NULL_FILE;
        
        @Override
        public void editOverlay(File f) {
            file = f;
        }


        private ObjectHandle lockHandle(int id) {
            ObjectHandle handle;
            
            if (id == GpxInformation.ID.INFO_ID_EDITOR_DRAFT) {
                
                handle = GpxObjectEditable.loadEditor(getSContext(), 
                        
                        AppDirectory.getEditorDraft(getContext()).getAbsolutePath(), 
                        GpxInformation.ID.INFO_ID_EDITOR_DRAFT);
                
                draft.free();
                draft = handle;
                return draft;

                
            } else {
                handle = GpxObjectEditable.loadEditor(getSContext(), 
                        
                        file.getAbsolutePath(), 
                        GpxInformation.ID.INFO_ID_EDITOR_OVERLAY);
                
                overlay.free();
                overlay = handle;
                return overlay;
            }
        }


        @Override
        public GpxInformation getInformation(int id) {
            ObjectHandle handle = lockHandle(id);
            
            if (GpxObjectEditable.class.isInstance(handle)) {
                return ((GpxObjectEditable)handle).editor;
            }
            return GpxInformation.NULL;
        }
        
        
        @Override
        public EditorInterface getEditor(int id) {
            ObjectHandle handle = lockHandle(id);
            
            if (GpxObjectEditable.class.isInstance(handle)) {
                return ((GpxObjectEditable)handle).editor;
            }
            return EditorInterface.NULL;
        }


        

        @Override
        public void close() {
            EditorInterface draftE = getEditor(GpxInformation.ID.INFO_ID_EDITOR_DRAFT);
            if (draftE.isModified()) { 
                draftE.save();
            }
            
            
            draft.free();
            overlay.free();
            
            draft = ObjectHandle.NULL;
            overlay = ObjectHandle.NULL;
        }

        
    }


    @Override
    public void appendStatusText(StringBuilder builder) {
        // TODO Auto-generated method stub
        
    };
}

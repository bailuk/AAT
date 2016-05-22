package ch.bailu.aat.services.editor;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;

import ch.bailu.aat.gpx.GpxInformation;
import ch.bailu.aat.gpx.GpxPoint;
import ch.bailu.aat.gpx.GpxPointNode;
import ch.bailu.aat.helpers.AppDirectory;
import ch.bailu.aat.helpers.AppLog;
import ch.bailu.aat.services.AbsService;
import ch.bailu.aat.services.MultiServiceLink.ServiceNotUpException;
import ch.bailu.aat.services.cache.CacheService;
import ch.bailu.aat.services.cache.GpxObjectEditable;


public class EditorService extends AbsService {
    public static final Class<?> SERVICES[] = {
        CacheService.class
    };    
    
    
    private Self self = new Self();
    
    public class Self implements Closeable {

        public void editOverlay(File file) {}

        public GpxInformation getOverlayInformation() {
            return GpxInformation.NULL;
        }

        public GpxInformation getDraftInformation() {
            return GpxInformation.NULL;
        }

        public EditorInterface getDraftEditor() {
            return EditorInterface.NULL;
        }

        public EditorInterface getOverlayEditor() {
            return EditorInterface.NULL;
        }

        @Override
        public void close() {}
        
    };

    
    public class SelfOn extends Self {
        private final GpxObjectEditable draft;
        private GpxObjectEditable overlay;
        
        public SelfOn () throws SecurityException, ServiceNotUpException, IOException {
            draft=GpxObjectEditable.loadEditor(getServiceContext(), 
                    AppDirectory.getEditorDraft(EditorService.this).getAbsolutePath(), 
                    GpxInformation.ID.INFO_ID_EDITOR_DRAFT);
        }
        

        @Override
        public void editOverlay(File file) {
            freeOverlay();
            overlay = GpxObjectEditable.loadEditor(
                    getServiceContext(), 
                    file.getAbsolutePath(), 
                    GpxInformation.ID.INFO_ID_EDITOR_OVERLAY);


        }

        
         
        private void freeOverlay() {
            if (overlay != null)
                overlay.free();
            overlay = null;
        }

        
        
        @Override
        public void close() {
            if (getDraftEditor().isModified()) { 
                getDraftEditor().save();
            }
            freeOverlay();
            draft.free();
        }

        @Override
        public GpxInformation getOverlayInformation() {
            if (overlay != null) 
                return overlay.editor;
            return super.getOverlayInformation();
        }

        @Override
        public GpxInformation getDraftInformation() {
            if (draft != null) 
                return draft.editor;
            return super.getDraftInformation();
        }

        @Override
        public EditorInterface getDraftEditor() {
            if (draft != null)
                return draft.editor;
        
            return super.getDraftEditor();
        }

        public EditorInterface getOverlayEditor() {
            if (overlay != null)
                return overlay.editor;
        
            return super.getOverlayEditor();
        }
    };
    
    
    
    
    @Override
    public void onCreate() {
        super.onCreate();
        
        connectToServices(SERVICES);
    }
    

    @Override
    public void onServicesUp() {
        try {
            self = new SelfOn();
        } catch (Exception e) {
            AppLog.e(EditorService.this, e);
        }
    }
    
    
    public void editOverlay(File file) {
        self.editOverlay(file);
    }

    
    public EditorInterface getDraftEditor() {
        return self.getDraftEditor();
    }
    
    
    
    public GpxInformation getDraftIntormation() {
        return self.getDraftInformation();
    }
    
    
    public GpxInformation getOverlayInformation() {
        return self.getOverlayInformation();
    }
    
    
    @Override
    public void onDestroy() {
        self.close();
        self = new Self();
        super.onDestroy();
    }

    
    public final OverlayEditor editor=new OverlayEditor();
    public class OverlayEditor implements EditorInterface {

        @Override
        public void save() {
            self.getOverlayEditor().save();
        }

        @Override
        public void toggle() {
            self.getOverlayEditor().toggle();
        }

        @Override
        public void remove() {
            self.getOverlayEditor().remove();
        }

        @Override
        public void add(GpxPoint point) {
            self.getOverlayEditor().add(point);
        }

        @Override
        public void up() {
            self.getOverlayEditor().up();
        }

        @Override
        public void down() {
            self.getOverlayEditor().down();
        }

        @Override
        public boolean isModified() {
            return self.getOverlayEditor().isModified();
        }

        @Override
        public GpxPointNode getSelected() {
            return self.getOverlayEditor().getSelected();
        }

        @Override
        public void select(GpxPointNode p) {
            self.getOverlayEditor().select(p);
        }

        @Override
        public void saveAs() {
            self.getOverlayEditor().saveAs();
        }

        @Override
        public void clear() {
            self.getOverlayEditor().clear();
            
        }

        @Override
        public void redo() {
            self.getOverlayEditor().redo();
            
        }

        @Override
        public void undo() {
            self.getOverlayEditor().undo();
            
        }

        @Override
        public void discard() {
            self.getOverlayEditor().discard();
        }
        
    }
}

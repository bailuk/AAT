package ch.bailu.aat.views.map.overlay.control;

import org.osmdroid.api.IGeoPoint;

import android.view.View;
import ch.bailu.aat.R;
import ch.bailu.aat.gpx.GpxInformation;
import ch.bailu.aat.gpx.GpxPoint;
import ch.bailu.aat.helpers.AppLayout;
import ch.bailu.aat.helpers.ToolTip;
import ch.bailu.aat.preferences.SolidMapGrid;
import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat.services.editor.EditorInterface;
import ch.bailu.aat.views.ControlBar;
import ch.bailu.aat.views.map.OsmInteractiveView;
import ch.bailu.aat.views.map.overlay.MapPainter;
import ch.bailu.aat.views.map.overlay.OsmOverlay;
import ch.bailu.aat.views.map.overlay.editor.EditorNodeSelectorOverlay;
import ch.bailu.aat.views.map.overlay.gpx.GpxDynOverlay;

public class EditorOverlay extends ControlBarOverlay {
    private final SolidMapGrid sgrid;
    
    private final View  add, remove, up, down, 
                        save, saveAs, toggle, clear, 
                        undo, redo;
//    private final EditorInterface editor;
    
    private final EditorNodeSelectorOverlay selector;
    private final OsmOverlay content;
    //private final OsmOverlay legend;
    
    private OsmOverlay coordinates;
    
//    private final ElevationProvider elevation;
    
    private final ServiceContext scontext;
    
    
    public EditorOverlay(OsmInteractiveView osm, ServiceContext sc, int id) {
        super(osm, new ControlBar(
                osm.getContext(),
                AppLayout.getOrientationAlongLargeSide(osm.getContext())));

        scontext=sc;

        sgrid = new SolidMapGrid(osm.getContext(), osm.solidKey);
        coordinates = sgrid.createCenterCoordinatesOverlay(getOsmView());
        
        content = new GpxDynOverlay(osm, sc, id);
        //legend = new GpxLegendOverlay(osm,id, new PointIndexWalker());
        selector = new EditorNodeSelectorOverlay(osm, id, sc);
        
        
        ControlBar bar = getBar();
        
        add = bar.addImageButton(R.drawable.list_add);
        ToolTip.set(add, R.string.tt_edit_add);
        
        remove = bar.addImageButton(R.drawable.list_remove);
        ToolTip.set(remove, R.string.tt_edit_remove);
        
        up = bar.addImageButton(R.drawable.go_up);
        ToolTip.set(up, R.string.tt_edit_up);
        
        down = bar.addImageButton(R.drawable.go_down);
        ToolTip.set(down, R.string.tt_edit_down);
        
        toggle = bar.addImageButton(R.drawable.gtk_convert);
        ToolTip.set(toggle, R.string.tt_edit_convert);
        
        clear = bar.addImageButton(R.drawable.edit_clear_all);
        ToolTip.set(clear, R.string.tt_edit_clear);
        
        redo = bar.addImageButton(R.drawable.edit_redo);
        ToolTip.set(redo, R.string.tt_edit_redo);
        
        undo = bar.addImageButton(R.drawable.edit_undo);
        ToolTip.set(undo, R.string.tt_edit_undo);
        
        save = bar.addImageButton(R.drawable.document_save);
        ToolTip.set(save, R.string.tt_edit_save);
        
        saveAs = bar.addImageButton(R.drawable.document_save_as);
        ToolTip.set(saveAs, R.string.tt_edit_save_as);
    }
    
    

    @Override
    public void draw(MapPainter p) {
        content.draw(p);
        if (isVisible()) {
            //legend.draw(p);
            selector.draw(p);
            coordinates.draw(p);
        }
    }

    
    @Override
    public void onClick(View v) {
        super.onClick(v);
        
        final EditorInterface editor = scontext.getEditorService().getDraftEditor();
        
             if (v==save)    editor.save();
        else if (v==saveAs)  editor.saveAs();
        else if (v==add)    {
            IGeoPoint p = getMapView().getBoundingBox(). getCenter(); 
            editor.add(new GpxPoint(p, scontext.getElevationService().getElevation(p.getLatitudeE6(), p.getLongitudeE6()), 0));
        }
        else if (v==remove) editor.remove();
        
        else if (v==up)      editor.up();
        else if (v==down)    editor.down();
        else if (v==toggle)  editor.toggle();
        else if (v==clear)   editor.clear();
        else if (v==undo)    editor.undo();
        else if (v==redo)    editor.redo();
    }
    
    
    @Override
    public void leftTab() {
        showBar();
    }
    
    
    @Override
    public void showBar() {
        showBarAtLeft();
        selector.showAtRight();
    }

    @Override
    public void hideBar() {
        super.hideBar();
        selector.hide();
    }
    
    
    @Override
    public void updateGpxContent(GpxInformation info) {
        content.updateGpxContent(info);
        selector.updateGpxContent(info);
        //legend.updateGpxContent(info);
    }
    
    
    @Override
    public void onSharedPreferenceChanged(String key) {
        if (sgrid.hasKey(key)) {
            coordinates = sgrid.createCenterCoordinatesOverlay(getOsmView());
        } else {
            content.onSharedPreferenceChanged(key);
        }
        
    }
    
    
    @Override
    public void run() {}
}

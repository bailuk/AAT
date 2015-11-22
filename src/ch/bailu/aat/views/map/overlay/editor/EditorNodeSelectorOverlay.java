package ch.bailu.aat.views.map.overlay.editor;

import ch.bailu.aat.gpx.GpxInformation;
import ch.bailu.aat.gpx.GpxPointNode;
import ch.bailu.aat.services.editor.EditorInterface;
import ch.bailu.aat.views.map.OsmInteractiveView;
import ch.bailu.aat.views.map.overlay.gpx.InfoViewNodeSelectorOverlay;

public class EditorNodeSelectorOverlay extends InfoViewNodeSelectorOverlay {
    //private static final String MODIFIED_MARKER = "*";
    private final EditorInterface editor;

    
    public EditorNodeSelectorOverlay(OsmInteractiveView v, int id, EditorInterface e) {
        super(v, id);
        editor=e;
    }


    
    @Override
    public GpxPointNode getSelectedNode() {
        return editor.getSelected();
    }

    
    
    
    @Override
    public void setSelectedNode(GpxInformation info, GpxPointNode node, int index) {
        editor.select(node);
        super.setSelectedNode(info,node, index);
    }




}

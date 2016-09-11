package ch.bailu.aat.views.map.overlay.editor;

import ch.bailu.aat.gpx.GpxInformation;
import ch.bailu.aat.gpx.GpxPointNode;
import ch.bailu.aat.services.editor.EditorHelper;
import ch.bailu.aat.views.map.OsmInteractiveView;
import ch.bailu.aat.views.map.overlay.gpx.InfoViewNodeSelectorOverlay;

public class EditorNodeSelectorOverlay extends InfoViewNodeSelectorOverlay {

    private final EditorHelper edit;

    public EditorNodeSelectorOverlay(OsmInteractiveView v, int iid, EditorHelper e) {
        super(v, iid);
        edit=e;
    }



    @Override
    public GpxPointNode getSelectedNode() {
        return edit.getEditor().getSelected();
    }




    @Override
    public void setSelectedNode(GpxInformation info, GpxPointNode node, int index) {
        edit.getEditor().select(node);
        super.setSelectedNode(info,node, index);
    }




}

package ch.bailu.aat.views.map.overlay.editor;

import ch.bailu.aat.gpx.GpxInformation;
import ch.bailu.aat.gpx.GpxPointNode;
import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat.views.map.OsmInteractiveView;
import ch.bailu.aat.views.map.overlay.gpx.InfoViewNodeSelectorOverlay;

public class EditorNodeSelectorOverlay extends InfoViewNodeSelectorOverlay {

    private final ServiceContext scontext;


    public EditorNodeSelectorOverlay(OsmInteractiveView v, int id, ServiceContext sc) {
        super(v, id);
        scontext = sc;
    }



    @Override
    public GpxPointNode getSelectedNode() {
        return scontext.getEditorService().getEditor(GpxInformation.ID.INFO_ID_EDITOR_DRAFT).getSelected();
    }




    @Override
    public void setSelectedNode(GpxInformation info, GpxPointNode node, int index) {
        scontext.getEditorService().getEditor(GpxInformation.ID.INFO_ID_EDITOR_DRAFT).select(node);
        super.setSelectedNode(info,node, index);
    }




}

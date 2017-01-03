package ch.bailu.aat.map.osm.overlay.editor;

import android.view.View;

import ch.bailu.aat.gpx.GpxInformation;
import ch.bailu.aat.gpx.GpxPointNode;
import ch.bailu.aat.util.HtmlBuilderGpx;
import ch.bailu.aat.services.editor.EditorHelper;
import ch.bailu.aat.map.osm.OsmInteractiveView;
import ch.bailu.aat.map.osm.overlay.gpx.NodeViewOverlay;

public class EditorNodeSelectorOverlay extends NodeViewOverlay {

    private final EditorHelper edit;

    public EditorNodeSelectorOverlay(OsmInteractiveView v, EditorHelper e) {
        super(v);
        edit=e;
    }



    @Override
    public GpxPointNode getSelectedNode() {
        return edit.getEditor().getSelected();
    }




    @Override
    public void setSelectedNode(GpxInformation info, GpxPointNode node, int index) {
        edit.getEditor().select(node);

        HtmlBuilderGpx h = new HtmlBuilderGpx(getContext());

        h.appendInfo(info, index);
        h.appendNode(node, info);
        setHtmlText(h.toString());
    }


    @Override
    public boolean onLongClick(View v) {
        return false;
    }
}

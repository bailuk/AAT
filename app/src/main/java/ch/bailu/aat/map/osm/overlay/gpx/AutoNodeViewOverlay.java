package ch.bailu.aat.map.osm.overlay.gpx;

import android.view.View;

import java.io.File;

import ch.bailu.aat.activities.NodeDetailActivity;
import ch.bailu.aat.gpx.GpxInformation;
import ch.bailu.aat.gpx.GpxPointNode;
import ch.bailu.aat.util.HtmlBuilderGpx;
import ch.bailu.aat.map.osm.OsmInteractiveView;


public class AutoNodeViewOverlay extends NodeViewOverlay {

    final HtmlBuilderGpx html = new HtmlBuilderGpx(getContext());

    private String fileID = null;
    private int index = 0;

    public AutoNodeViewOverlay(OsmInteractiveView v) {
        super(v);
    }

    @Override
    public boolean onLongClick(View v) {
        if (fileID != null && new File(fileID).isFile()) {

            NodeDetailActivity.start(getContext(), fileID, index);
            return true;
        }
        return false;
    }


    @Override
    public void setSelectedNode(GpxInformation info, GpxPointNode node, int i) {
        index = i;
        fileID = info.getPath();

        html.clear();

        html.appendInfo(info, index);
        html.appendNode(node, info);
        html.appendAttributes(node.getAttributes());

        setHtmlText(html.toString());

    }
}

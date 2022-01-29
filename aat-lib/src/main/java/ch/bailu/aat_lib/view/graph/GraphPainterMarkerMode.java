package ch.bailu.aat_lib.view.graph;

import ch.bailu.aat_lib.gpx.GpxPointNode;
import ch.bailu.aat_lib.gpx.GpxSegmentNode;

public class GraphPainterMarkerMode extends GraphPainter {

    public GraphPainterMarkerMode(GraphPlotter p, int md) {
        super(p, md);
    }


    @Override
    public boolean doMarker(GpxSegmentNode marker) {
        if (marker.getFirstNode() != null) {
            plotIfDistance((GpxPointNode)marker.getFirstNode());
            incrementSummaryDistance(marker.getDistance());
        }
        return false;
    }
}

package ch.bailu.aat_lib.view.graph;

import ch.bailu.aat_lib.gpx.GpxPointNode;
import ch.bailu.aat_lib.gpx.GpxSegmentNode;

public class ScaleGeneratorMarkerMode extends ScaleGenerator {

    public ScaleGeneratorMarkerMode(GraphPlotter p) {
        super(p);
    }

    @Override
    public boolean doMarker(GpxSegmentNode marker) {
        GpxPointNode point = (GpxPointNode) marker.getFirstNode();
        if (point != null) {
            doPoint(point);
        }
        return false;
    }
}

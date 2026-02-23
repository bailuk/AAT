package ch.bailu.aat_lib.view.graph;

import ch.bailu.aat_lib.gpx.GpxList;
import ch.bailu.aat_lib.gpx.GpxListWalker;
import ch.bailu.aat_lib.gpx.GpxPointNode;
import ch.bailu.aat_lib.gpx.GpxSegmentNode;
import ch.bailu.aat_lib.map.MapColor;

public class SegmentNodePainter extends GpxListWalker {

    private float distance;

    private final GraphPlotter plotter;

    public SegmentNodePainter(GraphPlotter p, float offset) {
        plotter = p;
        distance = 0f - offset;

    }

    @Override
    public boolean doList(GpxList track) {
        return true;
    }

    @Override
    public boolean doSegment(GpxSegmentNode segment) {
        if (segment.getSegmentSize() > 0 && distance > 0f) {
            GpxPointNode node = (GpxPointNode) segment.getFirstNode();
            plotPoint(node, distance + node.getDistance());
        }

        distance += segment.getDistance();
        return false;

    }

    @Override
    public boolean doMarker(GpxSegmentNode marker) {
        return false;
    }


    @Override
    public void doPoint(GpxPointNode point) {}

    private void plotPoint(GpxPointNode point, float distance) {
        plotter.plotPoint(distance, (float) point.getAltitude(),
                MapColor.NODE_NEUTRAL);
    }
}
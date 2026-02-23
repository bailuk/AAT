package ch.bailu.aat_lib.view.graph;

import ch.bailu.aat_lib.gpx.GpxList;
import ch.bailu.aat_lib.gpx.GpxListWalker;
import ch.bailu.aat_lib.gpx.GpxPointNode;
import ch.bailu.aat_lib.gpx.GpxSegmentNode;
import ch.bailu.aat_lib.map.MapColor;

public class IndexPainter extends GpxListWalker {

    private float distance = 0f;
    private final float offset;
    private int index = 0;

    private final int nodeIndex;
    private final GraphPlotter plotter;

    public IndexPainter(GraphPlotter p, int n, float offset) {
        nodeIndex = n;
        plotter = p;
        this.offset = offset;
    }

    @Override
    public boolean doList(GpxList track) {
        return true;
    }

    @Override
    public boolean doSegment(GpxSegmentNode segment) {
        return doDelta(segment);
    }

    @Override
    public boolean doMarker(GpxSegmentNode marker) {
        return doDelta(marker);
    }


    private boolean doDelta(GpxSegmentNode segment) {
        if (index + segment.getSegmentSize() <= nodeIndex) {
            index += segment.getSegmentSize();
            distance += segment.getDistance();
            return false;
        }
        return true;
    }

    @Override
    public void doPoint(GpxPointNode point) {
        if (index == nodeIndex) {
            distance += point.getDistance();
            plotPoint(point, distance - offset);
            index++;

        } else if (index < nodeIndex) {
            distance += point.getDistance();
            index++;
        }
    }

    private void plotPoint(GpxPointNode point, float distance) {
        plotter.plotPoint(distance, (float) point.getAltitude(),
                MapColor.NODE_SELECTED);
    }
}
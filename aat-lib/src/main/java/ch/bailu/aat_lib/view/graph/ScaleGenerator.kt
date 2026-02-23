package ch.bailu.aat_lib.view.graph;

import ch.bailu.aat_lib.gpx.GpxList;
import ch.bailu.aat_lib.gpx.GpxListWalker;
import ch.bailu.aat_lib.gpx.GpxPointNode;
import ch.bailu.aat_lib.gpx.GpxSegmentNode;

public class ScaleGenerator extends GpxListWalker {
    private final GraphPlotter plotter;

    public ScaleGenerator(GraphPlotter p) {
        plotter=p;
    }

    @Override
    public void doPoint(GpxPointNode point) {
        plotter.inlcudeInYScale((float)point.getAltitude());
    }


    @Override
    public boolean doMarker(GpxSegmentNode marker) {
        return true;
    }

    @Override
    public boolean doSegment(GpxSegmentNode segment) {
        return true;
    }

    @Override
    public boolean doList(GpxList track) {
        return true;
    }
}

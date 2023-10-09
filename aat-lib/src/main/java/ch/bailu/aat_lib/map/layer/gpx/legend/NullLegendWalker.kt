package ch.bailu.aat_lib.map.layer.gpx.legend;

import ch.bailu.aat_lib.gpx.GpxPointNode;
import ch.bailu.aat_lib.gpx.GpxSegmentNode;

public final class NullLegendWalker extends LegendWalker {
    @Override
    public boolean doMarker(GpxSegmentNode marker) {
        return false;
    }

    @Override
    public void doPoint(GpxPointNode point) {

    }
}

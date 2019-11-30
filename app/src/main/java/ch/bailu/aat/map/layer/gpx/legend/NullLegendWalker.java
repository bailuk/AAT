package ch.bailu.aat.map.layer.gpx.legend;

import ch.bailu.aat.gpx.GpxPointNode;
import ch.bailu.aat.gpx.GpxSegmentNode;

public final class NullLegendWalker extends LegendWalker {
    @Override
    public boolean doMarker(GpxSegmentNode marker) {
        return false;
    }

    @Override
    public void doPoint(GpxPointNode point) {

    }
}

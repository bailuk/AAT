package ch.bailu.aat.map.layer.gpx.legend;

import ch.bailu.aat_lib.gpx.GpxList;
import ch.bailu.aat_lib.gpx.GpxPointNode;
import ch.bailu.aat_lib.gpx.GpxSegmentNode;

public final class PointIndexWalker extends LegendWalker{
    private int index=1;

    @Override
    public boolean doList(GpxList l) {
        index=1;
        return super.doList(l);
    }

    @Override
    public boolean doMarker(GpxSegmentNode marker) {
        return c.isVisible(marker.getBoundingBox());
    }

    @Override
    public void doPoint(GpxPointNode point) {
        c.nodes.nodeB.set(point);

        if (!c.arePointsTooClose()) {
            drawLegendFromB();
            c.nodes.switchNodes();

        }
        index++;
    }

    private void drawLegendFromB() {
        if (c.isBVisible()) {
            c.drawLabelB(String.valueOf(index));
        }
    }
}

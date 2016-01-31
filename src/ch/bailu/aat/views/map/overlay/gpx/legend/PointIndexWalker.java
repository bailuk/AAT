package ch.bailu.aat.views.map.overlay.gpx.legend;

import ch.bailu.aat.gpx.GpxList;
import ch.bailu.aat.gpx.GpxPointNode;
import ch.bailu.aat.gpx.GpxSegmentNode;

public class PointIndexWalker extends LegendWalker {
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
        if (c.isVisible(c.nodes.nodeB)) {
            c.drawLegend(c.nodes.nodeB, String.valueOf(index));
        }
    }
}

package ch.bailu.aat.mapsforge.layer.gpx.legend;

import android.content.Context;

import ch.bailu.aat.description.DistanceDescription;
import ch.bailu.aat.gpx.GpxList;
import ch.bailu.aat.gpx.GpxPointNode;
import ch.bailu.aat.gpx.GpxSegmentNode;

public class PointDistanceWalker extends LegendWalker {

    private final DistanceDescription description;
    private float distance=0;
    private final boolean resetAfterDraw;

    public PointDistanceWalker(Context context, boolean r) {
        resetAfterDraw=r;
        description = new DistanceDescription(context);
    }

    @Override
    public boolean doList(GpxList l) {
        distance=0;
        return super.doList(l);
    }

    @Override
    public boolean doMarker(GpxSegmentNode marker) {
        if (c.isVisible(marker.getBoundingBox())) {
            distance -= ((GpxPointNode)marker.getFirstNode()).getDistance();
            return true;
        } else {
            distance+=marker.getDistance();
            return false;
        }
    }

    @Override
    public void doPoint(GpxPointNode point) {
        c.nodes.nodeB.set(point);

        distance += point.getDistance();

        if (!c.arePointsTooClose()) {
            drawLegendFromB();

            c.nodes.switchNodes();

            if (resetAfterDraw)
                distance=0;
        }
    }


    private void drawLegendFromB() {
        if (c.isVisible(c.nodes.nodeB)) {
            c.drawLegend(c.nodes.nodeB, description.getDistanceDescription(distance));
        }
    }
}

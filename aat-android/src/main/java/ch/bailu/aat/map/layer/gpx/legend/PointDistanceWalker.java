package ch.bailu.aat.map.layer.gpx.legend;

import android.content.Context;

import ch.bailu.aat_lib.description.DistanceDescription;
import ch.bailu.aat.preferences.Storage;
import ch.bailu.aat_lib.gpx.GpxList;
import ch.bailu.aat_lib.gpx.GpxPointNode;
import ch.bailu.aat_lib.gpx.GpxSegmentNode;

public final class PointDistanceWalker extends LegendWalker {

    private final DistanceDescription description;
    private float distance=0;
    private final boolean resetAfterDraw;

    public PointDistanceWalker(Context context, boolean r) {
        resetAfterDraw=r;
        description = new DistanceDescription(new Storage(context));
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
        c.setB(point);

        distance += point.getDistance();

        if (!c.arePointsTooClose()) {
            drawLegendFromB();

            c.switchNodes();

            if (resetAfterDraw)
                distance=0;
        }
    }


    private void drawLegendFromB() {
        if (c.isBVisible()) {
            c.drawLabelB(description.getDistanceDescriptionN1(distance));
        }
    }
}

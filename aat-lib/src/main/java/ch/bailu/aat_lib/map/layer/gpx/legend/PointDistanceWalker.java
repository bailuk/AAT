package ch.bailu.aat_lib.map.layer.gpx.legend;

import ch.bailu.aat_lib.description.DistanceDescription;
import ch.bailu.aat_lib.gpx.GpxList;
import ch.bailu.aat_lib.gpx.GpxPointNode;
import ch.bailu.aat_lib.gpx.GpxSegmentNode;
import ch.bailu.aat_lib.preferences.StorageInterface;

public final class PointDistanceWalker extends LegendWalker {

    private final DistanceDescription description;
    private float distance=0;
    private final boolean resetAfterDraw;

    public PointDistanceWalker(StorageInterface storage, boolean r) {
        resetAfterDraw=r;
        description = new DistanceDescription(storage);
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

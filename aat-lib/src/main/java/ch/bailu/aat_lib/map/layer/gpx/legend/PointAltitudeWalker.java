package ch.bailu.aat_lib.map.layer.gpx.legend;

import ch.bailu.aat_lib.description.DistanceDescription;
import ch.bailu.aat_lib.gpx.GpxPointNode;
import ch.bailu.aat_lib.gpx.GpxSegmentNode;
import ch.bailu.aat_lib.preferences.StorageInterface;

public final class PointAltitudeWalker extends LegendWalker {
    private final DistanceDescription description;

    public PointAltitudeWalker(StorageInterface storage) {
        description = new DistanceDescription(storage);
    }


    @Override
    public boolean doMarker(GpxSegmentNode marker) {
        return c.isVisible(marker.getBoundingBox());
    }

    @Override
    public void doPoint(GpxPointNode point) {
        c.setB(point);

        if (!c.arePointsTooClose()) {
            drawLegendFromB();
            c.switchNodes();

        }
    }

    private void drawLegendFromB() {
        if (c.isBVisible()) {
            c.drawLabelB(description.getAltitudeDescription(c.nodes.nodeB.point.getAltitude()));
        }
    }
}

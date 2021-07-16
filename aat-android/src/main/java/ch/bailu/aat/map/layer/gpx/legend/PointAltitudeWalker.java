package ch.bailu.aat.map.layer.gpx.legend;

import android.content.Context;

import ch.bailu.aat.description.DistanceDescription;
import ch.bailu.aat.gpx.GpxPointNode;
import ch.bailu.aat.gpx.GpxSegmentNode;

public final class PointAltitudeWalker extends LegendWalker {
    private final DistanceDescription description;

    public PointAltitudeWalker(Context context) {
        description = new DistanceDescription(context);
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

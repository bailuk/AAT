package ch.bailu.aat.map.layer.gpx.legend;

import android.content.Context;

import ch.bailu.aat_lib.description.DistanceDescription;
import ch.bailu.aat.preferences.Storage;
import ch.bailu.aat_lib.gpx.GpxPointNode;
import ch.bailu.aat_lib.gpx.GpxSegmentNode;

public final class PointAltitudeWalker extends LegendWalker {
    private final DistanceDescription description;

    public PointAltitudeWalker(Context context) {
        description = new DistanceDescription(new Storage(context));
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

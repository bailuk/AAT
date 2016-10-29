package ch.bailu.aat.views.map.overlay.gpx.legend;

import android.content.Context;

import ch.bailu.aat.description.DistanceDescription;
import ch.bailu.aat.gpx.GpxPointNode;
import ch.bailu.aat.gpx.GpxSegmentNode;

public class PointAltitudeWalker  extends LegendWalker {

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
        c.nodes.nodeB.set(point);

        if (!c.arePointsTooClose()) {
            drawLegendFromB();
            c.nodes.switchNodes();

        }
    }
    
    private void drawLegendFromB() {
        if (c.isVisible(c.nodes.nodeB)) {
            c.drawLegend(c.nodes.nodeB, description.getAltitudeDescription(c.nodes.nodeB.point.getAltitude()));
        }
    }
}

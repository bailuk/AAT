package ch.bailu.aat.map.layer.gpx.legend;

import android.content.Context;

import ch.bailu.aat_lib.description.DistanceDescription;
import ch.bailu.aat.preferences.Storage;
import ch.bailu.aat_lib.gpx.GpxPointNode;
import ch.bailu.aat_lib.gpx.GpxSegmentNode;

public final class MarkerAltitudeWalker extends LegendWalker {

    private final DistanceDescription description;

    public MarkerAltitudeWalker(Context context) {
        description = new DistanceDescription(new Storage(context));
    }


    @Override
    public boolean doMarker(GpxSegmentNode marker) {
        boolean isLast = (marker.getNext() == null);

        if (!isLast) {
            c.nodes.nodeB.set((GpxPointNode)marker.getFirstNode());

            drawLegendFromB();

            if (!c.arePointsTooClose()) {
                c.nodes.switchNodes();
            }

        }

        return isLast;
    }


    @Override
    public void doPoint(GpxPointNode point) {
        if (point.getNext()==null) {
            c.nodes.nodeB.set(point);
            drawLegendFromB();

        }
    }

    private void drawLegendFromB() {
        if (c.isBVisible()) {
            if (!c.arePointsTooClose()) {
                c.drawNodeB();
                c.drawLabelB(description.getAltitudeDescription(c.nodes.nodeB.point.getAltitude()));
            }
        }
    }
}

package ch.bailu.aat.map.osm.overlay.gpx.legend;

import android.content.Context;

import ch.bailu.aat.description.DistanceDescription;
import ch.bailu.aat.gpx.GpxPointNode;
import ch.bailu.aat.gpx.GpxSegmentNode;

public class MarkerAltitudeWalker extends LegendWalker {


    private final DistanceDescription description;

    public MarkerAltitudeWalker(Context context) {
        description = new DistanceDescription(context);
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
        if (c.isVisible(c.nodes.nodeB)) {
            c.drawNode(c.nodes.nodeB);
            if (!c.arePointsTooClose()) {
                c.drawLegend(c.nodes.nodeB, description.getAltitudeDescription(c.nodes.nodeB.point.getAltitude()));
            }
        }
    }
}

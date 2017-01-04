package ch.bailu.aat.map.osmdroid.overlay.gpx.legend;

import android.content.Context;

import ch.bailu.aat.description.DistanceDescription;
import ch.bailu.aat.gpx.GpxList;
import ch.bailu.aat.gpx.GpxPointNode;
import ch.bailu.aat.gpx.GpxSegmentNode;

public class MarkerDistanceWalker extends LegendWalker {
    private final boolean resetAfterDraw;
    private float distance=0;
    private final DistanceDescription description;
    
    
    public MarkerDistanceWalker(Context context, boolean reset) {
        resetAfterDraw=reset;
        description=new DistanceDescription(context);
    }



    
    @Override
    public boolean doList(GpxList l) {
        if (super.doList(l)) {
            distance=0;
            c.nodes.nodeA.set((GpxPointNode)l.getPointList().getFirst());
            c.drawNodeIfVisible(c.nodes.nodeA);
            
            return true;
        }
        return false;
    }

    @Override
    public boolean doMarker(GpxSegmentNode marker) {
        boolean isLast = marker.getNext() == null;

        if (!isLast) {
            c.nodes.nodeB.set((GpxPointNode)marker.getFirstNode());
            drawLegendFromB();
            
            if (!c.arePointsTooClose()) {
                c.nodes.switchNodes();
                if (resetAfterDraw) distance=0;
            }

        }
        distance += marker.getDistance();
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
            if (!c.arePointsTooClose()) {
                c.drawNode(c.nodes.nodeB);
                c.drawLegend(c.nodes.nodeB, description.getDistanceDescription(distance));
            }
        }
    }

}

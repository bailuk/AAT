package ch.bailu.aat_lib.map.layer.gpx.legend;

import ch.bailu.aat_lib.description.DistanceDescription;
import ch.bailu.aat_lib.gpx.GpxList;
import ch.bailu.aat_lib.gpx.GpxPointNode;
import ch.bailu.aat_lib.gpx.GpxSegmentNode;
import ch.bailu.aat_lib.preferences.StorageInterface;

public final class MarkerDistanceWalker extends LegendWalker {
    private final boolean resetAfterDraw;
    private float distance=0;
    private final DistanceDescription description;


    public MarkerDistanceWalker(StorageInterface storage, boolean reset) {
        resetAfterDraw=reset;
        description=new DistanceDescription(storage);
    }




    @Override
    public boolean doList(GpxList l) {
        if (super.doList(l)) {
            distance=0;
            c.setA((GpxPointNode)l.getPointList().getFirst());

            if (c.isAVisible())
                c.drawNodeA();


            return true;
        }
        return false;
    }

    @Override
    public boolean doMarker(GpxSegmentNode marker) {
        boolean isLast = marker.getNext() == null;

        if (!isLast) {
            c.setB((GpxPointNode)marker.getFirstNode());
            drawLegendFromB();

            if (!c.arePointsTooClose()) {
                c.switchNodes();
                if (resetAfterDraw) distance=0;
            }

        }
        distance += marker.getDistance();
        return isLast;
    }


    @Override
    public void doPoint(GpxPointNode point) {
        if (point.getNext()==null) {
            c.setB(point);
            drawLegendFromB();
        }
    }


    private void drawLegendFromB() {
        if (c.isBVisible()) {
            if (!c.arePointsTooClose()) {
                c.drawNodeB();
                c.drawLabelB(description.getDistanceDescriptionN1(distance));
            }
        }
    }


}

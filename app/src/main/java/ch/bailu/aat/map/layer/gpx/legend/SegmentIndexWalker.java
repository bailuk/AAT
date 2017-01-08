package ch.bailu.aat.map.layer.gpx.legend;

import ch.bailu.aat.gpx.GpxList;
import ch.bailu.aat.gpx.GpxPointNode;
import ch.bailu.aat.gpx.GpxSegmentNode;

public class SegmentIndexWalker extends LegendWalker{

    private int index=1;

    @Override
    public boolean doList(GpxList l) {
        index=1;
        return super.doList(l);
    }


    @Override
    public boolean doSegment(GpxSegmentNode segment) {
        c.nodes.nodeB.set((GpxPointNode)segment.getFirstNode());

        if (!c.arePointsTooClose() || index == 1) {
            drawLegendFromB();
            c.nodes.switchNodes();
        }
        index++;
        return segment.getNext()==null;
    }


    @Override
    public boolean doMarker(GpxSegmentNode marker) {
        return marker.getNext()==null;
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
            c.drawNodeB();
            if (!c.arePointsTooClose())
                c.drawLabelB(String.valueOf(index));
        }
    }
}

package ch.bailu.aat.views.map.overlay.gpx.legend;

import ch.bailu.aat.gpx.GpxList;
import ch.bailu.aat.gpx.GpxPointNode;
import ch.bailu.aat.gpx.GpxSegmentNode;

public class PointNameWalker extends LegendWalker {
    private static int LIMIT=20;
    private int index=1;
    private int displayed=0;
    
    
    @Override
    public boolean doList(GpxList l) {
        index=1;
        displayed=0;
        return super.doList(l);
    }

    @Override
    public boolean doMarker(GpxSegmentNode marker) {
        return (displayed < LIMIT) && c.isVisible(marker.getBoundingBox());
    }

    @Override
    public void doPoint(GpxPointNode point) {
        if (displayed < LIMIT) { 
            c.nodes.nodeB.set(point);

            if (c.arePointsTooClose()==false) {
                drawLegendFromB();
                c.nodes.switchNodes();

            }
            index++;
        }
    }
    
    private void drawLegendFromB() {
        if (c.isVisible(c.nodes.nodeB)) {
            c.drawLegend(c.nodes.nodeB, getNameFromB());
            displayed++;
        }
    }
    
    private String getNameFromB() {
        String name = c.nodes.nodeB.point.getAttributes().get("name");
        if (name==null) name = String.valueOf(index);
        return name;
    }

}

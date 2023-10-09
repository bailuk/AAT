package ch.bailu.aat_lib.map.layer.gpx.legend;


import ch.bailu.aat_lib.gpx.GpxList;
import ch.bailu.aat_lib.gpx.GpxPointNode;
import ch.bailu.aat_lib.gpx.GpxSegmentNode;
import ch.bailu.aat_lib.gpx.attributes.GpxAttributes;
import ch.bailu.aat_lib.gpx.attributes.Keys;

public final class PointNameWalker extends LegendWalker{
    private static final int LIMIT=20;
    private int displayed=0;


    @Override
    public boolean doList(GpxList l) {
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
            c.setB(point);

            if (c.arePointsTooClose()==false) {
                drawLegendFromB();
                c.switchNodes();

            }
        }
    }

    private void drawLegendFromB() {

        if (c.isBVisible()) {
            String name = getNameFromB();
            if (name != null) {
                c.drawLabelB(getNameFromB());
                displayed++;
            }
        }
    }

    private static final int KEY_INDEX_NAME = Keys.toIndex("name");
    private String getNameFromB() {
        final GpxAttributes attr = c.nodes.nodeB.point.getAttributes();

        if (attr.hasKey(KEY_INDEX_NAME))
            return attr.get(KEY_INDEX_NAME);

        return null;
    }
}

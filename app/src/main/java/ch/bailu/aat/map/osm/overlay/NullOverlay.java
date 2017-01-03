package ch.bailu.aat.map.osm.overlay;

import ch.bailu.aat.map.osm.AbsOsmView;


public class NullOverlay extends OsmOverlay {

    public NullOverlay(AbsOsmView map) {
        super(map);
    }

    @Override
    public void draw(MapPainter p) {}


}

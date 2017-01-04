package ch.bailu.aat.map.osmdroid.overlay;

import ch.bailu.aat.map.osmdroid.AbsOsmView;


public class NullOverlay extends OsmOverlay {

    public NullOverlay(AbsOsmView map) {
        super(map);
    }

    @Override
    public void draw(MapPainter p) {}


}

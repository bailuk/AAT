package ch.bailu.aat.views.map.overlay;

import ch.bailu.aat.views.map.AbsOsmView;


public class NullOverlay extends OsmOverlay {

    public NullOverlay(AbsOsmView map) {
        super(map);
    }

    @Override
    public void draw(MapPainter p) {}


}

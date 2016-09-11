package ch.bailu.aat.views.map.overlay;

import ch.bailu.aat.views.map.OsmInteractiveView;

public class ZoomLevelOverlay extends OsmOverlay {

    public ZoomLevelOverlay(OsmInteractiveView v) {
        super(v);
    }

    @Override
    public void draw(MapPainter p) {
        int z = getMapView().getZoomLevel();

        
        p.canvas.drawTextTop(String.format("Zoomlevel*: %d", z) ,2);

    }

}

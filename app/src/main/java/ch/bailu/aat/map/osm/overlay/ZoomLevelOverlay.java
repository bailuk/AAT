package ch.bailu.aat.map.osm.overlay;

import java.util.Locale;

import ch.bailu.aat.map.osm.OsmInteractiveView;

public class ZoomLevelOverlay extends OsmOverlay {

    public ZoomLevelOverlay(OsmInteractiveView v) {
        super(v);
    }

    @Override
    public void draw(MapPainter p) {
        int z = getMapView().getZoomLevel();

        
        p.canvas.drawTextTop(String.format((Locale)null,"Zoomlevel*: %d", z) ,2);

    }

}

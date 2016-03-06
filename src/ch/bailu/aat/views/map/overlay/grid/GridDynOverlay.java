package ch.bailu.aat.views.map.overlay.grid;

import ch.bailu.aat.preferences.SolidMapGrid;
import ch.bailu.aat.services.dem.ElevationProvider;
import ch.bailu.aat.views.map.OsmInteractiveView;
import ch.bailu.aat.views.map.overlay.MapPainter;
import ch.bailu.aat.views.map.overlay.OsmOverlay;

public class GridDynOverlay extends OsmOverlay {

    private OsmOverlay gridOverlay;
    private SolidMapGrid    sgrid;
    
    private final ElevationProvider elevation;



    public GridDynOverlay(OsmInteractiveView osm, ElevationProvider e) {
        super(osm);
        
        elevation =e;
        sgrid = new SolidMapGrid(osm.getContext(), osm.solidKey);
        gridOverlay = sgrid.createGridOverlay(getOsmView(), elevation);
    }

    
    @Override
    public void draw(MapPainter p) {
        gridOverlay.draw(p);

    }


    @Override
    public void onSharedPreferenceChanged(String key) {
        if (sgrid.hasKey(key)) {
            gridOverlay = sgrid.createGridOverlay(getOsmView(), elevation);
            getMapView().invalidate();
        }
    }
}


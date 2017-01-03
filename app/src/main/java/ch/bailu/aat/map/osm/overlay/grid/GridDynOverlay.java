package ch.bailu.aat.map.osm.overlay.grid;

import ch.bailu.aat.preferences.SolidMapGrid;
import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat.map.osm.OsmInteractiveView;
import ch.bailu.aat.map.osm.overlay.MapPainter;
import ch.bailu.aat.map.osm.overlay.OsmOverlay;

public class GridDynOverlay extends OsmOverlay {

    private OsmOverlay gridOverlay;
    private final SolidMapGrid    sgrid;
    
    private final ServiceContext scontext;



    public GridDynOverlay(OsmInteractiveView osm, ServiceContext sc) {
        super(osm);
        
        scontext = sc;
        sgrid = new SolidMapGrid(osm.getContext(), osm.getSolidKey());
        gridOverlay = sgrid.createGridOverlay(getOsmView(), scontext);
    }

    
    @Override
    public void draw(MapPainter p) {
        gridOverlay.draw(p);

    }


    @Override
    public void onSharedPreferenceChanged(String key) {
        if (sgrid.hasKey(key)) {
            gridOverlay = sgrid.createGridOverlay(getOsmView(), scontext);
            getOsmView().requestRedraw();
        }
    }
}


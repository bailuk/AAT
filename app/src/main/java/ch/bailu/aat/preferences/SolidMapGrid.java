package ch.bailu.aat.preferences;

import android.content.Context;

import ch.bailu.aat.R;
import ch.bailu.aat.map.MapContext;
import ch.bailu.aat.map.layer.MapLayerInterface;
import ch.bailu.aat.map.layer.NullLayer;
import ch.bailu.aat.map.layer.grid.CH1903CenterCoordinatesLayer;
import ch.bailu.aat.map.layer.grid.CH1903GridLayer;
import ch.bailu.aat.map.layer.grid.UTMCenterCoordinatesLayer;
import ch.bailu.aat.map.layer.grid.UTMGridLayer;
import ch.bailu.aat.map.layer.grid.WGS84Layer;
import ch.bailu.aat.map.osmdroid.AbsOsmView;
import ch.bailu.aat.map.osmdroid.overlay.NullOverlay;
import ch.bailu.aat.map.osmdroid.overlay.OsmOverlay;
import ch.bailu.aat.map.osmdroid.overlay.grid.CH1903CenterCoordinatesOverlay;
import ch.bailu.aat.map.osmdroid.overlay.grid.CH1903GridOverlay;
import ch.bailu.aat.map.osmdroid.overlay.grid.UTMCenterCoordinatesOverlay;
import ch.bailu.aat.map.osmdroid.overlay.grid.UTMGridOverlay;
import ch.bailu.aat.map.osmdroid.overlay.grid.WGS84Overlay;
import ch.bailu.aat.services.ServiceContext;

public class SolidMapGrid extends SolidStaticIndexList {

    private static final String POSTFIX="_GRID";
    
    private static final String[] LABEL={"WGS84", "CH1903", "UTM", "None"};
    
    
    public SolidMapGrid(Context context, String k) {
        super(Storage.map(context), k + POSTFIX, LABEL);
    }

    public MapLayerInterface createCenterCoordinatesLayer() {
        if (this.getIndex()==1) {
            return new CH1903CenterCoordinatesLayer();
        }

        if (this.getIndex()==2) {
            return new UTMCenterCoordinatesLayer();
        }

        return new NullLayer();

    }

    public OsmOverlay createCenterCoordinatesOverlay(AbsOsmView osmPreview) {
        if (this.getIndex()==1) {
            return new CH1903CenterCoordinatesOverlay(osmPreview);
        }

        if (this.getIndex()==2) {
            return new UTMCenterCoordinatesOverlay(osmPreview);
        }
        
        return new NullOverlay(osmPreview);
    }
    
    public OsmOverlay createGridOverlay(AbsOsmView osmPreview, ServiceContext sc) {
        if (this.getIndex()==0) {
            return new WGS84Overlay(osmPreview, sc);
        }
        
        if (this.getIndex()==1) {
            return new CH1903GridOverlay(osmPreview);
        }

        if (this.getIndex()==2) {
            return new UTMGridOverlay(osmPreview);
        }
        
        return new NullOverlay(osmPreview);
    }

    
    @Override
    public int getIconResource() {
        return R.drawable.view_grid;
    }

    public MapLayerInterface createGridOverlay(MapContext cl) {
        if (this.getIndex()==0) {
            return new WGS84Layer(cl);
        }

        if (this.getIndex()==1) {
            return new CH1903GridLayer(cl);
        }

        if (this.getIndex()==2) {
            return new UTMGridLayer(cl);
        }

        return new NullLayer();
    }

}

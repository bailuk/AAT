package ch.bailu.aat.preferences;

import android.content.Context;

import ch.bailu.aat.R;
import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat.views.map.AbsOsmView;
import ch.bailu.aat.views.map.overlay.NullOverlay;
import ch.bailu.aat.views.map.overlay.OsmOverlay;
import ch.bailu.aat.views.map.overlay.grid.CH1903CenterCoordinatesOverlay;
import ch.bailu.aat.views.map.overlay.grid.CH1903GridOverlay;
import ch.bailu.aat.views.map.overlay.grid.UTMCenterCoordinatesOverlay;
import ch.bailu.aat.views.map.overlay.grid.UTMGridOverlay;
import ch.bailu.aat.views.map.overlay.grid.WGS84Overlay;

public class SolidMapGrid extends SolidStaticIndexList {

    private static final String POSTFIX="_GRID";
    
    private static final String[] LABEL={"WGS84", "CH1903", "UTM", "None"};
    
    
    public SolidMapGrid(Context context, String k) {
        super(Storage.map(context), k + POSTFIX, LABEL);
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
}

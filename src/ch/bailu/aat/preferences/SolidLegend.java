package ch.bailu.aat.preferences;

import android.content.Context;
import ch.bailu.aat.views.map.AbsOsmView;
import ch.bailu.aat.views.map.overlay.NullOverlay;
import ch.bailu.aat.views.map.overlay.OsmOverlay;
import ch.bailu.aat.views.map.overlay.gpx.GpxAltitudeLengendOverlay;
import ch.bailu.aat.views.map.overlay.gpx.GpxDistanceLegendOverlay;
import ch.bailu.aat.views.map.overlay.gpx.GpxSegmentIndexOverlay;
import ch.bailu.aat.views.map.overlay.gpx.GpxSpeedLegendOverlay;
import ch.bailu.aat.R;

public class SolidLegend extends SolidStaticIndexList {

    private static final String POSTFIX="_LEGEND";
    
    private static final String[] LABEL={
        "None*",
        "Distance continious*",
        "Distance*",        
        "Altitude*", 
        "Speed*",
        };
    
    public SolidLegend(Context context, String k) {
        super(Storage.map(context), k+POSTFIX, LABEL);
    }

    
    public OsmOverlay createTrackLegendOverlay(AbsOsmView osmPreview, int id) {
        if (getIndex()==0) return new GpxSegmentIndexOverlay(osmPreview,id);
        if (getIndex()==1) return new GpxDistanceLegendOverlay(osmPreview, id, false);
        if (getIndex()==2) return new GpxDistanceLegendOverlay(osmPreview, id, true);
        if (getIndex()==3) return new GpxAltitudeLengendOverlay(osmPreview, id);
        if (getIndex()==4) return new GpxSpeedLegendOverlay(osmPreview, id);
        
        return new NullOverlay(osmPreview);
    }
    
    
    public OsmOverlay createWayLegendOverlay(AbsOsmView osmPreview, int id) {
        return new NullOverlay(osmPreview);
    }

    
    public OsmOverlay createRouteLegendOverlay(AbsOsmView osmPreview, int id) {
        if (getIndex()==1 || getIndex()==2) 
            return createTrackLegendOverlay(osmPreview, id);
        return new NullOverlay(osmPreview);
    }

    
    @Override
    public int getImageResource() {
        return R.drawable.dialog_question;
    }
}

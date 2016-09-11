package ch.bailu.aat.preferences;

import android.content.Context;
import ch.bailu.aat.R;
import ch.bailu.aat.views.map.AbsOsmView;
import ch.bailu.aat.views.map.overlay.NullOverlay;
import ch.bailu.aat.views.map.overlay.OsmOverlay;
import ch.bailu.aat.views.map.overlay.gpx.legend.GpxLegendOverlay;
import ch.bailu.aat.views.map.overlay.gpx.legend.MarkerAltitudeWalker;
import ch.bailu.aat.views.map.overlay.gpx.legend.MarkerDistanceWalker;
import ch.bailu.aat.views.map.overlay.gpx.legend.MarkerSpeedWalker;
import ch.bailu.aat.views.map.overlay.gpx.legend.PointAltitudeWalker;
import ch.bailu.aat.views.map.overlay.gpx.legend.PointDistanceWalker;
import ch.bailu.aat.views.map.overlay.gpx.legend.PointIndexWalker;
import ch.bailu.aat.views.map.overlay.gpx.legend.PointNameWalker;
import ch.bailu.aat.views.map.overlay.gpx.legend.SegmentIndexWalker;

public class SolidLegend extends SolidStaticIndexList {

    private static final String POSTFIX="_LEGEND";
    
    public SolidLegend(Context context, String k) {
        super(Storage.map(context), k+POSTFIX, new String[] {
            context.getString(R.string.none),
            context.getString(R.string.p_legend_fulldistance),
            context.getString(R.string.distance),        
            context.getString(R.string.altitude), 
            context.getString(R.string.speed)
            });
        
    }

    
    public OsmOverlay createTrackLegendOverlay(AbsOsmView osmPreview, int id) {
        if (getIndex()==0) return new GpxLegendOverlay(osmPreview,id,new SegmentIndexWalker());
        if (getIndex()==1) return new GpxLegendOverlay(osmPreview,id,new MarkerDistanceWalker(getContext(), false));
        if (getIndex()==2) return new GpxLegendOverlay(osmPreview,id,new MarkerDistanceWalker(getContext(), true));
        if (getIndex()==3) return new GpxLegendOverlay(osmPreview,id,new MarkerAltitudeWalker(getContext()));
        if (getIndex()==4) return new GpxLegendOverlay(osmPreview,id,new MarkerSpeedWalker(getContext()));
        
        return new NullOverlay(osmPreview);
    }
    
    
    public OsmOverlay createWayLegendOverlay(AbsOsmView osmPreview, int id) {
        if (getIndex()==1) return new GpxLegendOverlay(osmPreview,id,new PointNameWalker());
        if (getIndex()==2) return new GpxLegendOverlay(osmPreview,id,new PointNameWalker());
        if (getIndex()==3) return new GpxLegendOverlay(osmPreview,id,new PointAltitudeWalker(getContext()));
        if (getIndex()==4) return new GpxLegendOverlay(osmPreview,id,new PointIndexWalker());

        return new NullOverlay(osmPreview);
    }

    
    public OsmOverlay createRouteLegendOverlay(AbsOsmView osmPreview, int id) {
        if (getIndex()==1) return new GpxLegendOverlay(osmPreview,id,new PointDistanceWalker(getContext(), false));
        if (getIndex()==2) return new GpxLegendOverlay(osmPreview,id,new PointDistanceWalker(getContext(), true));
        if (getIndex()==3) return new GpxLegendOverlay(osmPreview,id,new PointAltitudeWalker(getContext()));
        if (getIndex()==4) return new GpxLegendOverlay(osmPreview,id,new PointIndexWalker());
        
        return new NullOverlay(osmPreview);
    }

    
    @Override
    public int getImageResource() {
        return R.drawable.dialog_question;
    }
}

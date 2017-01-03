package ch.bailu.aat.preferences;

import android.content.Context;

import ch.bailu.aat.R;
import ch.bailu.aat.map.mapsforge.layer.context.MapContext;
import ch.bailu.aat.map.mapsforge.layer.gpx.GpxLayer;
import ch.bailu.aat.map.mapsforge.layer.gpx.legend.GpxLegendLayer;
import ch.bailu.aat.map.osm.AbsOsmView;
import ch.bailu.aat.map.osm.overlay.gpx.GpxOverlay;
import ch.bailu.aat.map.osm.overlay.gpx.legend.GpxLegendOverlay;
import ch.bailu.aat.map.osm.overlay.gpx.legend.MarkerAltitudeWalker;
import ch.bailu.aat.map.osm.overlay.gpx.legend.MarkerDistanceWalker;
import ch.bailu.aat.map.osm.overlay.gpx.legend.MarkerSpeedWalker;
import ch.bailu.aat.map.osm.overlay.gpx.legend.PointAltitudeWalker;
import ch.bailu.aat.map.osm.overlay.gpx.legend.PointDistanceWalker;
import ch.bailu.aat.map.osm.overlay.gpx.legend.PointIndexWalker;
import ch.bailu.aat.map.osm.overlay.gpx.legend.PointNameWalker;
import ch.bailu.aat.map.osm.overlay.gpx.legend.SegmentIndexWalker;

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

    
    public GpxOverlay createTrackLegendOverlay(AbsOsmView osmPreview) {
        if (getIndex()==0) return new GpxLegendOverlay(osmPreview,new SegmentIndexWalker());
        if (getIndex()==1) return new GpxLegendOverlay(osmPreview,new MarkerDistanceWalker(getContext(), false));
        if (getIndex()==2) return new GpxLegendOverlay(osmPreview,new MarkerDistanceWalker(getContext(), true));
        if (getIndex()==3) return new GpxLegendOverlay(osmPreview,new MarkerAltitudeWalker(getContext()));

        return new GpxLegendOverlay(osmPreview,new MarkerSpeedWalker(getContext()));
    }
    
    
    public GpxOverlay createWayLegendOverlay(AbsOsmView osmPreview) {
        if (getIndex()==1) return new GpxLegendOverlay(osmPreview,new PointNameWalker());
        if (getIndex()==2) return new GpxLegendOverlay(osmPreview,new PointNameWalker());
        if (getIndex()==3) return new GpxLegendOverlay(osmPreview,new PointAltitudeWalker(getContext()));

        return new GpxLegendOverlay(osmPreview,new PointIndexWalker());
    }

    
    public GpxOverlay createRouteLegendOverlay(AbsOsmView osmPreview) {
        if (getIndex()==1) return new GpxLegendOverlay(osmPreview,new PointDistanceWalker(getContext(), false));
        if (getIndex()==2) return new GpxLegendOverlay(osmPreview,new PointDistanceWalker(getContext(), true));
        if (getIndex()==3) return new GpxLegendOverlay(osmPreview,new PointAltitudeWalker(getContext()));

        return new GpxLegendOverlay(osmPreview,new PointIndexWalker());
    }


    public GpxLayer createTrackLegendOverlayMF(MapContext mc) {
        if (getIndex()==0) return new GpxLegendLayer(mc,new ch.bailu.aat.mapsforge.layer.gpx.legend.SegmentIndexWalker());
        if (getIndex()==1) return new GpxLegendLayer(mc,new ch.bailu.aat.mapsforge.layer.gpx.legend.MarkerDistanceWalker(getContext(), false));
        if (getIndex()==2) return new GpxLegendLayer(mc,new ch.bailu.aat.mapsforge.layer.gpx.legend.MarkerDistanceWalker(getContext(), true));
        if (getIndex()==3) return new GpxLegendLayer(mc,new ch.bailu.aat.mapsforge.layer.gpx.legend.MarkerAltitudeWalker(getContext()));

        return new GpxLegendLayer(mc,new ch.bailu.aat.mapsforge.layer.gpx.legend.MarkerSpeedWalker(getContext()));
    }


    public GpxLayer createWayLegendOverlayMF(MapContext mc) {
        if (getIndex()==1) return new GpxLegendLayer(mc,new ch.bailu.aat.mapsforge.layer.gpx.legend.PointNameWalker());
        if (getIndex()==2) return new GpxLegendLayer(mc,new ch.bailu.aat.mapsforge.layer.gpx.legend.PointNameWalker());
        if (getIndex()==3) return new GpxLegendLayer(mc,new ch.bailu.aat.mapsforge.layer.gpx.legend.PointAltitudeWalker(getContext()));

        return new GpxLegendLayer(mc,new ch.bailu.aat.mapsforge.layer.gpx.legend.PointIndexWalker());
    }


    public GpxLayer createRouteLegendOverlayMF(MapContext mc) {
        if (getIndex()==1) return new GpxLegendLayer(mc,new ch.bailu.aat.mapsforge.layer.gpx.legend.PointDistanceWalker(getContext(), false));
        if (getIndex()==2) return new GpxLegendLayer(mc,new ch.bailu.aat.mapsforge.layer.gpx.legend.PointDistanceWalker(getContext(), true));
        if (getIndex()==3) return new GpxLegendLayer(mc,new ch.bailu.aat.mapsforge.layer.gpx.legend.PointAltitudeWalker(getContext()));

        return new GpxLegendLayer(mc,new ch.bailu.aat.mapsforge.layer.gpx.legend.PointIndexWalker());
    }

    @Override
    public int getIconResource() {
        return R.drawable.dialog_question;
    }

}

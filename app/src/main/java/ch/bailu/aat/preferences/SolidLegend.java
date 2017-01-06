package ch.bailu.aat.preferences;

import android.content.Context;

import ch.bailu.aat.R;
import ch.bailu.aat.map.MapContext;
import ch.bailu.aat.map.layer.gpx.GpxLayer;
import ch.bailu.aat.map.layer.gpx.legend.GpxLegendLayer;

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

    


    public GpxLayer createTrackLegendOverlayMF(MapContext mc) {
        if (getIndex()==0) return new GpxLegendLayer(mc,new ch.bailu.aat.map.layer.gpx.legend.SegmentIndexWalker());
        if (getIndex()==1) return new GpxLegendLayer(mc,new ch.bailu.aat.map.layer.gpx.legend.MarkerDistanceWalker(getContext(), false));
        if (getIndex()==2) return new GpxLegendLayer(mc,new ch.bailu.aat.map.layer.gpx.legend.MarkerDistanceWalker(getContext(), true));
        if (getIndex()==3) return new GpxLegendLayer(mc,new ch.bailu.aat.map.layer.gpx.legend.MarkerAltitudeWalker(getContext()));

        return new GpxLegendLayer(mc,new ch.bailu.aat.map.layer.gpx.legend.MarkerSpeedWalker(getContext()));
    }


    public GpxLayer createWayLegendOverlayMF(MapContext mc) {
        if (getIndex()==1) return new GpxLegendLayer(mc,new ch.bailu.aat.map.layer.gpx.legend.PointNameWalker());
        if (getIndex()==2) return new GpxLegendLayer(mc,new ch.bailu.aat.map.layer.gpx.legend.PointNameWalker());
        if (getIndex()==3) return new GpxLegendLayer(mc,new ch.bailu.aat.map.layer.gpx.legend.PointAltitudeWalker(getContext()));

        return new GpxLegendLayer(mc,new ch.bailu.aat.map.layer.gpx.legend.PointIndexWalker());
    }


    public GpxLayer createRouteLegendOverlayMF(MapContext mc) {
        if (getIndex()==1) return new GpxLegendLayer(mc,new ch.bailu.aat.map.layer.gpx.legend.PointDistanceWalker(getContext(), false));
        if (getIndex()==2) return new GpxLegendLayer(mc,new ch.bailu.aat.map.layer.gpx.legend.PointDistanceWalker(getContext(), true));
        if (getIndex()==3) return new GpxLegendLayer(mc,new ch.bailu.aat.map.layer.gpx.legend.PointAltitudeWalker(getContext()));

        return new GpxLegendLayer(mc,new ch.bailu.aat.map.layer.gpx.legend.PointIndexWalker());
    }

    @Override
    public int getIconResource() {
        return R.drawable.dialog_question;
    }

}

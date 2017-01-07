package ch.bailu.aat.preferences;

import android.content.Context;

import ch.bailu.aat.R;
import ch.bailu.aat.map.MapContext;
import ch.bailu.aat.map.layer.gpx.GpxLayer;
import ch.bailu.aat.map.layer.gpx.legend.GpxLegendLayer;
import ch.bailu.aat.map.layer.gpx.legend.MarkerAltitudeWalker;
import ch.bailu.aat.map.layer.gpx.legend.MarkerDistanceWalker;
import ch.bailu.aat.map.layer.gpx.legend.MarkerSpeedWalker;
import ch.bailu.aat.map.layer.gpx.legend.PointAltitudeWalker;
import ch.bailu.aat.map.layer.gpx.legend.PointDistanceWalker;
import ch.bailu.aat.map.layer.gpx.legend.PointIndexWalker;
import ch.bailu.aat.map.layer.gpx.legend.PointNameWalker;
import ch.bailu.aat.map.layer.gpx.legend.SegmentIndexWalker;

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

    


    public GpxLayer createTrackLegendLayer() {
        if (getIndex()==0) return new GpxLegendLayer(new SegmentIndexWalker());
        if (getIndex()==1) return new GpxLegendLayer(new MarkerDistanceWalker(getContext(), false));
        if (getIndex()==2) return new GpxLegendLayer(new MarkerDistanceWalker(getContext(), true));
        if (getIndex()==3) return new GpxLegendLayer(new MarkerAltitudeWalker(getContext()));

        return new GpxLegendLayer(new MarkerSpeedWalker(getContext()));
    }


    public GpxLayer createWayLegendLayer() {
        if (getIndex()==1) return new GpxLegendLayer(new PointNameWalker());
        if (getIndex()==2) return new GpxLegendLayer(new PointNameWalker());
        if (getIndex()==3) return new GpxLegendLayer(new PointAltitudeWalker(getContext()));

        return new GpxLegendLayer(new PointIndexWalker());
    }


    public GpxLayer createRouteLegendLayer() {
        if (getIndex()==1) return new GpxLegendLayer(new PointDistanceWalker(getContext(), false));
        if (getIndex()==2) return new GpxLegendLayer(new PointDistanceWalker(getContext(), true));
        if (getIndex()==3) return new GpxLegendLayer(new PointAltitudeWalker(getContext()));

        return new GpxLegendLayer(new PointIndexWalker());
    }

    @Override
    public int getIconResource() {
        return R.drawable.dialog_question;
    }

}

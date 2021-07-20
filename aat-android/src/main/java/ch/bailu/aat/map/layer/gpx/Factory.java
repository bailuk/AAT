package ch.bailu.aat.map.layer.gpx;

import android.content.Context;

import ch.bailu.aat.preferences.map.SolidLegend;
import ch.bailu.aat_lib.gpx.interfaces.GpxType;
import ch.bailu.aat_lib.map.MapContext;

public abstract class Factory {

    public abstract GpxLayer legend(Context context, SolidLegend slegend, int color);
    public abstract GpxLayer layer(MapContext mcontext, int color);



    public static Factory get(GpxType type) {
        if (type == GpxType.WAY) return WAY;
        else if (type == GpxType.ROUTE) return ROUTE;
        else return TRACK;
    }


    public final static Factory WAY = new Factory() {


        @Override
        public GpxLayer legend(Context context, SolidLegend slegend, int iid) {
            return slegend.createWayLegendLayer(context);
        }

        @Override
        public GpxLayer layer(MapContext mcontext, int iid) {
            return new WayLayer(mcontext);
        }



    };


    public final static Factory ROUTE = new Factory() {


        @Override
        public GpxLayer legend(Context context, SolidLegend slegend, int iid) {
            return slegend.createRouteLegendLayer(context);
        }

        @Override
        public GpxLayer layer(MapContext mcontext, int iid) {
            return new RouteLayer(mcontext);
        }

    };


    public final static Factory TRACK = new Factory() {

        @Override
        public GpxLayer legend(Context context, SolidLegend slegend, int iid) {
            return slegend.createTrackLegendLayer(context);
        }

        @Override
        public GpxLayer layer(MapContext mcontext, int iid) {
            return new TrackLayer(mcontext);
        }
    };
}

package ch.bailu.aat.map.layer.gpx;

import ch.bailu.aat.gpx.interfaces.GpxType;
import ch.bailu.aat.map.MapColor;
import ch.bailu.aat.map.MapContext;
import ch.bailu.aat.preferences.SolidLegend;

public abstract class Factory {

    public abstract GpxLayer legend(SolidLegend slegend, int color);
    public abstract GpxLayer layer(MapContext mcontext, int color);



    public static Factory get(GpxType type) {
        if (type == GpxType.WAY) return WAY;
        else if (type == GpxType.ROUTE) return ROUTE;
        else return TRACK;
    }


    public final static Factory WAY = new Factory() {


        @Override
        public GpxLayer legend(SolidLegend slegend, int iid) {
            return slegend.createWayLegendLayer();
        }

        @Override
        public GpxLayer layer(MapContext mcontext, int iid) {
            return new WayLayer(mcontext);
        }



    };


    public final static Factory ROUTE = new Factory() {


        @Override
        public GpxLayer legend(SolidLegend slegend, int iid) {
            return slegend.createRouteLegendLayer();
        }

        @Override
        public GpxLayer layer(MapContext mcontext, int iid) {
            return new RouteLayer(mcontext);
        }

    };


    public final static Factory TRACK = new Factory() {

        @Override
        public GpxLayer legend(SolidLegend slegend, int iid) {
            return slegend.createTrackLegendLayer();
        }

        @Override
        public GpxLayer layer(MapContext mcontext, int iid) {
            return new TrackLayer(mcontext);
        }
    };
}

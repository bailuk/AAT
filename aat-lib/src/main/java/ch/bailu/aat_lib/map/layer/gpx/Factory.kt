package ch.bailu.aat_lib.map.layer.gpx;

import ch.bailu.aat_lib.preferences.map.SolidLegend;
import ch.bailu.aat_lib.gpx.interfaces.GpxType;
import ch.bailu.aat_lib.map.MapContext;
import ch.bailu.aat_lib.preferences.StorageInterface;
import ch.bailu.aat_lib.service.ServicesInterface;

public abstract class Factory {

    public abstract GpxLayer legend(StorageInterface storage, SolidLegend slegend, int color);
    public abstract GpxLayer layer(MapContext mcontext, ServicesInterface services, int color);


    public static Factory get(GpxType type) {
        if (type == GpxType.WAY) return WAY;
        else if (type == GpxType.ROUTE) return ROUTE;
        else return TRACK;
    }


    public final static Factory WAY = new Factory() {


        @Override
        public GpxLayer legend(StorageInterface storage, SolidLegend slegend, int iid) {
            return slegend.createWayLegendLayer(storage);
        }

        @Override
        public GpxLayer layer(MapContext mcontext, ServicesInterface services, int iid) {
            return new WayLayer(mcontext, services);
        }



    };


    public final static Factory ROUTE = new Factory() {


        @Override
        public GpxLayer legend(StorageInterface storage, SolidLegend slegend, int iid) {
            return slegend.createRouteLegendLayer(storage);
        }

        @Override
        public GpxLayer layer(MapContext mcontext, ServicesInterface services, int iid) {
            return new RouteLayer(mcontext);
        }

    };


    public final static Factory TRACK = new Factory() {

        @Override
        public GpxLayer legend(StorageInterface storage, SolidLegend slegend, int iid) {
            return slegend.createTrackLegendLayer(storage);
        }

        @Override
        public GpxLayer layer(MapContext mcontext, ServicesInterface services, int iid) {
            return new TrackLayer(mcontext);
        }
    };
}

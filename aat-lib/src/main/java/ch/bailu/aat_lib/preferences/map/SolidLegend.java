package ch.bailu.aat_lib.preferences.map;

import ch.bailu.aat_lib.map.layer.gpx.GpxLayer;
import ch.bailu.aat_lib.map.layer.gpx.legend.GpxLegendLayer;
import ch.bailu.aat_lib.map.layer.gpx.legend.MarkerAltitudeWalker;
import ch.bailu.aat_lib.map.layer.gpx.legend.MarkerDistanceWalker;
import ch.bailu.aat_lib.map.layer.gpx.legend.MarkerSpeedWalker;
import ch.bailu.aat_lib.map.layer.gpx.legend.NullLegendWalker;
import ch.bailu.aat_lib.map.layer.gpx.legend.PointAltitudeWalker;
import ch.bailu.aat_lib.map.layer.gpx.legend.PointDistanceWalker;
import ch.bailu.aat_lib.map.layer.gpx.legend.PointIndexWalker;
import ch.bailu.aat_lib.map.layer.gpx.legend.PointNameWalker;
import ch.bailu.aat_lib.map.layer.gpx.legend.SegmentIndexWalker;
import ch.bailu.aat_lib.preferences.SolidStaticIndexList;
import ch.bailu.aat_lib.preferences.StorageInterface;
import ch.bailu.aat_lib.resources.Images;
import ch.bailu.aat_lib.resources.Res;

public class SolidLegend extends SolidStaticIndexList {

    private static final String POSTFIX="_LEGEND";

    public SolidLegend(StorageInterface storage, String k) {
        super(storage, k+POSTFIX, new String[] {
            Res.str().none(),
            Res.str().p_legend_fulldistance() + " / " + Res.str().name(),
            Res.str().distance()+ " / " + Res.str().name(),
            Res.str().altitude(),
            Res.str().speed()
            });
    }

    public GpxLayer createTrackLegendLayer(StorageInterface storage) {
        if (getIndex()==0) return new GpxLegendLayer(new SegmentIndexWalker());
        if (getIndex()==1) return new GpxLegendLayer(new MarkerDistanceWalker(storage, false));
        if (getIndex()==2) return new GpxLegendLayer(new MarkerDistanceWalker(storage, true));
        if (getIndex()==3) return new GpxLegendLayer(new MarkerAltitudeWalker(storage));

        return new GpxLegendLayer(new MarkerSpeedWalker(storage));
    }


    public GpxLayer createWayLegendLayer(StorageInterface storage) {
        if (getIndex()==0) return new GpxLegendLayer(new NullLegendWalker());
        if (getIndex()==1) return new GpxLegendLayer(new PointNameWalker());
        if (getIndex()==2) return new GpxLegendLayer(new PointNameWalker());
        if (getIndex()==3) return new GpxLegendLayer(new PointAltitudeWalker(storage));

        return new GpxLegendLayer(new PointIndexWalker());
    }


    public GpxLayer createRouteLegendLayer(StorageInterface storage) {
        if (getIndex()==0) return new GpxLegendLayer(new NullLegendWalker());
        if (getIndex()==1) return new GpxLegendLayer(new PointDistanceWalker(storage, false));
        if (getIndex()==2) return new GpxLegendLayer(new PointDistanceWalker(storage, true));
        if (getIndex()==3) return new GpxLegendLayer(new PointAltitudeWalker(storage));

        return new GpxLegendLayer(new PointIndexWalker());
    }

    @Override
    public int getIconResource() {
        return Images.dialog_question();
    }

}

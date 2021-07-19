package ch.bailu.aat.map;

import android.content.Context;

import ch.bailu.aat.map.mapsforge.MapsForgeViewBase;
import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat_lib.map.MapContext;
import ch.bailu.aat_lib.map.MapViewInterface;

public class To {
    public static Context context(MapContext mc) {
        AndroidMapContext amc = (AndroidMapContext) mc;
        return amc.getContext();
    }

    public static ServiceContext scontext(MapContext mc) {
        AndroidMapContext amc = (AndroidMapContext) mc;
        return amc.getSContext();
    }

    public static MapsForgeViewBase view(MapViewInterface map) {
        return  (MapsForgeViewBase) map;

    }
}

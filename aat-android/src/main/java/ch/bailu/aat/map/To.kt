package ch.bailu.aat.map;

import ch.bailu.aat.map.mapsforge.MapsForgeViewBase;
import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat_lib.map.MapViewInterface;
import ch.bailu.aat_lib.service.ServicesInterface;

public class To {

    public static MapsForgeViewBase view(MapViewInterface map) {
        return  (MapsForgeViewBase) map;

    }

    public static ServiceContext serviceContext(ServicesInterface services) {
        return (ServiceContext) services;
    }
}

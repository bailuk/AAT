package ch.bailu.aat_lib.preferences.location;

import javax.annotation.Nonnull;

import ch.bailu.aat_lib.preferences.SolidStaticIndexList;
import ch.bailu.aat_lib.preferences.StorageInterface;
import ch.bailu.aat_lib.resources.Res;
import ch.bailu.aat_lib.service.location.LocationServiceInterface;
import ch.bailu.aat_lib.service.location.LocationStackItem;

public abstract class SolidLocationProvider extends SolidStaticIndexList {

    private static final String KEY="location_provider";

    public SolidLocationProvider(StorageInterface s, String[] list) {
        super(s, KEY, list);
    }


    public abstract LocationStackItem createProvider(LocationServiceInterface locationService, LocationStackItem last);


    @Nonnull
    @Override
    public String getLabel() {
        return Res.str().p_location_provider();
    }

}

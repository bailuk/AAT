package ch.bailu.aat.preferences;


import android.content.Context;

import ch.bailu.aat.services.cache.CacheService;
import ch.bailu.aat.services.cache.osm_features.MapFeaturesHandle;
import ch.bailu.aat.util.ToDo;
import ch.bailu.aat.util.filter_list.KeyList;

public class SolidOsmFeaturesList extends SolidBoolean {

    private final static String SMALL_LIST_KEYS = "amenity emergency leisure shop sport tourism name";

    public SolidOsmFeaturesList(Context c) {
        super(Storage.global(c), SolidOsmFeaturesList.class.getSimpleName());
    }


    @Override
    public String getLabel() {
        return ToDo.translate("All");
    }


    public MapFeaturesHandle getList(CacheService cacheService) {
        String id = MapFeaturesHandle.ID_SMALL;

        if (isEnabled())
            id = MapFeaturesHandle.ID_FULL;

        return (MapFeaturesHandle)
                cacheService.getObject(id, new MapFeaturesHandle.Factory(id));
    }


    public static KeyList getKeyList(String id) {
        if (MapFeaturesHandle.ID_SMALL.equals(id)) {
            return new KeyList(SMALL_LIST_KEYS);
        } else {
            return new KeyList();
        }
    }
}

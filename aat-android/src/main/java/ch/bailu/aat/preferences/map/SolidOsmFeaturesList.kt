package ch.bailu.aat.preferences.map;


import android.content.Context;

import ch.bailu.aat.preferences.Storage;
import ch.bailu.aat.services.cache.osm_features.ObjMapFeatures;
import ch.bailu.aat_lib.lib.filter_list.KeyList;
import ch.bailu.aat_lib.preferences.SolidBoolean;
import ch.bailu.aat_lib.resources.Res;
import ch.bailu.aat_lib.service.cache.CacheServiceInterface;

public class SolidOsmFeaturesList extends SolidBoolean {

    private final static String SMALL_LIST_KEYS = "amenity emergency leisure shop sport tourism name";

    public SolidOsmFeaturesList(Context c) {
        super(new Storage(c), SolidOsmFeaturesList.class.getSimpleName());
    }


    @Override
    public String getLabel() {
        return Res.str().all();
    }


    public ObjMapFeatures getList(CacheServiceInterface cacheService) {
        String id = ObjMapFeatures.ID_SMALL;

        if (isEnabled())
            id = ObjMapFeatures.ID_FULL;

        return (ObjMapFeatures)
                cacheService.getObject(id, new ObjMapFeatures.Factory(id));
    }


    public static KeyList getKeyList(String id) {
        if (ObjMapFeatures.ID_SMALL.equals(id)) {
            return new KeyList(SMALL_LIST_KEYS);
        } else {
            return new KeyList();
        }
    }
}

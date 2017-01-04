package ch.bailu.aat.map.osmdroid;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;

import ch.bailu.aat.preferences.SolidMapTileStack;
import ch.bailu.aat.preferences.SolidPreset;
import ch.bailu.aat.preferences.SolidTileSize;
import ch.bailu.aat.preferences.Storage;
import ch.bailu.aat.services.ServiceContext;


public class DynTileProvider extends CachedTileProvider implements OnSharedPreferenceChangeListener {
    private final Storage     storage;
    private final SolidPreset spreset;

    private SolidMapTileStack soverlay;



    public DynTileProvider(ServiceContext sc) {
        super(sc);

        setTileSize(new SolidTileSize(sc.getContext()).getTileSize());

        storage = Storage.preset(sc.getContext()); 
        spreset = new SolidPreset(sc.getContext());

        createSolid(spreset);

        storage.register(this);

        setSubTileSource(soverlay.getSourceList());
    }



    private void createSolid(SolidPreset spreset) {
        soverlay = new SolidMapTileStack(spreset.getContext(), spreset.getIndex());
    }




    @Override
    public void onDetached() {
        storage.unregister(this);
        super.onDetached();

    }



    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
            String key) {
        if (soverlay.hasKey(key)) {
            setSourceList();


        } else if (spreset.hasKey(key)) {
            createSolid(spreset);
            setSourceList();
        }
    }



    private void setSourceList() {
        setSubTileSource(soverlay.getSourceList());
        onCacheChanged();
    }
}

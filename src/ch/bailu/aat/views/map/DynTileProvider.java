package ch.bailu.aat.views.map;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import ch.bailu.aat.preferences.SolidMapTileStack;
import ch.bailu.aat.preferences.SolidPreset;
import ch.bailu.aat.preferences.Storage;
import ch.bailu.aat.services.MultiServiceLink.ServiceContext;


public class DynTileProvider extends CachedTileProvider implements OnSharedPreferenceChangeListener {
    private final Storage     storage;
    private final SolidPreset spreset;

    private SolidMapTileStack soverlay;



    public DynTileProvider(ServiceContext context, String key) {
        super(context);
        storage = Storage.preset(context.getContext()); 
        spreset = new SolidPreset(context.getContext());

        createSolid(spreset);

        storage.register(this);

        setSourceList();
    }



    private void createSolid(SolidPreset spreset) {
        soverlay = new SolidMapTileStack(spreset.getContext(), spreset.getIndex());
    }




    @Override
    public void detach() {
        storage.unregister(this);
        super.detach();

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
    }
}

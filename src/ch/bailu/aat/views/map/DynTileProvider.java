package ch.bailu.aat.views.map;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import ch.bailu.aat.preferences.SolidMapTileStack;
import ch.bailu.aat.preferences.SolidPreset;
import ch.bailu.aat.preferences.Storage;


public class DynTileProvider extends CachedTileProvider implements OnSharedPreferenceChangeListener {
    private final Storage              storage;
    private final SolidPreset          spreset;

    private SolidMapTileStack soverlay;



    public DynTileProvider(Context context, String key) {
        super(context);
        storage = Storage.preset(context); 
        spreset = new SolidPreset(context);

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
        setSubTileSource(
                soverlay.getSourceList(), 
                soverlay.getFilterList());
    }
}

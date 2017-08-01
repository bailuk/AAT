package ch.bailu.aat.services.render;

import android.content.SharedPreferences;

import org.mapsforge.core.graphics.TileBitmap;
import org.mapsforge.core.model.Tile;

import ch.bailu.aat.preferences.SolidMapsForgeDirectory;
import ch.bailu.aat.preferences.SolidRenderTheme;
import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat.services.VirtualService;
import ch.bailu.aat.services.cache.MapsForgeTileObject;

public class RenderService  extends VirtualService
        implements SharedPreferences.OnSharedPreferenceChangeListener {


    private final SolidMapsForgeDirectory sdirectory;
    private final SolidRenderTheme stheme;

    private final Configuration configuration = new Configuration();
    private final Caches caches= new Caches();


    public RenderService(ServiceContext sc) {
        super(sc);

        sdirectory = new SolidMapsForgeDirectory(sc.getContext());
        stheme = new SolidRenderTheme(sc.getContext());

        sdirectory.getStorage().register(this);
        reconfigureRenderer();
    }


    private void reconfigureRenderer() {
        configuration.destroy();
        configuration.configure(
                sdirectory.getValueAsFile(),
                caches,
                stheme.getValueAsRenderTheme(),
                stheme.getValueAsThemeID());
    }


    public TileBitmap getTile(MapsForgeTileObject o) {
        return configuration.getTile(o);
    }


    public void lockToCache(MapsForgeTileObject o) {
        caches.lockToCache(o);
    }


    public void freeFromCache(MapsForgeTileObject o) {
        caches.freeFromCache(o);
    }



    @Override
    public void appendStatusText(StringBuilder builder) {

    }

    @Override
    public void close() {
        sdirectory.getStorage().unregister(this);
        configuration.destroy();
    }


    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (sdirectory.hasKey(key) || stheme.hasKey(key)) {
            reconfigureRenderer();
        }
    }

    public boolean supportsTile(Tile t) {
        return configuration.supportsTile(t);
    }
}

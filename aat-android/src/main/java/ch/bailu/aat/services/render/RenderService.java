package ch.bailu.aat.services.render;

import android.content.SharedPreferences;

import org.mapsforge.core.model.Tile;

import ch.bailu.aat.preferences.map.SolidMapsForgeDirectory;
import ch.bailu.aat.preferences.map.SolidRenderTheme;
import ch.bailu.aat.preferences.map.SolidRendererThreads;
import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat.services.cache.ObjTileMapsForge;
import ch.bailu.aat_lib.preferences.OnPreferencesChanged;
import ch.bailu.aat_lib.preferences.StorageInterface;
import ch.bailu.aat_lib.service.VirtualService;

public final class RenderService  extends VirtualService
        implements OnPreferencesChanged {


    private final SolidMapsForgeDirectory sdirectory;
    private final SolidRenderTheme stheme;
    //private final SolidRendererThreads sthreads;


    private final Configuration configuration = new Configuration();
    private final Caches caches= new Caches();


    public RenderService(ServiceContext sc) {


        sdirectory = new SolidMapsForgeDirectory(sc.getContext());
        stheme = new SolidRenderTheme(sc.getContext());
        //sthreads = new SolidRendererThreads(sc.getContext());

        sdirectory.getStorage().register(this);
        reconfigureRenderer();
    }


    private void reconfigureRenderer() {
        configuration.destroy();
        SolidRendererThreads.set();
        configuration.configure(
                sdirectory.getValueAsFile(),
                caches,
                stheme.getValueAsRenderTheme(),
                stheme.getValueAsThemeID());
    }


      public void lockToRenderer(ObjTileMapsForge o) {
        caches.lockToRenderer(o);
        configuration.lockToRenderer(o);
    }


    public void freeFromRenderer(ObjTileMapsForge o) {
        configuration.freeFromRenderer(o);
        caches.freeFromRenderer(o);
    }

    public void close() {
        sdirectory.getStorage().unregister(this);
        configuration.destroy();
    }


    @Override
    public void onPreferencesChanged(StorageInterface s, String key) {
        if (sdirectory.hasKey(key) || stheme.hasKey(key) /*|| sthreads.hasKey(key)*/) {
            reconfigureRenderer();
        }
    }

    public boolean supportsTile(Tile t) {
        return configuration.supportsTile(t);
    }
}

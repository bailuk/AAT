package ch.bailu.aat.services.render;

import org.mapsforge.core.model.Tile;

import ch.bailu.aat.preferences.map.AndroidSolidMapsForgeDirectory;
import ch.bailu.aat.preferences.map.SolidRenderTheme;
import ch.bailu.aat.preferences.map.SolidRendererThreads;
import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat_lib.preferences.OnPreferencesChanged;
import ch.bailu.aat_lib.preferences.StorageInterface;
import ch.bailu.aat_lib.preferences.map.SolidMapsForgeDirectory;
import ch.bailu.aat_lib.service.VirtualService;
import ch.bailu.aat_lib.service.cache.ObjTileMapsForge;
import ch.bailu.aat_lib.service.render.RenderServiceInterface;
import ch.bailu.foc_android.FocAndroidFactory;

public final class RenderService  extends VirtualService
        implements OnPreferencesChanged, RenderServiceInterface {


    private final SolidMapsForgeDirectory sdirectory;
    private final SolidRenderTheme stheme;
    //private final SolidRendererThreads sthreads;


    private final Configuration configuration = new Configuration();
    private final Caches caches= new Caches();


    public RenderService(ServiceContext sc) {


        sdirectory = new AndroidSolidMapsForgeDirectory(sc.getContext());
        stheme = new SolidRenderTheme(new AndroidSolidMapsForgeDirectory(sc.getContext()), new FocAndroidFactory(sc.getContext()));
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

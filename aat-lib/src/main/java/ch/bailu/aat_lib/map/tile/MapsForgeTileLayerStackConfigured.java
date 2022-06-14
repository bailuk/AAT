package ch.bailu.aat_lib.map.tile;

import org.mapsforge.map.view.MapView;

import javax.annotation.Nonnull;

import ch.bailu.aat_lib.app.AppContext;
import ch.bailu.aat_lib.map.tile.source.CachedSource;
import ch.bailu.aat_lib.map.tile.source.DoubleSource;
import ch.bailu.aat_lib.map.tile.source.ElevationSource;
import ch.bailu.aat_lib.map.tile.source.MapsForgeSource;
import ch.bailu.aat_lib.map.tile.source.Source;
import ch.bailu.aat_lib.preferences.StorageInterface;
import ch.bailu.aat_lib.preferences.map.SolidEnableTileCache;
import ch.bailu.aat_lib.preferences.map.SolidMapTileStack;
import ch.bailu.aat_lib.preferences.map.SolidRenderTheme;
import ch.bailu.aat_lib.service.cache.DownloadSource;

public abstract class MapsForgeTileLayerStackConfigured extends MapsForgeTileLayerStack {

    private final SolidMapTileStack stiles;
    private final SolidEnableTileCache.Hillshade scacheHS;
    private final SolidEnableTileCache.MapsForge scacheMF;
    private final SolidRenderTheme stheme;

    private final MapView mapView;
    protected final AppContext scontext;

    public MapsForgeTileLayerStackConfigured(MapView v, AppContext appContext) {
        super(appContext.getServices());

        scontext = appContext;

        mapView = v;
        stheme = new SolidRenderTheme(appContext.getMapDirectory(), appContext);
        stiles = new SolidMapTileStack(stheme);
        scacheMF = new SolidEnableTileCache.MapsForge(appContext.getStorage());
        scacheHS = new SolidEnableTileCache.Hillshade(appContext.getStorage());

        initLayers();
    }


    private void initLayers() {
        boolean[] enabled = stiles.getEnabledArray();
        Source[] sources = SolidMapTileStack.SOURCES;

        removeLayers();

        addBackgroundLayers(enabled, sources);
        addOverlayLayers(enabled, sources);

        setMapViewZoomLimit(mapView);
    }

    protected abstract void addBackgroundLayers(boolean[] enabled, Source[] sources);

    protected abstract void addOverlayLayers(boolean[] enabled, Source[] sources);

    protected Source getHillShadeSource() {
        if (scacheHS.isEnabled()) {
            return CachedSource.CACHED_ELEVATION_HILLSHADE;
        }
        return ElevationSource.ELEVATION_HILLSHADE;
    }

    protected Source getMapsForgeSource() {
        String theme = stheme.getValueAsString();

        if (scacheMF.isEnabled()) {
            return new CachedSource(new MapsForgeSource(theme));
        }
        return new MapsForgeSource(theme);
    }

    @Override
    public void onPreferencesChanged(@Nonnull StorageInterface s, @Nonnull String key) {

        if (
                stiles.hasKey(key) ||
                scacheMF.hasKey(key) ||
                scacheHS.hasKey(key) ||
                stheme.hasKey(key))

        {
            initLayers();
        }
    }


    public static class BackgroundOnly extends MapsForgeTileLayerStackConfigured {
        public BackgroundOnly(MapView v, AppContext appContext) {
            super(v, appContext);
        }

        @Override
        protected void addBackgroundLayers(boolean[] enabled, Source[] sources) {
            Source download = null, mapsforge = null;

            for (int i=0; i< enabled.length; i++) {
                if (enabled[i]) {
                    if (DownloadSource.isDownloadBackgroundSource(sources[i]))
                        download = sources[i];
                }
            }

            if (enabled[0] && sources[0] == MapsForgeSource.MAPSFORGE) {
                mapsforge = getMapsForgeSource();
            }

            if (download != null && mapsforge != null) {
                addLayer(new TileProvider(scontext,
                        new DoubleSource(scontext.getServices(), mapsforge, download, 6)), scontext.getTilePainter());

            } else if (download != null) {
                addLayer(new TileProvider(scontext, download), scontext.getTilePainter());

            } else if (mapsforge != null) {
                addLayer(new TileProvider(scontext, mapsforge), scontext.getTilePainter());
            }
        }

        @Override
        protected void addOverlayLayers(boolean[] enabled, Source[] sources) {}
    }


    public static class All extends BackgroundOnly {
        public All(MapView v, AppContext appContext) {
            super(v, appContext);
        }

        @Override
        protected void addOverlayLayers(boolean[] enabled, Source[] sources) {
            for (int i=SolidMapTileStack.FIRST_OVERLAY_INDEX; i< sources.length; i++) {
                if (enabled[i]) {
                    Source s = sources[i];

                    if (s == ElevationSource.ELEVATION_HILLSHADE) {
                        s = getHillShadeSource();
                    }

                    addLayer(new TileProvider(scontext, s), scontext.getTilePainter());
                }
            }
        }

    }
}

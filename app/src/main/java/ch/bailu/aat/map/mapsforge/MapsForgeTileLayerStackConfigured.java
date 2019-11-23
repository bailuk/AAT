package ch.bailu.aat.map.mapsforge;

import android.content.SharedPreferences;

import ch.bailu.aat.map.tile.TileProvider;
import ch.bailu.aat.map.tile.source.CachedSource;
import ch.bailu.aat.map.tile.source.DoubleSource;
import ch.bailu.aat.map.tile.source.DownloadSource;
import ch.bailu.aat.map.tile.source.MapsForgeSource;
import ch.bailu.aat.map.tile.source.Source;
import ch.bailu.aat.preferences.map.SolidEnableTileCache;
import ch.bailu.aat.preferences.map.SolidMapTileStack;
import ch.bailu.aat.preferences.map.SolidRenderTheme;
import ch.bailu.aat.services.ServiceContext;

public abstract class MapsForgeTileLayerStackConfigured extends MapsForgeTileLayerStack {

    private final SolidMapTileStack stiles;
    private final SolidEnableTileCache.Hillshade scacheHS;
    private final SolidEnableTileCache.MapsForge scacheMF;
    private final SolidRenderTheme stheme;

    private final MapsForgeViewBase mapView;
    protected final ServiceContext scontext;

    public MapsForgeTileLayerStackConfigured(MapsForgeViewBase v) {
        super(v.getMContext().getSContext());

        scontext = v.getMContext().getSContext();

        mapView = v;
        stiles = new SolidMapTileStack(scontext.getContext());
        stheme = new SolidRenderTheme(scontext.getContext());
        scacheMF = new SolidEnableTileCache.MapsForge(scontext.getContext());
        scacheHS = new SolidEnableTileCache.Hillshade(scontext.getContext());

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
        return Source.ELEVATION_HILLSHADE;
    }


    protected Source getMapsForgeSource() {
        String theme =
                stheme.getValueAsString();

        if (scacheMF.isEnabled()) {
            return new CachedSource(new MapsForgeSource(theme));
        }
        return new MapsForgeSource(theme);
    }


    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
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
        public BackgroundOnly(MapsForgeViewBase v) {
            super(v);
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
                        new DoubleSource(scontext, mapsforge, download, 6)));

            } else if (download != null) {
                addLayer(new TileProvider(scontext, download));

            } else if (mapsforge != null) {
                addLayer(new TileProvider(scontext, mapsforge));
            }
        }

        @Override
        protected void addOverlayLayers(boolean[] enabled, Source[] sources) {

        }
    }


    public static class All extends BackgroundOnly {
        public All(MapsForgeViewBase v) {
            super(v);
        }

        @Override
        protected void addOverlayLayers(boolean[] enabled, Source[] sources) {
            for (int i=2; i< sources.length; i++) {
                if (enabled[i]) {
                    Source s = sources[i];

                    if (s == Source.ELEVATION_HILLSHADE) {
                        s = getHillShadeSource();
                    }

                    addLayer(new TileProvider(scontext, s));
                }
            }
        }

    }
}

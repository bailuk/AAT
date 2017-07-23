package ch.bailu.aat.map.mapsforge;

import android.content.SharedPreferences;

import ch.bailu.aat.map.tile.TileProvider;
import ch.bailu.aat.map.tile.source.CachedSource;
import ch.bailu.aat.map.tile.source.DownloadSource;
import ch.bailu.aat.map.tile.source.MapsForgeSource;
import ch.bailu.aat.map.tile.source.Source;
import ch.bailu.aat.preferences.SolidEnableTileCache;
import ch.bailu.aat.preferences.SolidMapTileStack;
import ch.bailu.aat.preferences.SolidRenderTheme;
import ch.bailu.aat.services.ServiceContext;

public class MapsForgeTileLayerStackConfigured extends MapsForgeTileLayerStack {

    private final SolidMapTileStack stiles;
    private final SolidEnableTileCache.Hillshade scacheHS;
    private final SolidEnableTileCache.MapsForge scacheMF;
    private final SolidRenderTheme stheme;

    private final MapsForgeView mapView;
    private final ServiceContext scontext;

    public MapsForgeTileLayerStackConfigured(MapsForgeView v) {
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
        boolean enabled[] = stiles.getEnabledArray();
        Source sources[] = SolidMapTileStack.SOURCES;

        removeLayers();

        addBackgroundLayers(enabled, sources);
        addOverlayLayers(enabled, sources);

        setMapViewZoomLimit(mapView);
    }


    private void addBackgroundLayers(boolean enabled[], Source sources[]) {
        Source mapnik = null, mapsforge = null;

        if (enabled[1] && sources[1] == DownloadSource.MAPNIK) {
            mapnik = DownloadSource.MAPNIK;
        }

        if (enabled[0] && sources[0] == MapsForgeSource.MAPSFORGE) {
            mapsforge = getMapsForgeSource();
        }

        if (mapnik != null && mapsforge != null) {
            addLayer(new TileProvider(scontext, mapnik), 0, 6);
            addLayer(new TileProvider(scontext, mapsforge), 7, 100);

        } else if (mapnik != null) {
            addLayer(new TileProvider(scontext, mapnik));

        } else if (mapsforge != null) {
            addLayer(new TileProvider(scontext, mapsforge));
        }
    }


    private void addOverlayLayers(boolean enabled[], Source sources[]) {
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

    private Source getHillShadeSource() {
        if (scacheHS.isEnabled()) {
            return CachedSource.CACHED_ELEVATION_HILLSHADE;
        }
        return Source.ELEVATION_HILLSHADE;
    }


    private Source getMapsForgeSource() {
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
}

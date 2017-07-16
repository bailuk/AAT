package ch.bailu.aat.map.mapsforge;

import android.content.SharedPreferences;

import org.mapsforge.core.model.Point;

import java.util.ArrayList;

import ch.bailu.aat.map.MapContext;
import ch.bailu.aat.map.layer.MapLayerInterface;
import ch.bailu.aat.map.tile.TileProvider;
import ch.bailu.aat.map.tile.source.CachedSource;
import ch.bailu.aat.map.tile.source.MapsForgeSource;
import ch.bailu.aat.map.tile.source.Source;
import ch.bailu.aat.preferences.SolidEnableTileCache;
import ch.bailu.aat.preferences.SolidMapTileStack;
import ch.bailu.aat.preferences.SolidPreset;
import ch.bailu.aat.preferences.SolidRenderTheme;
import ch.bailu.aat.services.ServiceContext;

public class MapsForgeTileLayerStack implements MapLayerInterface {

    private final SolidMapTileStack stiles;

    private final ArrayList<MapsForgeTileLayer> layers =
            new ArrayList(SolidMapTileStack.SOURCES.length);


    private final MapsForgeView mapView;

    public MapsForgeTileLayerStack(MapsForgeView v) {
        final ServiceContext sc = v.getMContext().getSContext();

        mapView = v;
        stiles = new SolidMapTileStack(sc.getContext());

        init(sc);
    }


    private void init(ServiceContext sc) {
        for (Source s: SolidMapTileStack.SOURCES) {
            Source source = s;
            if (s == MapsForgeSource.MAPSFORGE) {
                String theme =
                        new SolidRenderTheme(sc.getContext()).getValueAsString();

                if (new SolidEnableTileCache.MapsForge(sc.getContext()).isEnabled()) {
                    source = new CachedSource(new MapsForgeSource(theme));
                } else {
                    source = new MapsForgeSource(theme);
                }

            } else if (s == Source.ELEVATION_HILLSHADE) {

                if (new SolidEnableTileCache.Hillshade(sc.getContext()).isEnabled()) {
                    source = CachedSource.CACHED_ELEVATION_HILLSHADE;
                } else {
                    source = Source.ELEVATION_HILLSHADE;
                }
            }


            MapsForgeTileLayer layer =
                    new MapsForgeTileLayer(sc,
                            new TileProvider(sc, source));

            layers.add(layer);
            mapView.add(layer, layer);
        }
    }


    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (stiles.hasKey(key)) {
            setEnabled();
        }
    }


    private void setEnabled() {
        int minZoom=6, maxZoom = 10;

        boolean[] enabled = stiles.getEnabledArray();

        for (int i=0; i<enabled.length; i++) {
            layers.get(i).setVisible(enabled[i]);

            if (enabled[i]) {
                maxZoom = Math.max(SolidMapTileStack.SOURCES[i].getMaximumZoomLevel(), maxZoom);
                minZoom = Math.min(SolidMapTileStack.SOURCES[i].getMinimumZoomLevel(), minZoom);
            }
        }

        mapView.setZoomLevelMin((byte)minZoom);
        mapView.setZoomLevelMax((byte)maxZoom);
    }

    public void reDownloadTiles() {
        for(MapsForgeTileLayer l: layers) {
            if (l.isVisible()) l.reDownloadTiles();
        }
    }

    @Override
    public void onLayout(boolean changed, int l, int t, int r, int b) {

    }

    @Override
    public void drawInside(MapContext mcontext) {

    }

    @Override
    public void drawForeground(MapContext mcontext) {

    }

    @Override
    public boolean onTap(Point tapXY) {
        return false;
    }


    @Override
    public void onAttached() {
        setEnabled();

    }

    @Override
    public void onDetached() {

    }

}

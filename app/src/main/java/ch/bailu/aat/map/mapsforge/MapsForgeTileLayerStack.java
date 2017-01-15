package ch.bailu.aat.map.mapsforge;

import android.content.Context;
import android.content.SharedPreferences;

import org.mapsforge.core.model.LatLong;
import org.mapsforge.core.model.Point;

import java.util.ArrayList;

import ch.bailu.aat.map.MapContext;
import ch.bailu.aat.map.layer.MapLayerInterface;
import ch.bailu.aat.map.tile.TileProvider;
import ch.bailu.aat.preferences.SolidMapTileStack;
import ch.bailu.aat.preferences.SolidPreset;
import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat.services.cache.TileObject;

public class MapsForgeTileLayerStack implements MapLayerInterface {

    private final SolidMapTileStack stiles;


    private final ArrayList<MapsForgeTileLayer> layers =
            new ArrayList(SolidMapTileStack.SOURCES.length);


    private final MapsForgeView mapView;

    public MapsForgeTileLayerStack(MapsForgeView v) {
        mapView = v;
        Context context = mapView.getContext();
        ServiceContext scontext = mapView.getMContext().getSContext();

        int preset = new SolidPreset(context).getIndex();
        stiles = new SolidMapTileStack(context, preset);

        int alpha = MapsForgeTileLayer.OPAQUE;
        for (TileObject.Source s: SolidMapTileStack.SOURCES) {
            MapsForgeTileLayer layer =
                    new MapsForgeTileLayer(new TileProvider(scontext,s), alpha);

            layers.add(layer);
            mapView.add(layer, layer);

            if (layers.size() == 2) alpha = MapsForgeTileLayer.TRANSPARENT;
        }
        setEnabled();
    }


    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (stiles.hasKey(key)) {
            setEnabled();
        }
    }


    private void setEnabled() {
        int minZoom=8, maxZoom = 10;

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


    @Override
    public void onLayout(boolean changed, int l, int t, int r, int b) {

    }

    @Override
    public void drawInside(MapContext mcontext) {

    }

    @Override
    public void drawOnTop(MapContext mcontext) {

    }

    @Override
    public boolean onTap(LatLong tapLatLong, Point layerXY, Point tapXY) {
        return false;
    }


    @Override
    public void onAttached() {

    }

    @Override
    public void onDetached() {

    }
}

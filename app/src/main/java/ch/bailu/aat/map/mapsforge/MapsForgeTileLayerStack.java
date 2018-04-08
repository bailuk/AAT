package ch.bailu.aat.map.mapsforge;

import android.content.SharedPreferences;
import android.graphics.Color;

import org.mapsforge.core.graphics.Canvas;
import org.mapsforge.core.model.BoundingBox;
import org.mapsforge.core.model.Point;
import org.mapsforge.map.layer.Layer;
import org.mapsforge.map.model.DisplayModel;
import org.mapsforge.map.model.common.Observer;
import org.mapsforge.map.view.MapView;

import java.util.ArrayList;

import ch.bailu.aat.map.Attachable;
import ch.bailu.aat.map.MapContext;
import ch.bailu.aat.map.layer.MapLayerInterface;
import ch.bailu.aat.map.tile.TileProvider;
import ch.bailu.aat.services.InsideContext;
import ch.bailu.aat.services.ServiceContext;

public class MapsForgeTileLayerStack extends Layer implements MapLayerInterface {

    private final SubLayers layers = new SubLayers();

    private final ServiceContext scontext;

    int minZoom=5, maxZoom = 5;


    public MapsForgeTileLayerStack(ServiceContext sc) {
        scontext = sc;
    }


    public void addLayer(TileProvider provider) {
        MapsForgeTileLayer layer = new MapsForgeTileLayer(scontext, provider);

        layer.setDisplayModel(getDisplayModel());

        layers.add(layer);

        provider.addObserver(new Observer() {

            @Override
            public void onChange() {
                requestRedraw();
            }
        });

        maxZoom = Math.max(provider.getMaximumZoomLevel(), maxZoom);
        minZoom = Math.min(provider.getMinimumZoomLevel(), minZoom);


    }


    @Override
    public void setDisplayModel(DisplayModel model) {
        super.setDisplayModel(model);
        layers.setDisplayModel(model);
    }


    public void removeLayers() {
        layers.clear();
        minZoom = maxZoom = 5;
    }


    public void setMapViewZoomLimit(MapView mapView) {
        mapView.setZoomLevelMin((byte)minZoom);
        mapView.setZoomLevelMax((byte)maxZoom);
    }

    @Override
    public void draw(final BoundingBox box, final byte zoom, final Canvas c, final Point tlp) {
        new InsideContext(scontext) {
            @Override
            public void run() {
                layers.draw(box, zoom, c, tlp);
            }
        };

    }


    public void reDownloadTiles() {
        layers.reDownloadTiles();
    }

    @Override
    public void onLayout(boolean changed, int l, int t, int r, int b) {}

    @Override
    public void drawInside(MapContext mcontext) {}

    @Override
    public void drawForeground(MapContext mcontext) {}

    @Override
    public boolean onTap(Point tapPos) {
        return false;
    }


    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {}

    @Override
    public void onAttached() { layers.attach(); }


    @Override
    public  void onDestroy() {
        layers.clear();
    }

    @Override
    public  void onDetached() {
        layers.detach();
    }


    private static class SubLayers {

        private boolean isAttached=false;

        private final ArrayList<MapsForgeTileLayer> layers = new ArrayList<>(10);

        public synchronized void add(MapsForgeTileLayer l) {

            layers.add(l);
            if (isAttached) l.onAttached();
        }


        public synchronized void clear() {
            for (MapsForgeTileLayer c : layers) {
                c.onDetached();
            }
            layers.clear();
        }

        public synchronized void attach() {

            for (Attachable a : layers)
                a.onAttached();

            isAttached = true;
        }

        public synchronized void detach() {

            for (Attachable a : layers) {
                a.onDetached();
            }

            isAttached = false;
        }

        public synchronized void draw(BoundingBox box, byte zoom, Canvas c, Point tlp) {
            for (Layer l : layers) {
                l.draw(box, zoom, c, tlp);
            }
        }


        public synchronized void reDownloadTiles() {
            for (MapsForgeTileLayer l : layers) {
                l.reDownloadTiles();
            }
        }


        public synchronized void setDisplayModel(DisplayModel model) {
            for(Layer l: layers) {
                l.setDisplayModel(model);
            }
        }
    }
}

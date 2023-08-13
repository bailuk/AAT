package ch.bailu.aat_lib.map.tile;

import org.mapsforge.core.graphics.Canvas;
import org.mapsforge.core.model.BoundingBox;
import org.mapsforge.core.model.Point;
import org.mapsforge.map.layer.Layer;
import org.mapsforge.map.model.DisplayModel;
import org.mapsforge.map.view.MapView;

import java.util.ArrayList;

import javax.annotation.Nonnull;

import ch.bailu.aat_lib.map.Attachable;
import ch.bailu.aat_lib.map.MapContext;
import ch.bailu.aat_lib.map.TilePainter;
import ch.bailu.aat_lib.map.layer.MapLayerInterface;
import ch.bailu.aat_lib.preferences.StorageInterface;
import ch.bailu.aat_lib.service.ServicesInterface;

public class MapsForgeTileLayerStack extends Layer implements MapLayerInterface {

    private final SubLayers layers = new SubLayers();

    private final ServicesInterface scontext;

    int minZoom=5, maxZoom = 5;


    public MapsForgeTileLayerStack(ServicesInterface sc) {
        scontext = sc;
    }


    public void addLayer(TileProvider tileProvider, TilePainter tilePainter) {
        MapsForgeTileLayer layer = new MapsForgeTileLayer(scontext, tileProvider, tilePainter);

        layer.setDisplayModel(getDisplayModel());

        layers.add(layer);

        tileProvider.addObserver(this::requestRedraw);

        maxZoom = Math.max(tileProvider.getMaximumZoomLevel(), maxZoom);
        minZoom = Math.min(tileProvider.getMinimumZoomLevel(), minZoom);


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
        scontext.insideContext(() -> layers.draw(box, zoom, c, tlp));

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
    public boolean onTap(ch.bailu.aat_lib.util.Point tapPos) {
        return false;
    }


    @Override
    public void onPreferencesChanged(@Nonnull StorageInterface s, @Nonnull String key) {}

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

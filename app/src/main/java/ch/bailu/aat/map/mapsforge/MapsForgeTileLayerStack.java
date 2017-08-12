package ch.bailu.aat.map.mapsforge;

import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.Rect;

import org.mapsforge.core.graphics.Canvas;
import org.mapsforge.core.graphics.TileBitmap;
import org.mapsforge.core.model.BoundingBox;
import org.mapsforge.core.model.Point;
import org.mapsforge.map.android.graphics.AndroidGraphicFactory;
import org.mapsforge.map.layer.Layer;
import org.mapsforge.map.layer.TilePosition;
import org.mapsforge.map.model.common.Observer;
import org.mapsforge.map.util.LayerUtil;
import org.mapsforge.map.view.MapView;

import java.util.ArrayList;
import java.util.List;

import ch.bailu.aat.map.MapContext;
import ch.bailu.aat.map.layer.MapLayerInterface;
import ch.bailu.aat.map.tile.TileProviderInterface;
import ch.bailu.aat.services.ServiceContext;
import ch.bailu.aat.util.ui.AppLog;

public class MapsForgeTileLayerStack extends Layer implements MapLayerInterface, Observer {


    private boolean attached = false;

    private final ArrayList<Container> layers = new ArrayList<>(10);

    private final ServiceContext scontext;

    public MapsForgeTileLayerStack(ServiceContext sc) {
        scontext = sc;
    }


    public void addLayer(TileProviderInterface provider) {
        addLayer(provider,
                provider.getMinimumZoomLevel(),
                provider.getMaximumZoomLevel());
    }


    public void addLayer(TileProviderInterface provider, int z1, int z2) {
        layers.add(new Container(provider, z1, z2));
        provider.addObserver(this);
    }


    public void removeLayers() {
        detachLayers();
        layers.clear();
    }

    private void detachLayers() {
        for (Container c : layers) {
            c.provider.detach();
        }
    }

    public void setMapViewZoomLimit(MapView mapView) {
        int minZoom=6, maxZoom = 10;

        for (Container c : layers) {
            maxZoom = Math.max(c.maxZoom, maxZoom);
            minZoom = Math.min(c.minZoom, minZoom);
        }

        mapView.setZoomLevelMin((byte)minZoom);
        mapView.setZoomLevelMax((byte)maxZoom);
    }

    @Override
    public void draw(BoundingBox box, byte zoom, Canvas c, Point tlp) {
        if (attached && scontext.lock()) {
            for (Container l: layers) {
                if (l.isZoomSupported(zoom)) {
                    l.provider.attach();
                    l.draw(box, zoom, c, tlp, displayModel.getTileSize());
                } else {
                    l.provider.detach();
                }
            }
            scontext.free();
        }
    }


    public void reDownloadTiles() {
        for (Container c: layers) {
            c.provider.reDownloadTiles();
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
    public boolean onTap(Point tapPos) {
        return false;
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

    }

    @Override
    public void onAttached() { attached = true; }


    @Override
    public void onDestroy() {
        removeLayers();
    }

    @Override
    public void onDetached() {
        attached = false;
        detachLayers();
    }

    @Override
    public void onChange() {
        requestRedraw();
    }


    private static class Container {
        public final int minZoom, maxZoom;
        public final TileProviderInterface provider;

        private final Paint paint = new Paint();


        public Container(TileProviderInterface p, int a, int b) {
            int min=Math.min(a, b);
            int max=Math.max(a, b);
            minZoom = Math.max(min, p.getSource().getMinimumZoomLevel());
            maxZoom = Math.min(max, p.getSource().getMaximumZoomLevel());

            provider = p;

            paint.setAlpha(p.getSource().getAlpha());
            paint.setFlags(p.getSource().getPaintFlags());
        }


        public void draw (BoundingBox box, byte zoom, Canvas canvas, Point tlp, int tileSize) {
            List<TilePosition> tilePositions = LayerUtil.getTilePositions(box, zoom, tlp, tileSize);

            provider.setCapacity(tilePositions.size());

            for (TilePosition tilePosition : tilePositions) {
                if (provider.contains(tilePosition.tile)) {
                    provider.get(tilePosition.tile);
                }
            }


            for (TilePosition tilePosition : tilePositions) {
                final TileBitmap bitmap = provider.get(tilePosition.tile);

                if (bitmap != null) {
                    final Point p = tilePosition.point;
                    final Rect r=new Rect();

                    r.left = (int) Math.round(p.x);
                    r.top = (int) Math.round(p.y);
                    r.right = r.left + tileSize;
                    r.bottom = r.top + tileSize;

                    Bitmap androidbitmap = AndroidGraphicFactory.getBitmap(bitmap);
                    if (androidbitmap == null) {
                        AppLog.d(this, provider.getSource().getName());
                    } else {

                        AndroidGraphicFactory.getCanvas(canvas).
                                drawBitmap(androidbitmap, null, r, paint);
                    }
                }
            }
        }

        public boolean isZoomSupported(byte zoom) {
            return (zoom <= maxZoom && zoom >= minZoom);
        }


    }
}

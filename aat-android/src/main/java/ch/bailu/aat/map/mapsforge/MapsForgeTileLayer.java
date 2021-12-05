package ch.bailu.aat.map.mapsforge;

import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.Rect;

import org.mapsforge.core.graphics.Canvas;
import org.mapsforge.core.graphics.TileBitmap;
import org.mapsforge.core.model.BoundingBox;
import org.mapsforge.core.model.LatLong;
import org.mapsforge.core.model.Point;
import org.mapsforge.map.android.graphics.AndroidGraphicFactory;
import org.mapsforge.map.layer.Layer;
import org.mapsforge.map.layer.TilePosition;
import org.mapsforge.map.model.common.Observer;
import org.mapsforge.map.util.LayerUtil;

import java.util.List;

import ch.bailu.aat.map.AndroidDraw;
import ch.bailu.aat.map.tile.TileProvider;
import ch.bailu.aat_lib.map.MapContext;
import ch.bailu.aat_lib.map.layer.MapLayerInterface;
import ch.bailu.aat_lib.preferences.StorageInterface;
import ch.bailu.aat_lib.service.InsideContext;
import ch.bailu.aat_lib.service.ServicesInterface;

public class MapsForgeTileLayer extends Layer implements MapLayerInterface, Observer {

    private final TileProvider provider;

    private boolean isAttached = false;

    private final ServicesInterface services;

    private final Paint paint = new Paint();
    private final Rect rect = new Rect();

    public MapsForgeTileLayer(ServicesInterface sc, TileProvider p) {
        services = sc;
        provider = p;

        paint.setAlpha(p.getSource().getAlpha());
        paint.setFlags(p.getSource().getPaintFlags());

        provider.addObserver(this);


    }


    @Override
    public void draw(final BoundingBox box, final byte zoom, final Canvas c, final Point tlp) {

        synchronized (provider) {
            new InsideContext(services) {
                @Override
                public void run() {
                    if (detachAttach(zoom)) {
                        draw(
                                box,
                                zoom,
                                c,
                                tlp,
                                displayModel.getTileSize());

                    }
                }
            };
        }
    }


    private void draw (BoundingBox box, byte zoom, Canvas canvas, Point tlp, int tileSize) {

        List<TilePosition> tilePositions = LayerUtil.getTilePositions(box, zoom, tlp, tileSize);

        provider.preload(tilePositions);

        for (TilePosition tilePosition : tilePositions) {
            final TileBitmap tileBitmap = provider.get(tilePosition.tile);

            if (tileBitmap != null) {
                final Bitmap bitmap = AndroidGraphicFactory.getBitmap(tileBitmap);

                if (bitmap != null) {
                    final Point p = tilePosition.point;

                    rect.left = (int) Math.round(p.x);
                    rect.top = (int) Math.round(p.y);
                    rect.right = rect.left + tileSize;
                    rect.bottom = rect.top + tileSize;

                    AndroidDraw.convert(canvas).drawBitmap(bitmap, null, rect, paint);
                }
            }
        }
    }


    @Override
    public void onChange() {
        requestRedraw();
    }


    @Override
    public void onAttached() {
        synchronized(provider) {
            isAttached = true;
        }
    }


    @Override
    public void onDetached() {
        synchronized(provider) {
            isAttached = false;
            provider.onDetached();
        }
    }


    private boolean detachAttach(int zoom) {

        if (isVisible() && isZoomSupported(zoom) && isAttached) {
            provider.onAttached();
        } else  {
            provider.onDetached();
        }

        return provider.isAttached();
    }


    private boolean isZoomSupported(int zoom) {
        return (provider.getMinimumZoomLevel() <= zoom && provider.getMaximumZoomLevel() >= zoom);
    }


    public void reDownloadTiles() {
        synchronized (provider) {
            provider.reDownloadTiles();
        }
    }


    @Override
    public void onLayout(boolean changed, int l, int t, int r, int b) {}

    @Override
    public void drawInside(MapContext mcontext) {}

    @Override
    public void drawForeground(MapContext mcontext) {}

    @Override
    public boolean onTap(ch.bailu.aat_lib.map.Point tapPos) {
        return false;
    }


    @Override
    public boolean onTap(LatLong tapLatLong, Point layerXY, Point tapXY) {return false;}

    @Override
    public void onPreferencesChanged(StorageInterface s, String key) {}

}

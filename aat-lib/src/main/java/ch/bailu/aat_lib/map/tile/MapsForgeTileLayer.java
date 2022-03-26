package ch.bailu.aat_lib.map.tile;


import org.jetbrains.annotations.NotNull;
import org.mapsforge.core.graphics.Canvas;
import org.mapsforge.core.graphics.Paint;
import org.mapsforge.core.graphics.TileBitmap;
import org.mapsforge.core.model.BoundingBox;
import org.mapsforge.core.model.LatLong;
import org.mapsforge.core.model.Point;
import org.mapsforge.map.layer.Layer;
import org.mapsforge.map.layer.TilePosition;
import org.mapsforge.map.model.common.Observer;
import org.mapsforge.map.util.LayerUtil;

import java.util.List;

import ch.bailu.aat_lib.map.MapContext;
import ch.bailu.aat_lib.map.TilePainter;
import ch.bailu.aat_lib.map.layer.MapLayerInterface;
import ch.bailu.aat_lib.map.tile.TileProvider;
import ch.bailu.aat_lib.preferences.StorageInterface;
import ch.bailu.aat_lib.service.InsideContext;
import ch.bailu.aat_lib.service.ServicesInterface;
import ch.bailu.aat_lib.util.Rect;

public class MapsForgeTileLayer extends Layer implements MapLayerInterface, Observer {

    private final TileProvider tileProvider;
    private final TilePainter tilePainter;

    private boolean isAttached = false;

    private final ServicesInterface services;

    private final Paint paint;
    private final Rect rect = new Rect();

    public MapsForgeTileLayer(ServicesInterface sc, TileProvider tileProvider, TilePainter tilePainter) {
        services = sc;
        this.tileProvider = tileProvider;
        this.tilePainter = tilePainter;

        paint = tilePainter.createPaint(tileProvider.getSource());

        this.tileProvider.addObserver(this);
    }


    @Override
    public void draw(final BoundingBox box, final byte zoom, final Canvas c, final Point tlp) {

        synchronized (tileProvider) {
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

        tileProvider.preload(tilePositions);

        for (TilePosition tilePosition : tilePositions) {
            final TileBitmap tileBitmap = tileProvider.get(tilePosition.tile);

            if (tileBitmap != null) {
                final Point p = tilePosition.point;

                rect.left = (int) Math.round(p.x);
                rect.top = (int) Math.round(p.y);
                rect.right = rect.left + tileSize;
                rect.bottom = rect.top + tileSize;

                tilePainter.paint(tileBitmap, canvas, rect, paint);
            }
        }
    }


    @Override
    public void onChange() {
        requestRedraw();
    }


    @Override
    public void onAttached() {
        synchronized(tileProvider) {
            isAttached = true;
        }
    }


    @Override
    public void onDetached() {
        synchronized(tileProvider) {
            isAttached = false;
            tileProvider.onDetached();
        }
    }


    private boolean detachAttach(int zoom) {

        if (isVisible() && isZoomSupported(zoom) && isAttached) {
            tileProvider.onAttached();
        } else  {
            tileProvider.onDetached();
        }

        return tileProvider.isAttached();
    }


    private boolean isZoomSupported(int zoom) {
        return (tileProvider.getMinimumZoomLevel() <= zoom && tileProvider.getMaximumZoomLevel() >= zoom);
    }


    public void reDownloadTiles() {
        synchronized (tileProvider) {
            tileProvider.reDownloadTiles();
        }
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
    public boolean onTap(LatLong tapLatLong, Point layerXY, Point tapXY) {return false;}

    @Override
    public void onPreferencesChanged(@NotNull StorageInterface s, @NotNull String key) {}

}

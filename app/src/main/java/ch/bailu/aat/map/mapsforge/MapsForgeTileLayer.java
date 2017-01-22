package ch.bailu.aat.map.mapsforge;

import android.content.SharedPreferences;
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

import ch.bailu.aat.map.MapContext;
import ch.bailu.aat.map.layer.MapLayerInterface;
import ch.bailu.aat.map.tile.TileProviderInterface;

public class MapsForgeTileLayer extends Layer implements MapLayerInterface, Observer {

    private final TileProviderInterface provider;

    private final Paint paint = new Paint();

    private boolean isAttached = false, isVisible = true, isZoomSupported = false;
    private boolean isProviderAttached = false;



    public MapsForgeTileLayer(TileProviderInterface p, int alpha) {
        provider = p;
        paint.setAlpha(alpha);
        paint.setAntiAlias(true);
    }


    @Override
    public void draw(BoundingBox box, byte zoom, Canvas c, Point tlp) {
        isZoomSupported =
                (zoom <= provider.getMaximumZoomLevel() && zoom >= provider.getMinimumZoomLevel());

        if (detachAttach()) {
            draw(
                    box,
                    zoom,
                    c,
                    tlp,
                    displayModel.getTileSize());
        }
    }


    private void draw (BoundingBox box, byte z, Canvas canvas, Point tlp, int tileSize) {
        List<TilePosition> tilePositions = LayerUtil.getTilePositions(box, z, tlp, tileSize);

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

                AndroidGraphicFactory.getCanvas(canvas).
                drawBitmap(AndroidGraphicFactory.getBitmap(bitmap), null, r, paint);
            }
        }
    }


    @Override
    public void onChange() {
        requestRedraw();
    }


    @Override
    public void setVisible(boolean requestVisible, boolean redraw) {
        isVisible = requestVisible;
        detachAttach();

        super.setVisible(requestVisible, redraw);
    }


    @Override
    public void onAttached() {
        isAttached = true;
        detachAttach();
    }


    @Override
    public void onDetached() {
        isAttached = false;
        detachAttach();
    }


    private synchronized boolean detachAttach() {
        if (isVisible && isZoomSupported && isAttached) {
            if (isProviderAttached == false) {
                provider.onAttached();
                provider.addObserver(this);
                isProviderAttached = true;
            }
        } else if (isProviderAttached) {
            provider.removeObserver(this);
            provider.onDetached();
            isProviderAttached = false;
        }
        return isProviderAttached;
    }


    public void reDownloadTiles() {
        provider.reDownloadTiles();
    }


    @Override
    public void onLayout(boolean changed, int l, int t, int r, int b) {}

    @Override
    public void drawInside(MapContext mcontext) {}

    @Override
    public void drawForeground(MapContext mcontext) {}


    @Override
    public boolean onTap(LatLong tapLatLong, Point layerXY, Point tapXY) {return false;}

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {}

}

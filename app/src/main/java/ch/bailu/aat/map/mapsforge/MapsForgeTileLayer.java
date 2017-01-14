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
import ch.bailu.aat.services.cache.TileObject;
import ch.bailu.aat.util.ui.AppLog;

public class MapsForgeTileLayer extends Layer implements MapLayerInterface, Observer {
    public final static int TRANSPARENT = 150;
    public final static int OPAQUE = 255;

    private final TileProviderInterface provider;

    private final Paint tilePaint = new Paint();
    private final Rect rect = new Rect();

    private boolean isMapAttached = false;


    public MapsForgeTileLayer(TileProviderInterface p, int alpha) {
        provider = p;

        tilePaint.setAlpha(alpha);



    }


    @Override
    public void draw(BoundingBox box, byte z, Canvas c, Point tlp) {
        final int tileSize =  displayModel.getTileSize();
        android.graphics.Canvas canvas = AndroidGraphicFactory.getCanvas(c);

        List<TilePosition> tilePositions = LayerUtil.getTilePositions(box, z, tlp, tileSize);

        provider.setCapacity(tilePositions.size());


        for (TilePosition tilePosition : tilePositions) {
            if (provider.contains(tilePosition.tile)) provider.get(tilePosition.tile);
        }


        for (TilePosition tilePosition : tilePositions) {
            final Point p = tilePosition.point;

            rect.left   = (int) Math.round(p.x);
            rect.top    = (int) Math.round(p.y);
            rect.right  = rect.left + tileSize;
            rect.bottom = rect.top + tileSize;

            final TileBitmap bitmap = provider.get(tilePosition.tile);

            if (bitmap != null) {
                canvas.drawBitmap(AndroidGraphicFactory.getBitmap(bitmap), null, rect, tilePaint);
            }
        }
    }


    @Override
    public void onChange() {
        requestRedraw();
    }

    @Override
    public void setVisible(boolean requestVisible, boolean redraw) {
        if (isMapAttached && (requestVisible != isVisible())) {

            if (requestVisible) {
                attach();
            } else {
                detach();
            }
        }

        super.setVisible(requestVisible, redraw);
    }


    @Override
    public void onAttached() {
        isMapAttached = true;
        if (isVisible()) {
            attach();
        }
    }

    @Override
    public void onDetached() {
        AppLog.d(this, "onDetached()");
        isMapAttached = false;
        if (isVisible()) {
            detach();
        }
    }


    private void attach() {
        provider.onAttached();
        provider.addObserver(this);
    }

    private void detach() {
        provider.removeObserver(this);
        provider.onDetached();
    }


    @Override
    public void onLayout(boolean changed, int l, int t, int r, int b) {}

    @Override
    public void drawInside(MapContext mcontext) {}

    @Override
    public void drawOnTop(MapContext mcontext) {}


    @Override
    public boolean onTap(LatLong tapLatLong, Point layerXY, Point tapXY) {return false;}

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {}

}

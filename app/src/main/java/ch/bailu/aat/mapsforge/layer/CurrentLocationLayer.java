package ch.bailu.aat.mapsforge.layer;


import android.content.SharedPreferences;
import android.graphics.Point;

import org.mapsforge.core.graphics.Canvas;
import org.mapsforge.core.graphics.Paint;
import org.mapsforge.core.graphics.Style;
import org.mapsforge.core.model.BoundingBox;
import org.mapsforge.core.model.LatLong;
import org.mapsforge.map.android.graphics.AndroidGraphicFactory;

import ch.bailu.aat.coordinates.LatLongE6;
import ch.bailu.aat.dispatcher.DispatcherInterface;
import ch.bailu.aat.dispatcher.OnContentUpdatedInterface;
import ch.bailu.aat.gpx.GpxInformation;
import ch.bailu.aat.gpx.InfoID;
import ch.bailu.aat.helpers.AppTheme;
import ch.bailu.aat.mapsforge.layer.context.MapContext;

public class CurrentLocationLayer extends MapsForgeLayer implements OnContentUpdatedInterface {
    private static final int MIN_RADIUS=7;
    private final static int STROKE_WIDTH=2;


    private LatLong center = new LatLong(0,0);
    private float accuracy=0f;

    private final Paint paint = AndroidGraphicFactory.INSTANCE.createPaint();

    private final MapContext mcontext;

    public CurrentLocationLayer(MapContext mc, DispatcherInterface d) {
        mcontext = mc;

        paint.setStyle(Style.STROKE);
        paint.setStrokeWidth(STROKE_WIDTH);
        paint.setColor(AppTheme.getHighlightColor());
        //paint.setAntiAlias(true);

        d.addTarget(this, InfoID.LOCATION);
    }


    @Override
    public void onContentUpdated(int iid, GpxInformation info) {
        center = new LatLongE6(info).toLatLong();
        accuracy=info.getAccuracy();

        if (mcontext.mapView.getBoundingBox().contains(center))
            requestRedraw();
    }




    @Override
    public void onLayout(boolean changed, int l, int t, int r, int b) {

    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

    }

    @Override
    public void draw(BoundingBox boundingBox, byte zoomLevel, Canvas canvas, org.mapsforge.core.model.Point topLeftPoint) {

        if (boundingBox.contains(center) && accuracy > 0) {
            Point pixel = mcontext.metrics.toPixel(center);
            int radius = Math.max(MIN_RADIUS, mcontext.metrics.distanceToPixel(accuracy));

            mcontext.draw.circle(pixel, radius, paint);
        }
    }
}

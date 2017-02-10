package ch.bailu.aat.map.layer;


import android.content.SharedPreferences;
import android.graphics.Point;

import org.mapsforge.core.graphics.Paint;
import org.mapsforge.core.graphics.Style;
import org.mapsforge.core.model.LatLong;
import org.mapsforge.map.android.graphics.AndroidGraphicFactory;

import ch.bailu.aat.coordinates.LatLongE6;
import ch.bailu.aat.dispatcher.DispatcherInterface;
import ch.bailu.aat.dispatcher.OnContentUpdatedInterface;
import ch.bailu.aat.gpx.GpxInformation;
import ch.bailu.aat.gpx.InfoID;
import ch.bailu.aat.map.MapContext;
import ch.bailu.aat.util.ui.AppTheme;

public class CurrentLocationLayer implements OnContentUpdatedInterface, MapLayerInterface {
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
        center = LatLongE6.toLatLong(info);
        accuracy=info.getAccuracy();

        if (mcontext.getMetrics().getBoundingBox().contains(center))
            mcontext.getMapView().requestRedraw();
    }




    @Override
    public void onLayout(boolean changed, int l, int t, int r, int b) {

    }

    @Override
    public void drawInside(MapContext mcontext) {
        if (mcontext.getMetrics().getBoundingBox().contains(center) && accuracy > 0) {
            Point pixel = mcontext.getMetrics().toPixel(center);
            int radius = Math.max(MIN_RADIUS, mcontext.getMetrics().distanceToPixel(accuracy));

            mcontext.draw().circle(pixel, radius, paint);
        }
    }

    @Override
    public boolean onTap( org.mapsforge.core.model.Point tapXY) {
        return false;
    }

    @Override
    public void drawForeground(MapContext mcontext) {

    }


    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

    }

    @Override
    public void onAttached() {

    }

    @Override
    public void onDetached() {

    }
}

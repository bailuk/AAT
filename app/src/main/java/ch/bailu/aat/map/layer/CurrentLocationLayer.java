package ch.bailu.aat.map.layer;


import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Build;
import android.provider.Settings;

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
import ch.bailu.aat.util.ui.AppLog;
import ch.bailu.aat.util.ui.AppTheme;

public class CurrentLocationLayer implements OnContentUpdatedInterface, MapLayerInterface {
    private static final int MIN_RADIUS=7;
    private final static int STROKE_WIDTH=2;

    private final static Satturate COLOR = new Satturate(AppTheme.getHighlightColor());

    private GpxInformation center = GpxInformation.NULL;

    private final Paint paint = AndroidGraphicFactory.INSTANCE.createPaint();

    private final MapContext mcontext;



    public CurrentLocationLayer(MapContext mc, DispatcherInterface d) {
        mcontext = mc;

        paint.setStyle(Style.STROKE);
        paint.setStrokeWidth(STROKE_WIDTH);
        //paint.setColor(AppTheme.getHighlightColor());

        //paint.setAntiAlias(true);

        d.addTarget(this, InfoID.LOCATION);
    }


    private static class Satturate {
        private final static int STEPS=60;

        private final short r[] = new short[STEPS];
        private final short g[] = new short[STEPS];
        private final short b[] = new short[STEPS];


        public Satturate(int c) {
            int r = Color.red(c);
            int g = Color.green(c);
            int b = Color.blue(c);

            int max = Math.max(r, Math.max(g,b));

            fill(r, max, this.r);
            fill(g, max, this.g);
            fill(b, max, this.b);
        }


        private void fill(int base, int max, short t[]) {
            for (int i=0; i<STEPS; i++) {
                float step = (max - base) / STEPS;

                t[i] = (short) (base+ Math.round(step * i));
            }
        }


        public int colorFromTimeStamp(long time) {

            return color((int) (System.currentTimeMillis() - time)/1000);
        }

        public int color(int i) {
            i = Math.min(STEPS-1, Math.abs(i));

            return Color.rgb(r[i], g[i], b[i]);
        }
    }

    @Override
    public void onContentUpdated(int iid, GpxInformation info) {
        center = info;

        if (contains(center))
            mcontext.getMapView().requestRedraw();
    }




    @Override
    public void onLayout(boolean changed, int l, int t, int r, int b) {

    }


    @Override
    public void drawInside(MapContext mcontext) {
        if (contains(center) && center.getAccuracy() > 0) {
            Point pixel = mcontext.getMetrics().toPixel(center);
            int radius = Math.max(MIN_RADIUS, mcontext.getMetrics().distanceToPixel(center.getAccuracy()));

            paint.setColor(COLOR.colorFromTimeStamp(center.getTimeStamp()));

            mcontext.draw().circle(pixel, radius, paint);
        }
    }

    private boolean contains(GpxInformation center) {
        return mcontext.getMetrics().getBoundingBox().contains(
                new LatLong(center.getLatitude(), center.getLongitude()));
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

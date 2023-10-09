package ch.bailu.aat_lib.preferences.location;


import org.mapsforge.core.graphics.Paint;
import org.mapsforge.core.graphics.Style;
import org.mapsforge.core.model.LatLong;

import javax.annotation.Nonnull;

import ch.bailu.aat_lib.app.AppColor;
import ch.bailu.aat_lib.dispatcher.DispatcherInterface;
import ch.bailu.aat_lib.dispatcher.OnContentUpdatedInterface;
import ch.bailu.aat_lib.gpx.GpxInformation;
import ch.bailu.aat_lib.gpx.InfoID;
import ch.bailu.aat_lib.map.MapContext;
import ch.bailu.aat_lib.util.Point;
import ch.bailu.aat_lib.map.layer.MapLayerInterface;
import ch.bailu.aat_lib.preferences.StorageInterface;
import ch.bailu.aat_lib.lib.color.ARGB;

public final class CurrentLocationLayer implements OnContentUpdatedInterface, MapLayerInterface {
    private static final int MIN_RADIUS=7;
    private final static int STROKE_WIDTH=2;

    private final static Saturate COLOR = new Saturate(AppColor.HL_ORANGE);
    private GpxInformation center = GpxInformation.NULL;
    private final Paint paint;
    private final MapContext mcontext;


    public CurrentLocationLayer(MapContext mc, DispatcherInterface d) {
        mcontext = mc;

        paint = mc.draw().createPaint();
        paint.setStyle(Style.STROKE);
        paint.setStrokeWidth(STROKE_WIDTH);

        d.addTarget(this, InfoID.LOCATION);
    }

    private static class Saturate {
        private final static int STEPS=60;

        private final short[] r = new short[STEPS];
        private final short[] g = new short[STEPS];
        private final short[] b = new short[STEPS];

        private final ARGB rgb;
        public Saturate(int c) {
            rgb = new ARGB(c);

            int max = Math.max(rgb.red(), Math.max(rgb.green(),rgb.blue()));

            fill(rgb.red(), max, r);
            fill(rgb.green(), max, g);
            fill(rgb.blue(), max, b);
        }

        private void fill(int base, int max, short[] t) {
            for (int i=0; i<STEPS; i++) {
                float step = (max - base) / (float) STEPS;

                t[i] = (short) (base+ Math.round(step * i));
            }
        }

        public int colorFromTimeStamp(long time) {

            return color((int) (System.currentTimeMillis() - time)/1000);
        }

        public int color(int i) {
            i = Math.min(STEPS-1, Math.abs(i));
            return new ARGB(rgb.alpha(), r[i], g[i], b[i]).toInt();
        }
    }

    @Override
    public void onContentUpdated(int iid, @Nonnull GpxInformation info) {
        center = info;

        if (contains(center))
            mcontext.getMapView().requestRedraw();
    }

    @Override
    public void onLayout(boolean changed, int l, int t, int r, int b) {}

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
    public boolean onTap(Point tapXY) {
        return false;
    }

    @Override
    public void drawForeground(MapContext mcontext) {}

    @Override
    public void onPreferencesChanged(@Nonnull StorageInterface s, @Nonnull String key) {}

    @Override
    public void onAttached() {}

    @Override
    public void onDetached() {}
}

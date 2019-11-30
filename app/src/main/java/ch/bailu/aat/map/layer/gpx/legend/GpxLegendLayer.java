package ch.bailu.aat.map.layer.gpx.legend;

import android.content.SharedPreferences;
import android.graphics.Color;

import org.mapsforge.core.graphics.Paint;
import org.mapsforge.core.model.Point;

import ch.bailu.aat.map.MapContext;
import ch.bailu.aat.map.MapMetrics;
import ch.bailu.aat.map.MapPaint;
import ch.bailu.aat.map.layer.gpx.GpxLayer;

public final class GpxLegendLayer extends GpxLayer {
    private final LegendWalker walker;

    private Paint backgroundPaint, framePaint;

    private int color = Color.WHITE;

    public GpxLegendLayer(LegendWalker w) {
        walker=w;

        initPaint();
    }

    @Override
    public void drawInside(MapContext mcontext) {
        if (color != getColor()) {
            color = getColor();

            initPaint(mcontext.getMetrics());
        }

        walker.init(mcontext, backgroundPaint, framePaint);
        walker.walkTrack(getGpxList());
    }



    private void initPaint(MapMetrics metrics) {
        backgroundPaint = MapPaint.createBackgroundPaint(color);
        framePaint = MapPaint.createEdgePaintLine(metrics.getDensity());
        framePaint.setColor(color);
    }


    private void initPaint() {
        backgroundPaint = MapPaint.createBackgroundPaint(color);
        framePaint = MapPaint.createBackgroundPaint(color);
    }


    @Override
    public boolean onTap(Point tapXY) {
        return false;
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

package ch.bailu.aat.map.layer.gpx.legend;

import android.content.SharedPreferences;
import android.graphics.Color;

import org.mapsforge.core.graphics.Paint;
import org.mapsforge.core.model.Point;

import ch.bailu.aat.map.MapContext;
import ch.bailu.aat.map.MapPaint;
import ch.bailu.aat.map.layer.gpx.GpxLayer;

public class GpxLegendLayer extends GpxLayer {
    private final LegendWalker walker;

    private Paint backgroundPaint, framePaint;

    private int color = Color.WHITE;

    public GpxLegendLayer(LegendWalker w) {
        walker=w;


        backgroundPaint =
                MapPaint.createBackgroundPaint(getColor());
        framePaint = MapPaint.createBackgroundPaint(getColor());
        framePaint.setColor(getColor());
    }

    @Override
    public void drawInside(MapContext mcontext) {
        if (color != getColor()) {
            backgroundPaint = MapPaint.createBackgroundPaint(getColor());
            framePaint = MapPaint.createEdgePaintLine(mcontext.getMetrics().getDensity());
            framePaint.setColor(getColor());
            color = getColor();

        }

        walker.init(mcontext, backgroundPaint, framePaint);
        walker.walkTrack(getGpxList());
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

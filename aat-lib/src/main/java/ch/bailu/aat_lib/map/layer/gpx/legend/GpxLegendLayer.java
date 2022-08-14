package ch.bailu.aat_lib.map.layer.gpx.legend;

import org.mapsforge.core.graphics.Paint;

import javax.annotation.Nonnull;

import ch.bailu.aat_lib.lib.color.ARGB;
import ch.bailu.aat_lib.map.MapContext;
import ch.bailu.aat_lib.map.MapMetrics;
import ch.bailu.aat_lib.map.MapPaint;
import ch.bailu.aat_lib.map.layer.gpx.GpxLayer;
import ch.bailu.aat_lib.preferences.StorageInterface;
import ch.bailu.aat_lib.util.Point;

public final class GpxLegendLayer extends GpxLayer {
    private final LegendWalker walker;

    private Paint backgroundPaint, framePaint;

    private int color = ARGB.WHITE;

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

    @Override
    public boolean onTap(Point tapPos) {
        return false;
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
    public void onPreferencesChanged(@Nonnull StorageInterface s, @Nonnull String key) {}

    @Override
    public void onAttached() {}

    @Override
    public void onDetached() {}
}

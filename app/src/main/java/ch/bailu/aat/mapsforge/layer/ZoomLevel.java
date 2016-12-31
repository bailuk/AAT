package ch.bailu.aat.mapsforge.layer;

import android.content.SharedPreferences;

import org.mapsforge.core.graphics.Canvas;
import org.mapsforge.core.model.BoundingBox;
import org.mapsforge.core.model.Point;

import java.util.Locale;

import ch.bailu.aat.helpers.AppDensity;
import ch.bailu.aat.mapsforge.layer.context.MapContext;

public class ZoomLevel extends MapsForgeLayer {

    private final MapsForgeCanvas canvasHelper;

    private final MapContext mcontext;

    public ZoomLevel(MapContext cl, AppDensity d) {
        canvasHelper = new MapsForgeCanvas(cl.context, d);
        mcontext = cl;
    }



    @Override
    public void onLayout(boolean changed, int l, int t, int r, int b) {

    }



    @Override
    public void draw(BoundingBox boundingBox, byte zoomLevel, Canvas canvas, Point topLeftPoint) {

        mcontext.draw.textTop(String.format((Locale)null,"Zoomlevel*: %d", (int)zoomLevel), 2);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {

    }
}

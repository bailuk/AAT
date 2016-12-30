package ch.bailu.aat.mapsforge.layer;

import org.mapsforge.core.graphics.Canvas;
import org.mapsforge.core.model.BoundingBox;
import org.mapsforge.core.model.Point;

import java.util.Locale;

import ch.bailu.aat.gpx.GpxInformation;
import ch.bailu.aat.helpers.AppDensity;

public class ZoomLevel extends MapsForgeLayer {

    private final MapsForgeCanvas canvasHelper;

    private final ContextLayer clayer;

    public ZoomLevel(ContextLayer cl, AppDensity d) {
        canvasHelper = new MapsForgeCanvas(cl.getContext(), d);
        clayer = cl;
    }


    @Override
    public void onSharedPreferenceChanged(String key) {

    }

    @Override
    public void onLayout(boolean changed, int l, int t, int r, int b) {

    }

    @Override
    public void onContentUpdated(int iid, GpxInformation info) {

    }

    @Override
    public void draw(BoundingBox boundingBox, byte zoomLevel, Canvas canvas, Point topLeftPoint) {

        clayer.drawTextTop(String.format((Locale)null,"Zoomlevel*: %d", (int)zoomLevel), 2);
    }
}

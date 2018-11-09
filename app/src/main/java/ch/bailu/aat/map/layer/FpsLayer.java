package ch.bailu.aat.map.layer;

import android.content.SharedPreferences;

import org.mapsforge.core.model.Point;

import ch.bailu.aat.map.MapContext;

public class FpsLayer implements MapLayerInterface {

    private static long totalTime;
    private static long frameCount;
    private static long startTime;
    private static long frameTime;


    @Override
    public void onLayout(boolean changed, int l, int t, int r, int b) {

    }

    @Override
    public void drawInside(MapContext mcontext) {

    }

    @Override
    public void drawForeground(MapContext mcontext) {
        if (frameCount > 0 ) {
            long averageTime = totalTime / frameCount;
            mcontext.draw().textTop(frameCount + " " + frameTime + "ms, " + averageTime + "ms", 2);
        }
    }

    @Override
    public boolean onTap(Point tapPos) {
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


    public static void start() {
        startTime = System.currentTimeMillis();
    }

    public static void stop() {
        frameTime = System.currentTimeMillis() - startTime;
        totalTime += frameTime;
        frameCount++;
    }
}

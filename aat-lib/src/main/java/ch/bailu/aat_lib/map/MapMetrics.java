package ch.bailu.aat_lib.map;


import org.mapsforge.core.model.BoundingBox;
import org.mapsforge.core.model.LatLong;

import ch.bailu.aat_lib.coordinates.BoundingBoxE6;
import ch.bailu.aat_lib.coordinates.LatLongInterface;

public interface MapMetrics {
    AppDensity getDensity();

    int getLeft();
    int getRight();
    int getTop();
    int getBottom();
    int getWidth();
    int getHeight();


    float pixelToDistance(int pixel);
    int distanceToPixel(float meter);
    int getShortDistance();

    Point getCenterPixel();

    boolean isVisible(BoundingBoxE6 box);
    boolean isVisible(LatLongInterface point);
    Rect toMapPixels(BoundingBoxE6 box);
    Point toPixel(LatLongInterface tp);
    Point toPixel(LatLong p);
    LatLong fromPixel(int x, int y);

    BoundingBox getBoundingBox();

    int getZoomLevel();
}

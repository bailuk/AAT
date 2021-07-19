package ch.bailu.aat_lib.map;


import org.mapsforge.core.model.BoundingBox;
import org.mapsforge.core.model.LatLong;

import ch.bailu.aat_lib.coordinates.BoundingBoxE6;
import ch.bailu.aat_lib.coordinates.LatLongE6Interface;

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
    boolean isVisible(LatLongE6Interface point);
    Rect toMapPixels(BoundingBoxE6 box);
    Point toPixel(LatLongE6Interface tp);
    Point toPixel(LatLong p);
    LatLong fromPixel(int x, int y);

    BoundingBox getBoundingBox();

    int getZoomLevel();
}

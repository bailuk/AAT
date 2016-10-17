// Created by plusminus on 21:37:08 - 27.09.2008
package org.osmdroid.views;

import android.graphics.Point;

import org.osmdroid.api.IGeoPoint;
import org.osmdroid.api.IMapController;
import org.osmdroid.util.BoundingBoxE6;
import org.osmdroid.views.util.MyMath;
import org.osmdroid.views.util.constants.MapViewConstants;

/**
 *
 * @author Nicolas Gramlich
 */
public class MapController implements IMapController, MapViewConstants {

    private final MapView mOsmv;
    private AbstractAnimationRunner mCurrentAnimationRunner;


    public MapController(final MapView osmv) {
        this.mOsmv = osmv;
    }


    public void zoomToSpan(final BoundingBoxE6 bb) {
        zoomToSpan(bb.getLatitudeSpanE6(), bb.getLongitudeSpanE6());
    }

    // TODO rework zoomToSpan
    @Override
    public void zoomToSpan(final int reqLatSpan, final int reqLonSpan) {
        if (reqLatSpan <= 0 || reqLonSpan <= 0) {
            return;
        }

        final BoundingBoxE6 bb = this.mOsmv.getBoundingBox();
        final int curZoomLevel = this.mOsmv.getZoomLevel();

        final int curLatSpan = bb.getLatitudeSpanE6();
        final int curLonSpan = bb.getLongitudeSpanE6();

        final float diffNeededLat = (float) reqLatSpan / curLatSpan; // i.e. 600/500 = 1,2
        final float diffNeededLon = (float) reqLonSpan / curLonSpan; // i.e. 300/400 = 0,75

        final float diffNeeded = Math.max(diffNeededLat, diffNeededLon); // i.e. 1,2

        if (diffNeeded > 1) { // Zoom Out
            this.mOsmv.setZoomLevel(curZoomLevel - MyMath.getNextSquareNumberAbove(diffNeeded));
        } else if (diffNeeded < 0.5) { // Can Zoom in
            this.mOsmv.setZoomLevel(curZoomLevel + MyMath.getNextSquareNumberAbove(1 / diffNeeded)
                    - 1);
        }
    }



    /**
     * Set the map view to the given center. There will be no animation.
     */
    @Override
    public void setCenter(final IGeoPoint point) {
        final int zoom  = this.mOsmv.getCurrentZoomLevel();

        final Point p = mOsmv.tileSystem.LatLongToPixelXY(point.getLatitudeE6() / 1E6,
                point.getLongitudeE6() / 1E6, zoom, null);
        final int worldSize_2 = mOsmv.tileSystem.MapSize(zoom) / 2;
        this.mOsmv.scrollTo(p.x - worldSize_2, p.y - worldSize_2);
    }


    @Override
    public int setZoom(final int zoomlevel) {
        return mOsmv.setZoomLevel(zoomlevel);
    }

    @Override
    public boolean zoomIn() {
        return mOsmv.zoomIn();
    }

    @Override
    public boolean zoomOut() {
        return mOsmv.zoomOut();
    }


    private abstract class AbstractAnimationRunner extends Thread {

        // ===========================================================
        // Fields
        // ===========================================================

        protected final int mSmoothness;
        protected final int mTargetLatitudeE6, mTargetLongitudeE6;
        protected boolean mDone = false;

        protected final int mStepDuration;

        protected final int mPanTotalLatitudeE6, mPanTotalLongitudeE6;

        // ===========================================================
        // Constructors
        // ===========================================================


        public AbstractAnimationRunner(final int aTargetLatitudeE6, final int aTargetLongitudeE6,
                                       final int aSmoothness, final int aDuration) {
            this.mTargetLatitudeE6 = aTargetLatitudeE6;
            this.mTargetLongitudeE6 = aTargetLongitudeE6;
            this.mSmoothness = aSmoothness;
            this.mStepDuration = aDuration / aSmoothness;

			/* Get the current mapview-center. */
            final MapView mapview = MapController.this.mOsmv;
            final IGeoPoint mapCenter = mapview.getMapCenter();

            this.mPanTotalLatitudeE6 = mapCenter.getLatitudeE6() - aTargetLatitudeE6;
            this.mPanTotalLongitudeE6 = mapCenter.getLongitudeE6() - aTargetLongitudeE6;
        }

        @Override
        public void run() {
            onRunAnimation();
            this.mDone = true;
        }


        public abstract void onRunAnimation();
    }
}

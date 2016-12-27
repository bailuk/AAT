// Created by plusminus on 17:45:56 - 25.09.2008
package org.osmdroid.views;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.Rect;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.Scroller;

import org.osmdroid.api.IGeoPoint;
import org.osmdroid.api.IMapView;
import org.osmdroid.api.IProjection;
import org.osmdroid.events.MapListener;
import org.osmdroid.events.ScrollEvent;
import org.osmdroid.events.ZoomEvent;
import org.osmdroid.tileprovider.util.SimpleInvalidationHandler;
import org.osmdroid.util.BoundingBoxE6;
import org.osmdroid.util.constants.GeoConstants;
import org.osmdroid.views.overlay.Overlay;
import org.osmdroid.views.overlay.OverlayManager;
import org.osmdroid.views.overlay.TilesOverlay;
import org.osmdroid.views.util.constants.MapViewConstants;

import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import ch.bailu.aat.views.map.AbsTileProvider;
import microsoft.mappoint.TileSystem;

public class MapView extends ViewGroup implements IMapView, MapViewConstants {

    private int mZoomLevel = 0;

    private final OverlayManager mOverlayManager;

    private Projection mProjection;

    private final TilesOverlay mMapOverlay;

    private final GestureDetector mGestureDetector;

    /** Handles map scrolling */
    private final Scroller mScroller;
    private final AtomicInteger mTargetZoomLevel = new AtomicInteger();
    private final AtomicBoolean mIsAnimating = new AtomicBoolean(false);

    private final ScaleAnimation mZoomInAnimation;
    private final ScaleAnimation mZoomOutAnimation;

    private final MapController mController;


    private float mMultiTouchScale = 1.0f;

    protected MapListener mListener;

    // for speed (avoiding allocations)
    private final Matrix mMatrix = new Matrix();
    private final AbsTileProvider mTileProvider;

    private final Handler mTileRequestCompleteHandler;

    /* a point that will be reused to design added views */
    private final Point mPoint = new Point();


    public final TileSystem tileSystem = new TileSystem();


    private MapView(final Context context, final int tileSizePixels,
                    AbsTileProvider tileProvider,
                    final Handler tileRequestCompleteHandler, final AttributeSet attrs) {
        super(context, attrs);
        this.mController = new MapController(this);
        this.mScroller = new Scroller(context);
        tileSystem.setTileSize(tileSizePixels);

        mTileRequestCompleteHandler = tileRequestCompleteHandler == null ? new SimpleInvalidationHandler(
                this) : tileRequestCompleteHandler;
        mTileProvider = tileProvider;
        mTileProvider.setTileRequestCompleteHandler(mTileRequestCompleteHandler);

        this.mMapOverlay = new TilesOverlay(mTileProvider);
        mOverlayManager = new OverlayManager(mMapOverlay);


        mZoomInAnimation = new ScaleAnimation(1, 2, 1, 2, Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        mZoomOutAnimation = new ScaleAnimation(1, 0.5f, 1, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        mZoomInAnimation.setDuration(ANIMATION_DURATION_SHORT);
        mZoomOutAnimation.setDuration(ANIMATION_DURATION_SHORT);

        mGestureDetector = new GestureDetector(context, new MapViewGestureDetectorListener());
        mGestureDetector.setOnDoubleTapListener(new MapViewDoubleClickListener());
    }

    /**
     * Constructor used by XML layout resource (uses default tile source).
     */
    public MapView(final Context context, final AttributeSet attrs) {
        this(context, 256, null, null, attrs);
    }

    /**
     * Standard Constructor.
     */


    public MapView(final Context context, final int tileSizePixels,
                   final AbsTileProvider aTileProvider) {
        this(context, tileSizePixels, aTileProvider, null);
    }

    public MapView(final Context context, final int tileSizePixels,
                   final AbsTileProvider aTileProvider,
                   final Handler tileRequestCompleteHandler) {
        this(context, tileSizePixels, aTileProvider, tileRequestCompleteHandler,
                null);
    }

    // ===========================================================
    // Getter & Setter
    // ===========================================================

    @Override
    public MapController getController() {
        return this.mController;
    }

    /**
     * You can setTarget/remove/reorder your Overlays using the List of {@link Overlay}. The first (index
     * 0) Overlay gets drawn first, the one with the highest as the last one.
     */
    public List<Overlay> getOverlays() {
        return mOverlayManager;
    }

    public OverlayManager getOverlayManager() {
        return mOverlayManager;
    }

    public AbsTileProvider getTileProvider() {
        return mTileProvider;
    }

    public Scroller getScroller() {
        return mScroller;
    }

    public Handler getTileRequestCompleteHandler() {
        return mTileRequestCompleteHandler;
    }

    @Override
    public int getLatitudeSpan() {
        return this.getBoundingBox().getLatitudeSpanE6();
    }

    @Override
    public int getLongitudeSpan() {
        return this.getBoundingBox().getLongitudeSpanE6();
    }

    public BoundingBoxE6 getBoundingBox() {
        return getBoundingBox(getWidth(), getHeight());
    }

    public BoundingBoxE6 getBoundingBox(final int pViewWidth, final int pViewHeight) {

        final int world_2 = tileSystem.MapSize(mZoomLevel) / 2;
        final Rect screenRect = getScreenRect(null);
        screenRect.offset(world_2, world_2);

        final IGeoPoint neGeoPoint = tileSystem.PixelXYToLatLong(screenRect.right, screenRect.top,
                mZoomLevel, null);
        final IGeoPoint swGeoPoint = tileSystem.PixelXYToLatLong(screenRect.left, screenRect.bottom,
                mZoomLevel, null);

        return new BoundingBoxE6(neGeoPoint.getLatitudeE6(), neGeoPoint.getLongitudeE6(),
                swGeoPoint.getLatitudeE6(), swGeoPoint.getLongitudeE6());
    }

    /**
     * Gets the current bounds of the screen in <I>screen coordinates</I>.
     */
    public Rect getScreenRect(final Rect reuse) {
        final Rect out = reuse == null ? new Rect() : reuse;

        final int w = getWidth();
        final int h = getHeight();

        out.set(getScrollX() - w / 2, getScrollY() - h / 2, getScrollX()
                + w / 2, getScrollY() + h / 2);
        return out;
    }

    /**
     * Get a projection for converting between screen-pixel coordinates and latitude/longitude
     * coordinates. You should not hold on to this object for more than one draw, since the
     * projection of the map could change.
     *
     * @return The Projection of the map in its current state. You should not hold on to this object
     *         for more than one draw, since the projection of the map could change.
     */
    @Override
    public Projection getProjection() {
        if (mProjection == null) {
            mProjection = new Projection();
        }
        return mProjection;
    }

    void setMapCenter(final IGeoPoint aCenter) {
        this.setMapCenter(aCenter.getLatitudeE6(), aCenter.getLongitudeE6());
    }

    void setMapCenter(final int aLatitudeE6, final int aLongitudeE6) {
        final Point coords = tileSystem.LatLongToPixelXY(aLatitudeE6 / 1E6, aLongitudeE6 / 1E6,
                getZoomLevel(), null);
        final int worldSize_2 = tileSystem.MapSize(mZoomLevel) / 2;
        if (getAnimation() == null || getAnimation().hasEnded()) {
            mScroller.startScroll(getScrollX(), getScrollY(),
                    coords.x - worldSize_2 - getScrollX(), coords.y - worldSize_2 - getScrollY(),
                    500);
            postInvalidate();
        }
    }



    /**
     * @param aZoomLevel
     *            the zoom level bound by the tile source
     */
    int setZoomLevel(final int aZoomLevel) {
        final int minZoomLevel = getMinZoomLevel();
        final int maxZoomLevel = getMaxZoomLevel();

        final int newZoomLevel = Math.max(minZoomLevel, Math.min(maxZoomLevel, aZoomLevel));
        final int curZoomLevel = this.mZoomLevel;

        this.mZoomLevel = newZoomLevel;

        if (newZoomLevel > curZoomLevel) {
            // We are going from a lower-resolution plane to a higher-resolution plane, so we have
            // to do it the hard way.
            final int worldSize_current_2 = tileSystem.MapSize(curZoomLevel) / 2;
            final int worldSize_new_2 = tileSystem.MapSize(newZoomLevel) / 2;
            final IGeoPoint centerGeoPoint = tileSystem.PixelXYToLatLong(getScrollX()
                    + worldSize_current_2, getScrollY() + worldSize_current_2, curZoomLevel, null);
            final Point centerPoint = tileSystem.LatLongToPixelXY(centerGeoPoint.getLatitudeE6() / 1E6,
                    centerGeoPoint.getLongitudeE6() / 1E6, newZoomLevel, null);
            scrollTo(centerPoint.x - worldSize_new_2, centerPoint.y - worldSize_new_2);
        } else if (newZoomLevel < curZoomLevel) {
            // We are going from a higher-resolution plane to a lower-resolution plane, so we can do
            // it the easy way.
            scrollTo(getScrollX() >> curZoomLevel - newZoomLevel, getScrollY() >> curZoomLevel
                    - newZoomLevel);
        }

        mProjection = new Projection();

        // do callback on listener
        if (newZoomLevel != curZoomLevel && mListener != null) {
            final ZoomEvent event = new ZoomEvent(this, newZoomLevel);
            mListener.onZoom(event);
        }
        return this.mZoomLevel;
    }

    /**
     * Get the current ZoomLevel for the map tiles.
     *
     * @return the current ZoomLevel between 0 (equator) and 18/19(closest), depending on the tile
     *         source chosen.
     */
    @Override
    public int getZoomLevel() {
        return getPendingZoomLevel();
    }


    public int getCurrentZoomLevel() {
        return mZoomLevel;
    }


    public int getPendingZoomLevel() {
        if (isAnimating()) {
            return mTargetZoomLevel.get();
        }
        return getCurrentZoomLevel();
    }



    /**
     * Returns the minimum zoom level for the point currently at the center.
     *
     * @return The minimum zoom level for the map's current center.
     */
    public int getMinZoomLevel() {
        return mMapOverlay.getMinimumZoomLevel();
    }

    /**
     * Returns the maximum zoom level for the point currently at the center.
     *
     * @return The maximum zoom level for the map's current center.
     */
    @Override
    public int getMaxZoomLevel() {
        return mMapOverlay.getMaximumZoomLevel();
    }

    public boolean canZoomIn() {
        final int maxZoomLevel = getMaxZoomLevel();
        if (mZoomLevel >= maxZoomLevel) {
            return false;
        }
        if (isAnimating() & mTargetZoomLevel.get() >= maxZoomLevel) {
            return false;
        }
        return true;
    }

    public boolean canZoomOut() {
        final int minZoomLevel = getMinZoomLevel();
        if (mZoomLevel <= minZoomLevel) {
            return false;
        }
        if (isAnimating() && mTargetZoomLevel.get() <= minZoomLevel) {
            return false;
        }
        return true;
    }

    /**
     * Zoom in by one zoom level.
     */
    boolean zoomIn() {

        if (canZoomIn()) {
            if (isAnimating()) {
                // TODO extend zoom (and return true)
                return false;
            } else {
                mTargetZoomLevel.set(mZoomLevel + 1);
                startAnimation(mZoomInAnimation);
                return true;
            }
        } else {
            return false;
        }
    }

    boolean zoomInFixing(final IGeoPoint point) {
        setMapCenter(point); // TODO should fix on point, not center on it
        return zoomIn();
    }

    boolean zoomInFixing(final int xPixel, final int yPixel) {
        setMapCenter(xPixel, yPixel); // TODO should fix on point, not center on it
        return zoomIn();
    }

    /**
     * Zoom out by one zoom level.
     */
    boolean zoomOut() {

        if (canZoomOut()) {
            if (isAnimating()) {
                // TODO extend zoom (and return true)
                return false;
            } else {
                mTargetZoomLevel.set(mZoomLevel - 1);
                startAnimation(mZoomOutAnimation);
                return true;
            }
        } else {
            return false;
        }
    }


    /**
     * Returns the current center-point position of the map, as a GeoPoint (latitude and longitude).
     *
     * @return A GeoPoint of the map's center-point.
     */
    @Override
    public IGeoPoint getMapCenter() {
        final int world_2 = tileSystem.MapSize(mZoomLevel) / 2;
        final Rect screenRect = getScreenRect(null);
        screenRect.offset(world_2, world_2);
        return tileSystem.PixelXYToLatLong(screenRect.centerX(), screenRect.centerY(), mZoomLevel,
                null);
    }


    public void onAttach() {
        mOverlayManager.onAttach(this);
    }


    public void onDetach() {
        mOverlayManager.onDetach(this);
    }


    @Override
    public boolean onTrackballEvent(final MotionEvent event) {

        if (mOverlayManager.onTrackballEvent(event, this)) {
            return true;
        }

        scrollBy((int) (event.getX() * 25), (int) (event.getY() * 25));

        return super.onTrackballEvent(event);
    }

    @Override
    public boolean dispatchTouchEvent(final MotionEvent event) {

        if (mOverlayManager.onTouchEvent(event, this)) {
            return true;
        }

        final boolean r = super.dispatchTouchEvent(event);

        if (mGestureDetector.onTouchEvent(event)) {
            return true;
        }

        return r;
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            if (mScroller.isFinished()) {
                // This will facilitate snapping-to any Snappable points.
                setZoomLevel(mZoomLevel);
            } else {
                scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            }
            postInvalidate(); // Keep on drawing until the animation has
            // finished.
        }
    }

    @Override
    public void scrollTo(int x, int y) {
        final int worldSize_2 = tileSystem.MapSize(mZoomLevel) / 2;
        while (x < -worldSize_2) {
            x += (worldSize_2 * 2);
        }
        while (x > worldSize_2) {
            x -= (worldSize_2 * 2);
        }
        while (y < -worldSize_2) {
            y += (worldSize_2 * 2);
        }
        while (y > worldSize_2) {
            y -= (worldSize_2 * 2);
        }
        super.scrollTo(x, y);

        // do callback on listener
        if (mListener != null) {
            final ScrollEvent event = new ScrollEvent(this, x, y);
            mListener.onScroll(event);
        }
    }


    @SuppressLint("WrongCall")
    @Override
    protected void dispatchDraw(final Canvas c) {

        mProjection = new Projection();

        c.save();

        if (mMultiTouchScale == 1.0f) {
            c.translate(getWidth() / 2, getHeight() / 2);
        } else {
            c.getMatrix(mMatrix);
            mMatrix.postTranslate(getWidth() / 2, getHeight() / 2);
            mMatrix.preScale(mMultiTouchScale, mMultiTouchScale, getScrollX(), getScrollY());
            c.setMatrix(mMatrix);
        }


      mOverlayManager.onDraw(c, this);

        c.restore();

        super.dispatchDraw(c);
    }


    @Override
    protected void onDetachedFromWindow() {
        //	this.mZoomController.setVisible(false);
        this.onDetach();
        super.onDetachedFromWindow();
    }


    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.onAttach();
    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

    }

    // ===========================================================
    // Animation
    // ===========================================================

    @Override
    protected void onAnimationStart() {
        mIsAnimating.set(true);
        super.onAnimationStart();
    }

    @Override
    protected void onAnimationEnd() {
        mIsAnimating.set(false);
        clearAnimation();
        setZoomLevel(mTargetZoomLevel.get());
        this.isAnimating();
        super.onAnimationEnd();
    }

    /**
     * Check mAnimationListener.isAnimating() to determine if view is animating. Useful for overlays
     * to avoid recalculating during an animation sequence.
     *
     * @return boolean indicating whether view is animating.
     */
    public boolean isAnimating() {
        return mIsAnimating.get();
    }

    /*
     * Set the MapListener for this view
     */
    public void setMapListener(final MapListener ml) {
        mListener = ml;
    }





    // ===========================================================
    // Inner and Anonymous Classes
    // ===========================================================

    /**
     * A Projection serves to translate between the coordinate system of x/y on-screen pixel
     * coordinates and that of latitude/longitude points on the surface of the earth. You obtain a
     * Projection from MapView.getProjection(). You should not hold on to this object for more than
     * one draw, since the projection of the map could change. <br />
     * <br />
     * <I>Screen coordinates</I> are in the coordinate system of the screen's Canvas. The origin is
     * in the center of the plane. <I>Screen coordinates</I> are appropriate for using to draw to
     * the screen.<br />
     * <br />
     * <I>Map coordinates</I> are in the coordinate system of the standard Mercator projection. The
     * origin is in the upper-left corner of the plane. <I>Map coordinates</I> are appropriate for
     * use in the TileSystem class.<br />
     * <br />
     * <I>Intermediate coordinates</I> are used to cache the computationally heavy part of the
     * projection. They aren't suitable for use until translated into <I>screen coordinates</I> or
     * <I>map coordinates</I>.
     *
     * @author Nicolas Gramlich
     * @author Manuel Stahl
     */
    public class Projection implements IProjection, GeoConstants {

        private final int viewWidth_2 = getWidth() / 2;
        private final int viewHeight_2 = getHeight() / 2;
        private final int worldSize_2 = tileSystem.MapSize(mZoomLevel) / 2;
        private final int offsetX = -worldSize_2;
        private final int offsetY = -worldSize_2;

        private final BoundingBoxE6 mBoundingBoxProjection;
        private final int mZoomLevelProjection;
        private final Rect mScreenRectProjection;

        private Projection() {

			/*
			 * Do some calculations and drag attributes to local variables to save some performance.
			 */
            mZoomLevelProjection = MapView.this.mZoomLevel;
            mBoundingBoxProjection = MapView.this.getBoundingBox();
            mScreenRectProjection = MapView.this.getScreenRect(null);
        }

        public int getZoomLevel() {
            return mZoomLevelProjection;
        }

        public BoundingBoxE6 getBoundingBox() {
            return mBoundingBoxProjection;
        }

        public Rect getScreenRect() {
            return mScreenRectProjection;
        }

        /**
         * @deprecated Use TileSystem.getTileSize() instead.
         */
        @Deprecated
        public int getTileSizePixels() {
            return tileSystem.getTileSize();
        }

        /**
         * @deprecated Use
         *             <code>Point out = TileSystem.PixelXYToTileXY(screenRect.centerX(), screenRect.centerY(), null);</code>
         *             instead.
         */
        @Deprecated
        public Point getCenterMapTileCoords() {
            final Rect rect = getScreenRect();
            return tileSystem.PixelXYToTileXY(rect.centerX(), rect.centerY(), null);
        }


        /**
         * Converts <I>screen coordinates</I> to the underlying GeoPoint.
         *
         * @param x
         * @param y
         * @return GeoPoint under x/y.
         */
        public IGeoPoint fromPixels(final float x, final float y) {
            final Rect screenRect = getScreenRect();
            return tileSystem.PixelXYToLatLong(screenRect.left + (int) x + worldSize_2,
                    screenRect.top + (int) y + worldSize_2, mZoomLevelProjection, null);
        }

        public Point fromMapPixels(final int x, final int y, final Point reuse) {
            final Point out = reuse != null ? reuse : new Point();
            out.set(x - viewWidth_2, y - viewHeight_2);
            out.offset(getScrollX(), getScrollY());
            return out;
        }

        /**
         * Converts a GeoPoint to its <I>screen coordinates</I>.
         *
         * @param in
         *            the GeoPoint you want the <I>screen coordinates</I> of
         * @param reuse
         *            just pass null if you do not have a Point to be 'recycled'.
         * @return the Point containing the <I>screen coordinates</I> of the GeoPoint passed.
         */
        public Point toMapPixels(final IGeoPoint in, final Point reuse) {
            final Point out = reuse != null ? reuse : new Point();

            final Point coords = tileSystem.LatLongToPixelXY(in.getLatitudeE6() / 1E6,
                    in.getLongitudeE6() / 1E6, getZoomLevel(), null);
            out.set(coords.x, coords.y);
            out.offset(offsetX, offsetY);
            return out;
        }

        /**
         * Performs only the first computationally heavy part of the projection. Call
         * toMapPixelsTranslated to getDrawable the final position.
         *
         * @param latituteE6
         *            the latitute of the point
         * @param longitudeE6
         *            the longitude of the point
         * @param reuse
         *            just pass null if you do not have a Point to be 'recycled'.
         * @return intermediate value to be stored and passed to toMapPixelsTranslated.
         */
        public Point toMapPixelsProjected(final int latituteE6, final int longitudeE6,
                                          final Point reuse) {
            final Point out = reuse != null ? reuse : new Point();

            tileSystem
                    .LatLongToPixelXY(latituteE6 / 1E6, longitudeE6 / 1E6, MAXIMUM_ZOOMLEVEL, out);
            return out;
        }


        @Override
        public float metersToEquatorPixels(final float meters) {
            return meters / (float) tileSystem.GroundResolution(0, mZoomLevelProjection);
        }

        @Override
        public Point toPixels(final IGeoPoint in, final Point out) {
            return toMapPixels(in, out);
        }

        @Override
        public IGeoPoint fromPixels(final int x, final int y) {
            return fromPixels((float) x, (float) y);
        }
    }

    private class MapViewGestureDetectorListener implements OnGestureListener {

        @Override
        public boolean onDown(final MotionEvent e) {
            if (MapView.this.mOverlayManager.onDown(e, MapView.this)) {
                return true;
            }

            //mZoomController.setVisible(mEnableZoomController);
            return true;
        }

        @Override
        public boolean onFling(final MotionEvent e1, final MotionEvent e2, final float velocityX,
                               final float velocityY) {
            if (MapView.this.mOverlayManager.onFling(e1, e2, velocityX, velocityY, MapView.this)) {
                return true;
            }
            return false;
        }

        @Override
        public void onLongPress(final MotionEvent e) {
            MapView.this.mOverlayManager.onLongPress(e, MapView.this);
        }

        @Override
        public boolean onScroll(final MotionEvent e1, final MotionEvent e2, final float distanceX,
                                final float distanceY) {

            if (MapView.this.mOverlayManager.onScroll(e1, e2, distanceX, distanceY, MapView.this)) {
                return true;
            }

            scrollBy((int) distanceX, (int) distanceY);
            return true;
        }

        @Override
        public void onShowPress(final MotionEvent e) {
            MapView.this.mOverlayManager.onShowPress(e, MapView.this);
        }

        @Override
        public boolean onSingleTapUp(final MotionEvent e) {
            if (MapView.this.mOverlayManager.onSingleTapUp(e, MapView.this)) {
                return true;
            }

            return false;
        }

    }

    private class MapViewDoubleClickListener implements GestureDetector.OnDoubleTapListener {
        @Override
        public boolean onDoubleTap(final MotionEvent e) {
            if (mOverlayManager.onDoubleTap(e, MapView.this)) {
                return true;
            }
            final IGeoPoint center = getProjection().fromPixels(e.getX(), e.getY());
            return zoomInFixing(center);
        }

        @Override
        public boolean onDoubleTapEvent(final MotionEvent e) {
            if (mOverlayManager.onDoubleTapEvent(e, MapView.this)) {
                return true;
            }

            return false;
        }

        @Override
        public boolean onSingleTapConfirmed(final MotionEvent e) {
            if (mOverlayManager.onSingleTapConfirmed(e, MapView.this)) {
                return true;
            }

            return false;
        }
    }


}

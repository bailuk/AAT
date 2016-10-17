package org.osmdroid.views.overlay;

import android.graphics.Canvas;
import android.view.MotionEvent;

import org.osmdroid.views.MapView;

import java.util.AbstractList;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.concurrent.CopyOnWriteArrayList;

public class OverlayManager extends AbstractList<Overlay> {

    private TilesOverlay mTilesOverlay;

    private final CopyOnWriteArrayList<Overlay> mOverlayList;

    public OverlayManager(final TilesOverlay tilesOverlay) {
        setTilesOverlay(tilesOverlay);
        mOverlayList = new CopyOnWriteArrayList<>();
    }

    @Override
    public Overlay get(final int pIndex) {
        return mOverlayList.get(pIndex);
    }

    @Override
    public int size() {
        return mOverlayList.size();
    }

    @Override
    public void add(final int pIndex, final Overlay pElement) {
        mOverlayList.add(pIndex, pElement);
    }

    @Override
    public Overlay remove(final int pIndex) {
        return mOverlayList.remove(pIndex);
    }

    @Override
    public Overlay set(final int pIndex, final Overlay pElement) {
        return mOverlayList.set(pIndex, pElement);
    }



    public void setTilesOverlay(final TilesOverlay tilesOverlay) {
        mTilesOverlay = tilesOverlay;
    }


    public Iterable<Overlay> overlaysReversed() {
        return new Iterable<Overlay>() {
            @Override
            public Iterator<Overlay> iterator() {
                final ListIterator<Overlay> i = mOverlayList.listIterator(mOverlayList.size());

                return new Iterator<Overlay>() {
                    @Override
                    public boolean hasNext() {
                        return i.hasPrevious();
                    }

                    @Override
                    public Overlay next() {
                        return i.previous();
                    }

                    @Override
                    public void remove() {
                        i.remove();
                    }
                };
            }
        };
    }

    public void onDraw(final Canvas c, final MapView pMapView) {
        if ((mTilesOverlay != null)) {
            mTilesOverlay.draw(c, pMapView);
        }

        for (final Overlay overlay : mOverlayList) {
            overlay.draw(c, pMapView);
        }
    }



    public void onDetach(final MapView pMapView) {
        if (mTilesOverlay != null) {
            mTilesOverlay.onDetach(pMapView);
        }

        for (final Overlay overlay : this.overlaysReversed()) {
            overlay.onDetach(pMapView);
        }
    }


    public boolean onTouchEvent(final MotionEvent event, final MapView pMapView) {
        for (final Overlay overlay : this.overlaysReversed()) {
            if (overlay.onTouchEvent(event, pMapView)) {
                return true;
            }
        }

        return false;
    }

    public boolean onTrackballEvent(final MotionEvent event, final MapView pMapView) {
        for (final Overlay overlay : this.overlaysReversed()) {
            if (overlay.onTrackballEvent(event, pMapView)) {
                return true;
            }
        }

        return false;
    }


    /** GestureDetector.OnDoubleTapListener **/

    public boolean onDoubleTap(final MotionEvent e, final MapView pMapView) {
        for (final Overlay overlay : this.overlaysReversed()) {
            if (overlay.onDoubleTap(e, pMapView)) {
                return true;
            }
        }

        return false;
    }

    public boolean onDoubleTapEvent(final MotionEvent e, final MapView pMapView) {
        for (final Overlay overlay : this.overlaysReversed()) {
            if (overlay.onDoubleTapEvent(e, pMapView)) {
                return true;
            }
        }

        return false;
    }

    public boolean onSingleTapConfirmed(final MotionEvent e, final MapView pMapView) {
        for (final Overlay overlay : this.overlaysReversed()) {
            if (overlay.onSingleTapConfirmed(e, pMapView)) {
                return true;
            }
        }

        return false;
    }

    /** OnGestureListener **/

    public boolean onDown(final MotionEvent pEvent, final MapView pMapView) {
        for (final Overlay overlay : this.overlaysReversed()) {
            if (overlay.onDown(pEvent, pMapView)) {
                return true;
            }
        }

        return false;
    }

    public boolean onFling(final MotionEvent pEvent1, final MotionEvent pEvent2,
                           final float pVelocityX, final float pVelocityY, final MapView pMapView) {
        for (final Overlay overlay : this.overlaysReversed()) {
            if (overlay.onFling(pEvent1, pEvent2, pVelocityX, pVelocityY, pMapView)) {
                return true;
            }
        }

        return false;
    }

    public boolean onLongPress(final MotionEvent pEvent, final MapView pMapView) {
        for (final Overlay overlay : this.overlaysReversed()) {
            if (overlay.onLongPress(pEvent, pMapView)) {
                return true;
            }
        }

        return false;
    }

    public boolean onScroll(final MotionEvent pEvent1, final MotionEvent pEvent2,
                            final float pDistanceX, final float pDistanceY, final MapView pMapView) {
        for (final Overlay overlay : this.overlaysReversed()) {
            if (overlay.onScroll(pEvent1, pEvent2, pDistanceX, pDistanceY, pMapView)) {
                return true;
            }
        }

        return false;
    }

    public void onShowPress(final MotionEvent pEvent, final MapView pMapView) {
        for (final Overlay overlay : this.overlaysReversed()) {
            overlay.onShowPress(pEvent, pMapView);
        }
    }

    public boolean onSingleTapUp(final MotionEvent pEvent, final MapView pMapView) {
        for (final Overlay overlay : this.overlaysReversed()) {
            if (overlay.onSingleTapUp(pEvent, pMapView)) {
                return true;
            }
        }

        return false;
    }

}

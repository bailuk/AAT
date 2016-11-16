// Created by plusminus on 20:32:01 - 27.09.2008
package org.osmdroid.views.overlay;

import android.graphics.Canvas;
import android.view.GestureDetector;
import android.view.MotionEvent;

import org.osmdroid.views.MapView;
import org.osmdroid.views.util.constants.OverlayConstants;

/**
 * Base class representing an overlay which may be displayed on top of a {@link MapView}. To add an
 * overlay, subclass this class, create an instance, and add it to the list obtained from
 * getOverlays() of {@link MapView}.
 * 
 * This class implements a form of Gesture Handling similar to
 * {@link android.view.GestureDetector.SimpleOnGestureListener} and
 * {@link GestureDetector.OnGestureListener}. The difference is there is an additional argument for
 * the item.
 * 
 * @author Nicolas Gramlich
 */
public abstract class Overlay implements OverlayConstants {


	protected abstract void draw(final Canvas c, final MapView osmv);

	// ===========================================================
	// Methods
	// ===========================================================

	public void onAttach(final MapView mapView) {}
	public void onDetach(final MapView mapView) {}


	/**
	 * <b>You can prevent all(!) other Touch-related events from happening!</b><br />
	 * By default does nothing (<code>return false</code>). If you handled the Event, return
	 * <code>true</code>, otherwise return <code>false</code>. If you returned <code>true</code>
	 * none of the following Overlays or the underlying {@link MapView} has the chance to handle
	 * this event.
	 */
	public boolean onTouchEvent(final MotionEvent event, final MapView mapView) {
		return false;
	}

	/**
	 * By default does nothing (<code>return false</code>). If you handled the Event, return
	 * <code>true</code>, otherwise return <code>false</code>. If you returned <code>true</code>
	 * none of the following Overlays or the underlying {@link MapView} has the chance to handle
	 * this event.
	 */
	public boolean onTrackballEvent(final MotionEvent event, final MapView mapView) {
		return false;
	}

	/** GestureDetector.OnDoubleTapListener **/

	/**
	 * By default does nothing (<code>return false</code>). If you handled the Event, return
	 * <code>true</code>, otherwise return <code>false</code>. If you returned <code>true</code>
	 * none of the following Overlays or the underlying {@link MapView} has the chance to handle
	 * this event.
	 */
	public boolean onDoubleTap(final MotionEvent e, final MapView mapView) {
		return false;
	}

	/**
	 * By default does nothing (<code>return false</code>). If you handled the Event, return
	 * <code>true</code>, otherwise return <code>false</code>. If you returned <code>true</code>
	 * none of the following Overlays or the underlying {@link MapView} has the chance to handle
	 * this event.
	 */
	public boolean onDoubleTapEvent(final MotionEvent e, final MapView mapView) {
		return false;
	}

	/**
	 * By default does nothing (<code>return false</code>). If you handled the Event, return
	 * <code>true</code>, otherwise return <code>false</code>. If you returned <code>true</code>
	 * none of the following Overlays or the underlying {@link MapView} has the chance to handle
	 * this event.
	 */
	public boolean onSingleTapConfirmed(final MotionEvent e, final MapView mapView) {
		return false;
	}

	/** OnGestureListener **/

	/**
	 * By default does nothing (<code>return false</code>). If you handled the Event, return
	 * <code>true</code>, otherwise return <code>false</code>. If you returned <code>true</code>
	 * none of the following Overlays or the underlying {@link MapView} has the chance to handle
	 * this event.
	 */
	public boolean onDown(final MotionEvent e, final MapView mapView) {
		return false;
	}

	/**
	 * By default does nothing (<code>return false</code>). If you handled the Event, return
	 * <code>true</code>, otherwise return <code>false</code>. If you returned <code>true</code>
	 * none of the following Overlays or the underlying {@link MapView} has the chance to handle
	 * this event.
	 */
	public boolean onFling(final MotionEvent pEvent1, final MotionEvent pEvent2,
			final float pVelocityX, final float pVelocityY, final MapView pMapView) {
		return false;
	}

	/**
	 * By default does nothing (<code>return false</code>). If you handled the Event, return
	 * <code>true</code>, otherwise return <code>false</code>. If you returned <code>true</code>
	 * none of the following Overlays or the underlying {@link MapView} has the chance to handle
	 * this event.
	 */
	public boolean onLongPress(final MotionEvent e, final MapView mapView) {
		return false;
	}

	/**
	 * By default does nothing (<code>return false</code>). If you handled the Event, return
	 * <code>true</code>, otherwise return <code>false</code>. If you returned <code>true</code>
	 * none of the following Overlays or the underlying {@link MapView} has the chance to handle
	 * this event.
	 */
	public boolean onScroll(final MotionEvent pEvent1, final MotionEvent pEvent2,
			final float pDistanceX, final float pDistanceY, final MapView pMapView) {
		return false;
	}

	public void onShowPress(final MotionEvent pEvent, final MapView pMapView) {
		return;
	}

	/**
	 * By default does nothing (<code>return false</code>). If you handled the Event, return
	 * <code>true</code>, otherwise return <code>false</code>. If you returned <code>true</code>
	 * none of the following Overlays or the underlying {@link MapView} has the chance to handle
	 * this event.
	 */
	public boolean onSingleTapUp(final MotionEvent e, final MapView mapView) {
		return false;
	}



}

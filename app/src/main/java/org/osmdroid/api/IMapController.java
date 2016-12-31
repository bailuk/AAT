package org.osmdroid.api;

import org.osmdroid.util.BoundingBoxOsm;
import org.osmdroid.views.MapController;

/**
 * An interface that resembles the Google Maps API MapController class
 * and is implemented by the osmdroid {@link MapController} class.
 *
 * @author Neil Boyd
 *
 */
public interface IMapController {

	void setCenter(IGeoPoint point);
	int setZoom(int zoomLevel);
	boolean zoomIn();
	boolean zoomOut();
	void zoomToSpan(int latSpanE6, int lonSpanE6);
	void zoomToSpan(BoundingBoxOsm bounding);
}

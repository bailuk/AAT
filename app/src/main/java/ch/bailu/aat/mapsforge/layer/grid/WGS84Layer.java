package ch.bailu.aat.mapsforge.layer.grid;

import org.mapsforge.core.graphics.Canvas;
import org.mapsforge.core.model.BoundingBox;
import org.mapsforge.core.model.LatLong;
import org.mapsforge.core.model.Point;

import java.util.Locale;

import ch.bailu.aat.coordinates.WGS84Sexagesimal;
import ch.bailu.aat.description.AltitudeDescription;
import ch.bailu.aat.gpx.GpxInformation;
import ch.bailu.aat.mapsforge.layer.ContextLayer;
import ch.bailu.aat.mapsforge.layer.MapsForgeLayer;

public class WGS84Layer extends MapsForgeLayer {
    private final static int MIN_ZOOM_LEVEL=7;

    private final ContextLayer clayer;
    private final AltitudeDescription altitudeDescription;

    public WGS84Layer(ContextLayer cl) {
        clayer = cl;
        altitudeDescription= new AltitudeDescription(cl.getContext());
    }


    @Override
    public void draw(BoundingBox boundingBox, byte zoomLevel, Canvas canvas, Point topLeftPoint) {
        final LatLong point = boundingBox.getCenterPoint();

        drawGrid();
        drawCoordinates(point);
        drawElevation(zoomLevel, point);

    }


    private void drawGrid() {
        final Point pixel = clayer.getCenter();

        clayer.drawVLine((int)pixel.x);
        clayer.drawHLine((int)pixel.y);
        clayer.drawPoint(pixel);
    }


    private void drawCoordinates(LatLong point) {
        clayer.drawTextBottom(new WGS84Sexagesimal(point.getLatitude(), point.getLongitude()).toString(),2);
        clayer.drawTextBottom(
                String.format((Locale)null,"%.6f/%.6f",
                        ((double)point.getLatitude()),
                        ((double)point.getLongitude())),
                1);
    }

    private void drawElevation(int zoom, LatLong point) {
        if (zoom > MIN_ZOOM_LEVEL && clayer.getServiceContext().isUp()) {
            final short ele = clayer.getServiceContext().getElevationService().getElevation(point.getLatitudeE6(), point.getLongitudeE6());
            clayer.drawTextBottom(altitudeDescription.getValueUnit(ele),3);
        }
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

}

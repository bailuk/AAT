package ch.bailu.aat.map.mapsforge.layer.grid;


import android.graphics.Point;

import org.mapsforge.core.graphics.Canvas;
import org.mapsforge.core.model.BoundingBox;
import org.mapsforge.core.model.LatLong;

import ch.bailu.aat.coordinates.MeterCoordinates;
import ch.bailu.aat.description.DistanceDescription;
import ch.bailu.aat.map.mapsforge.layer.context.MapContext;
import ch.bailu.aat.map.mapsforge.layer.MapsForgeLayer;
import ch.bailu.aat.map.osm.overlay.grid.GridMetricScaler;

public abstract class MeterGridLayer extends MapsForgeLayer {

    private static final int MIN_ZOOM_LEVEL = 5;

    private final GridMetricScaler grid = new GridMetricScaler();

    private final DistanceDescription distanceDescription;

    private final MapContext mcontext;

    public MeterGridLayer(MapContext cl) {
        mcontext = cl;
        distanceDescription = new DistanceDescription(cl.context);
    }


    @Override
    public void draw(BoundingBox boundingBox, byte zoomLevel, Canvas canvas, org.mapsforge.core.model.Point topLeftPoint) {

        if (zoomLevel > MIN_ZOOM_LEVEL) {

            grid.findOptimalScale(mcontext.metrics.getShortDistance() / 2);

            if (grid.getOptimalScale() > 0) {

                MeterCoordinates coordinates = getRoundedCoordinates(boundingBox);
                Point centerPixel = getCenterPixel(coordinates);

                mcontext.draw.grid(centerPixel, mcontext.metrics.distanceToPixel(grid.getOptimalScale()));
                mcontext.draw.point(centerPixel);
                mcontext.draw.textTop(distanceDescription.getDistanceDescriptionRounded(grid.getOptimalScale()), 1);
                mcontext.draw.textBottom(coordinates.toString(), 1);
            }
        }


    }




    private Point getCenterPixel(MeterCoordinates c) {
        return mcontext.metrics.toPixel(c.toLatLong());
    }



    private MeterCoordinates getRoundedCoordinates(BoundingBox box) {
        LatLong center = box.getCenterPoint();
        MeterCoordinates c = getCoordinates(center);
        c.round(grid.getOptimalScale());
        return c;
    }

    public abstract MeterCoordinates getCoordinates(LatLong p);
}
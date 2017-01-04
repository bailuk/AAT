package ch.bailu.aat.map.layer.grid;


import android.graphics.Point;

import org.mapsforge.core.model.BoundingBox;
import org.mapsforge.core.model.LatLong;

import ch.bailu.aat.coordinates.MeterCoordinates;
import ch.bailu.aat.description.DistanceDescription;
import ch.bailu.aat.map.MapContext;
import ch.bailu.aat.map.layer.MapLayerInterface;
import ch.bailu.aat.map.osmdroid.overlay.grid.GridMetricScaler;

public abstract class MeterGridLayer implements MapLayerInterface {

    private static final int MIN_ZOOM_LEVEL = 5;

    private final GridMetricScaler grid = new GridMetricScaler();

    private final DistanceDescription distanceDescription;

    private final MapContext mcontext;

    public MeterGridLayer(MapContext cl) {
        mcontext = cl;
        distanceDescription = new DistanceDescription(cl.getContext());
    }


    @Override
    public void draw(MapContext mcontext) {

        if (mcontext.getMetrics().getZoomLevel() > MIN_ZOOM_LEVEL) {

            grid.findOptimalScale(mcontext.getMetrics().getShortDistance() / 2);

            if (grid.getOptimalScale() > 0) {

                MeterCoordinates coordinates = getRoundedCoordinates(mcontext.getMetrics().getBoundingBox());
                Point centerPixel = getCenterPixel(coordinates);

                mcontext.draw().grid(centerPixel, mcontext.getMetrics().distanceToPixel(grid.getOptimalScale()));
                mcontext.draw().point(centerPixel);
                mcontext.draw().textTop(distanceDescription.getDistanceDescriptionRounded(grid.getOptimalScale()), 1);
                mcontext.draw().textBottom(coordinates.toString(), 1);
            }
        }


    }




    private Point getCenterPixel(MeterCoordinates c) {
        return mcontext.getMetrics().toPixel(c.toLatLong());
    }



    private MeterCoordinates getRoundedCoordinates(BoundingBox box) {
        LatLong center = box.getCenterPoint();
        MeterCoordinates c = getCoordinates(center);
        c.round(grid.getOptimalScale());
        return c;
    }

    public abstract MeterCoordinates getCoordinates(LatLong p);
}
package ch.bailu.aat.map.layer.grid;


import android.content.Context;
import android.graphics.Point;

import org.mapsforge.core.model.BoundingBox;
import org.mapsforge.core.model.LatLong;

import ch.bailu.aat.coordinates.MeterCoordinates;
import ch.bailu.aat.description.DistanceDescription;
import ch.bailu.aat.map.MapContext;
import ch.bailu.aat.map.layer.MapLayerInterface;

public abstract class MeterGridLayer implements MapLayerInterface {

    private static final int MIN_ZOOM_LEVEL = 5;

    private final GridMetricScaler grid = new GridMetricScaler();

    private final DistanceDescription distanceDescription;

    private String ttext = "";
    private String btext = "";

    public MeterGridLayer(Context c) {
        distanceDescription = new DistanceDescription(c);
    }

    @Override
    public void drawForeground(MapContext mcontext) {
        mcontext.draw().textTop(ttext, 1);
        mcontext.draw().textBottom(btext, 0);
    }


    @Override
    public void drawInside(MapContext mcontext) {

        if (mcontext.getMetrics().getZoomLevel() > MIN_ZOOM_LEVEL) {

            grid.findOptimalScale(mcontext.getMetrics().getShortDistance() / 2);

            if (grid.getOptimalScale() > 0) {

                MeterCoordinates coordinates = getRoundedCoordinates(mcontext.getMetrics().getBoundingBox());
                Point centerPixel = getCenterPixel(mcontext, coordinates);

                mcontext.draw().grid(centerPixel, mcontext.getMetrics().distanceToPixel(grid.getOptimalScale()));
                mcontext.draw().point(centerPixel);

                ttext = distanceDescription.getDistanceDescription(grid.getOptimalScale());
                btext = coordinates.toString();
            }
        }


    }




    private Point getCenterPixel(MapContext mc, MeterCoordinates c) {
        return mc.getMetrics().toPixel(c.toLatLong());
    }



    private MeterCoordinates getRoundedCoordinates(BoundingBox box) {
        LatLong center = box.getCenterPoint();
        MeterCoordinates c = getCoordinates(center);
        c.round(grid.getOptimalScale());
        return c;
    }

    public abstract MeterCoordinates getCoordinates(LatLong p);
}
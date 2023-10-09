package ch.bailu.aat_lib.map.layer.grid;


import org.mapsforge.core.model.BoundingBox;
import org.mapsforge.core.model.LatLong;

import ch.bailu.aat_lib.coordinates.MeterCoordinates;
import ch.bailu.aat_lib.description.DistanceDescription;
import ch.bailu.aat_lib.map.MapContext;
import ch.bailu.aat_lib.util.Point;
import ch.bailu.aat_lib.map.layer.MapLayerInterface;
import ch.bailu.aat_lib.preferences.StorageInterface;

public abstract class MeterGridLayer implements MapLayerInterface {

    private static final int MIN_ZOOM_LEVEL = 5;

    private final GridMetricScaler grid = new GridMetricScaler();

    private final DistanceDescription distanceDescription;

    private String ttext = "";
    private String btext = "";

    public MeterGridLayer(StorageInterface storage) {
        distanceDescription = new DistanceDescription(storage);
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


    private Point getCenterPixel(MapContext mcontext, MeterCoordinates c) {
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

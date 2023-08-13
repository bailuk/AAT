package ch.bailu.aat_lib.gpx;

import ch.bailu.aat_lib.description.DistanceDescription;
import ch.bailu.aat_lib.preferences.StorageInterface;

public class GpxDistanceWindow extends GpxWindow {

    private final float distanceLimit;

    public GpxDistanceWindow(GpxList list) {
        super((GpxPointNode) list.getPointList().getFirst());
        distanceLimit = getDistanceLimit(list.getDelta().getDistance());
    }


    @Override
    protected boolean overLimit() {
        return getDistance() > distanceLimit;
    }


    public String getLimitAsString(StorageInterface s) {
        if (distanceLimit > 0)
            return new DistanceDescription(s).getDistanceDescription(distanceLimit);
        return "";

    }

    private static int getDistanceLimit(float distance) {

        if (distance > 60000) return 2000;
        if (distance > 30000) return 1000;
        if (distance > 10000) return 500;
        if (distance > 5000) return 100;
        if (distance > 2000) return 50;
        if (distance > 1000) return 10;
        if (distance > 0) return 5;
        return 0;
    }

}

package ch.bailu.aat.gpx;

public class GpxDistanceWindow extends GpxWindow {

    private final float distanceLimit;


    public GpxDistanceWindow(GpxPointNode node, float limit) {
        super(node);
        distanceLimit = limit;
    }


    @Override
    protected boolean overLmit() {
        return getDistance() > distanceLimit;
    }
}

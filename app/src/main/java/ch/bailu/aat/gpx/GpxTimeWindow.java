package ch.bailu.aat.gpx;

public class GpxTimeWindow extends GpxWindow {
    private final long timeLimit;

    public GpxTimeWindow(GpxPointNode node, long limit) {
        super(node);
        timeLimit = limit;
    }


    @Override
    protected boolean overLmit() {
        return getTimeDelta() > timeLimit;
    }
}

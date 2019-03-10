package ch.bailu.aat.gpx;

import ch.bailu.aat.coordinates.BoundingBoxE6;
import ch.bailu.aat.gpx.interfaces.GpxDeltaInterface;

public abstract class GpxWindow implements GpxDeltaInterface {

    private GpxPointNode first;
    private GpxPointNode last;


    private float distance;
    private long timeDelta;

    public GpxWindow(GpxPointNode node) {
        first = node;
        last = node;
    }

    public void forward(GpxPointNode n) {
        last = n;

        distance += n.getDistance();
        timeDelta += n.getTimeDelta();

        trim();
    }

    private void trim() {
        while (overLmit() && first != last && first.getNext() instanceof GpxPointNode) {
            timeDelta -= first.getTimeDelta();
            distance -= first.getDistance();

            first = (GpxPointNode) first.getNext();
        }
    }

    protected abstract boolean overLmit();

    @Override
    public float getDistance() {
        return distance;
    }


    @Override
    public float getSpeed() {
        float timeSI = timeDelta / 1000f;

        if (timeSI > 0)
            return distance / timeSI;
        else
            return 0;
    }

    @Override
    public float getAcceleration() {
        return 0;
    }

    @Override
    public long getTimeDelta() {
        return timeDelta;
    }

    @Override
    public BoundingBoxE6 getBoundingBox() {
        return null;
    }

}

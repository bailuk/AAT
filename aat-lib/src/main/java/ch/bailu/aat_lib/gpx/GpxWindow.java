package ch.bailu.aat_lib.gpx;

import javax.annotation.Nonnull;

import ch.bailu.aat_lib.coordinates.BoundingBoxE6;
import ch.bailu.aat_lib.gpx.interfaces.GpxDeltaInterface;

public abstract class GpxWindow implements GpxDeltaInterface {

    private GpxPointNode first;
    private GpxPointNode last;

    private float distance;
    private long timeDeltaMillis;

    public GpxWindow(GpxPointNode node) {
        first = node;
        last = node;
    }

    public void forward(GpxPointNode n) {
        last = n;

        distance += n.getDistance();
        timeDeltaMillis += n.getTimeDelta();

        trim();
    }

    private void trim() {
        while (overLimit() && first != last && first.getNext() instanceof GpxPointNode) {
            timeDeltaMillis -= first.getTimeDelta();
            distance -= first.getDistance();

            first = (GpxPointNode) first.getNext();
        }
    }

    protected abstract boolean overLimit();

    @Override
    public float getDistance() {
        return distance;
    }


    @Override
    public float getSpeed() {
        if (timeDeltaMillis > 0) {
            return (distance * 1000f) / timeDeltaMillis;
        } else {
            return 0;
        }
    }

    @Override
    public float getAcceleration() {
        return 0;
    }

    @Override
    public long getTimeDelta() {
        return timeDeltaMillis;
    }

    @Nonnull
    @Override
    public BoundingBoxE6 getBoundingBox() {
        return BoundingBoxE6.NULL_BOX;
    }

}

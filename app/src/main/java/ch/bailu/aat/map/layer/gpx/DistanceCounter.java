package ch.bailu.aat.map.layer.gpx;

public final class DistanceCounter {
    public final float min, max;
    private float count=0;

    public DistanceCounter(float limit1, float limit2) {
        max = Math.max(limit1, limit2);
        min = Math.min(limit1, limit2);
    }

    public void add(float x) {
        count += x;
    }


    public void reset() {
        count = 0;
    }


    public boolean isTooSmall() {
        return count < min;
    }

    public boolean hasDistance() {
        return count >= min;
    }


    public boolean isTooLarge() {
        return count > max;
    }
}

package ch.bailu.aat.gpx.attributes;

import ch.bailu.aat.gpx.GpxPointNode;

public abstract class AltitudeDelta extends GpxSubAttributes {

    private static final Keys KEYS = new Keys();

    public static final int INDEX_SLOPE = KEYS.add("Slope");
    public static final int INDEX_DESCEND = KEYS.add("Descend");
    public static final int INDEX_ASCEND = KEYS.add("Ascend");


    public AltitudeDelta() {
        super(KEYS);
    }

    public abstract void add(float alt, float dist);
    public abstract short getDescend();
    public abstract short getAscend();
    public abstract short getSlope();


    @Override
    public boolean update(GpxPointNode p, boolean autoPause) {
        if (!autoPause)
            add((float) p.getAltitude(), p.getDistance());
        return autoPause;
    }



    @Override
    public String get(int key) {
        if (key == INDEX_SLOPE) {
            return String.valueOf(getSlope());

        } else if (key == INDEX_DESCEND) {
            return String.valueOf(getDescend());

        } else if (key == INDEX_ASCEND) {
            return String.valueOf(getAscend());

        }

        return NULL_VALUE;
    }



    @Override
    public float getAsFloat(int key) {
        if (key == INDEX_ASCEND)
            return getAscend();
        else if (key == INDEX_DESCEND)
            return getDescend();

        return super.getAsFloat(key);
    }


    @Override
    public int getAsInteger(int key) {
        if (key == INDEX_SLOPE) {
            return getSlope();

        } else if (key == INDEX_DESCEND) {
            return getDescend();

        } else if (key == INDEX_ASCEND) {
            return getAscend();

        }
        return super.getAsInteger(key);
    }


    public static class LastAverage extends AltitudeDelta {
        private float ascend, descend;

        private final AverageAltitude average = new AverageAltitude();
        private float average_a, average_b, distance, delta;

        private int samples;

        public void add(float alt, float dist) {

            if (average.add(alt, dist)) {
                average_a = average_b;
                average_b = average.getAltitude();
                distance = average.getDistance();

                if (samples > 0) {
                    delta = average_b - average_a;
                    if (delta < 0) descend -= delta;
                    else ascend += delta;
                }
                samples++;
            }

        }

        public short getAscend() {
            return (short) ascend;
        }

        public short getDescend() {
            return (short) descend;
        }

        public short getSlope() {
            if (distance > 1) {
                return (short) Math.round(100 * delta / distance);
            }
            return 0;
        }

    }
}

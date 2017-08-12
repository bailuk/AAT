package ch.bailu.aat.gpx;

public abstract class AltitudeDelta {
    public static final AltitudeDelta NULL = new AltitudeDelta() {
        @Override
        public void add(short alt, float dist) {}

        @Override
        public short getDescend() {
            return 0;
        }

        @Override
        public short getAscend() {
            return 0;
        }

        @Override
        public short getSlope() {
            return 0;
        }
    };

    public abstract void add(short alt, float dist);
    public abstract short getDescend();
    public abstract short getAscend();
    public abstract short getSlope();


    public static class LastAverage extends AltitudeDelta {
        private float ascend, descend;

        private final AverageAltitude average = new AverageAltitude();
        private float average_a, average_b, distance, delta;

        private int samples;

        public void add(short alt, float dist) {

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

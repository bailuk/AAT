package ch.bailu.aat.gpx;

public class AltitudeDelta {
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
                delta = average_b - average_a;;
                if (delta < 0) descend -= delta;
                else ascend += delta;
            }
            samples++;
        }

    }

    public short getAscend()  {
        return (short)ascend;
    }
    public short getDescend() {
        return (short)descend;
    }

    public short getSlope() {
        if (distance > 1) {
            return (short) Math.round(100 * delta / distance);
        }
        return 0;
    }
}

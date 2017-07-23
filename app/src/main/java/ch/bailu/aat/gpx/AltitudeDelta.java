package ch.bailu.aat.gpx;

public class AltitudeDelta {
    private final static int SAMPLES=20;

    private float ascend, descend;

    private final AverageAltitude average = new AverageAltitude();
    private float oldAverage = 0f;

    private int samples;

    public void add(short alt) {
        if (average.getSamples() > SAMPLES) {
            if (samples > 0) {
                float delta = average.get() - oldAverage;
                if (delta < 0) descend -= delta;
                else ascend += delta;
            }
            samples++;
            oldAverage = average.get();
            average.reset(alt);
        } else {
            average.add(alt);
        }

    }

    public short getAscend() {
        return (short)ascend;
    }

    public short getDescend() {
        return (short)descend;
    }
}

package ch.bailu.aat.gpx;

public class AverageAltitude {
    private int samples;

    private float altitude;

    public AverageAltitude() {

    }

    public void add(float alt) {
        altitude +=alt;
        altitude /=2f;

        samples++;
    }

    public void reset(float alt) {
        altitude = alt;
        samples = 1;
    }

    public float get() {
        return altitude;
    }

    public int getSamples() {
        return samples;
    }
}

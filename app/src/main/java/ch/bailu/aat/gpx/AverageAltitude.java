package ch.bailu.aat.gpx;

public class AverageAltitude {
    private static final int SAMPLES= 10;
    private static final float SAMPLE_DISTANCE = 5;

    private int sample;
    private float distance, tdistance;

    private float altitude;


    public boolean add(float alt, float dist) {
        distance += dist;
        tdistance += dist;

        if (distance > SAMPLE_DISTANCE) {
            distance = 0f;
            return add(alt);
        }

        return false;
    }

    public boolean add(float alt) {
        if (sample == 0) {
            tdistance = 0;
            altitude = alt;
        } else {
            altitude += alt;
            altitude /= 2f;
        }

        sample++;

        if (sample < SAMPLES) {
            return false;

        } else {
            sample = 0;
            return true;
        }
    }

    public float getAltitude() {
        return altitude;
    }
    public float getDistance() { return tdistance;}

}

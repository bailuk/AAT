package ch.bailu.aat.gpx;

public class AverageAltitude {
    private static final int SAMPLES = 10;
    private static final float SAMPLE_MIN_DISTANCE = 10f;

    private int next_sample_index, samples;


    private float t_distance;
    private int t_altitude;


    public boolean add(short alt, float dist) {
        if (next_sample_index == 0) {
            t_distance = dist;
            t_altitude = alt;
            samples = 1;

        } else  {
            t_distance += dist;
            t_altitude += alt;
            samples++;

        }

        if (samples < SAMPLES || t_distance < SAMPLE_MIN_DISTANCE) {
            next_sample_index++;
            return false;

        } else {
            next_sample_index = 0;
            return true;
        }
    }

    public float getAltitude() {
        double alt = t_altitude;

        if (samples > 0) {
            return (float) (alt / samples);
        } else {
            return 0f;
        }

    }

    public float getDistance() { return t_distance;}
}

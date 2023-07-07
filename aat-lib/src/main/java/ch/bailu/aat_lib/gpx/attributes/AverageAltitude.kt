package ch.bailu.aat_lib.gpx.attributes;

/**
 *   Calculate average altitude from at least {@linkplain #MIN_SAMPLES} samples and at
 *   least {@linkplain #MIN_DISTANCE} distance.
 *   This is used to calculate current slope and total ascend and descend of a track.
 *
 *   This is used by {@linkplain AltitudeDelta}
 */
public class AverageAltitude {

    private static final int MIN_SAMPLES = 5;
    private static final int MAX_SAMPLES = 50;
    private static final float MIN_DISTANCE = 50f;

    private int next_sample_index, samples;


    private float t_distance;
    private float t_altitude;


    /**
     * Add a sample to the averaged sample.
     * @param alt altitude of sample
     * @param dist distance of sample
     * @return true if limit reached (have a new average altitude sample).
     */
    public boolean add(float alt, float dist) {
        if (next_sample_index == 0) {
            t_distance = dist;
            t_altitude = alt;
            samples = 1;

        } else  {
            t_distance += dist;
            t_altitude += alt;
            samples++;

        }

        if ( (samples < MIN_SAMPLES || t_distance < MIN_DISTANCE) && samples < MAX_SAMPLES ) {
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

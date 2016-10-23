package ch.bailu.aat.gpx;

public class MaxSpeed {
    private final static int SAMPLES=5;

    private final float speeds[] = new float[SAMPLES];
    private int i=0;

    private float maxSpeed=0f;

    public void add(float speed) {
        insert(speed);
        set();
    }


    private void set() {
        float s = getSmallest();
        maxSpeed = Math.max(maxSpeed, s);
    }


    private void insert(float speed) {
        speeds[i] = speed;
        i = (++i) % SAMPLES;
    }


    private float getSmallest() {
        float r=speeds[0];

        for (int i=1; i<speeds.length; i++) {
            r = Math.min(r, speeds[i]);
        }
        return r;
    }


    public float get() {
        return maxSpeed;
    }
}

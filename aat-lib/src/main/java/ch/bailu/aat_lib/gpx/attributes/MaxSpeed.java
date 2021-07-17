package ch.bailu.aat_lib.gpx.attributes;

import ch.bailu.aat_lib.gpx.GpxPointNode;

public abstract class MaxSpeed  extends GpxSubAttributes {


    private static final Keys KEYS = new Keys();
    public static final int INDEX_MAX_SPEED = KEYS.add("MaxSpeed");


    public MaxSpeed() {
        super(KEYS);
    }


    public abstract float get();
    public abstract void add(float speed);


    @Override
    public boolean update(GpxPointNode p, boolean autoPause) {
        if (!autoPause) add(p.getSpeed());
        return autoPause;
    }



    @Override
    public String get(int key) {
        if (key == INDEX_MAX_SPEED) {
            return String.valueOf(get());
        }
        return NULL_VALUE;
    }


    @Override
    public float getAsFloat(int key) {
        if (key == INDEX_MAX_SPEED)
            return get();

        return super.getAsFloat(key);
    }



    public static class Raw2 extends MaxSpeed {

        private float maximum=0f;

        @Override
        public float get() {
            return maximum;
        }

        @Override
        public void add(float speed) {
            maximum=Math.max(speed, maximum);
        }

    }


    public static class Samples extends MaxSpeed {
        private final float[] speeds;
        private int i = 0;

        private float maximum = 0f;


        public Samples() {
            this(5);
        }


        public Samples(int samples) {
            samples = Math.max(samples, 1);
            speeds = new float[samples];
        }

        @Override
        public float get() {
            return maximum;
        }

        @Override
        public void add(float speed) {
            insert(speed);
            set();
        }


        private void set() {
            float s = getSmallest();
            maximum = Math.max(maximum, s);
        }


        private void insert(float speed) {
            speeds[i] = speed;
            i = (++i) % speeds.length;
        }


        private float getSmallest() {
            float r = speeds[0];

            for (int i = 1; i < speeds.length; i++) {
                r = Math.min(r, speeds[i]);
            }
            return r;
        }
    }
}

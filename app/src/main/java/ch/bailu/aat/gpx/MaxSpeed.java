package ch.bailu.aat.gpx;

public abstract class MaxSpeed {

    public static final MaxSpeed NULL = new MaxSpeed() {
        @Override
        public float get() {
            return 0;
        }

        @Override
        public void add(float speed) {

        }
    };

    public abstract float get();
    public abstract void add(float speed);



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
        private final float speeds[];
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

package ch.bailu.aat.gpx.attributes;

import ch.bailu.aat.gpx.GpxPointNode;
import ch.bailu.aat.gpx.interfaces.GpxDeltaInterface;
import ch.bailu.aat.services.location.Trigger;
import ch.bailu.aat.util.ui.AppLog;

public abstract class AutoPause extends GpxSubAttributes {

    private static final Keys KEYS = new Keys();

    public static final int INDEX_AUTO_PAUSE = KEYS.add("AutoPause");

    public static final AutoPause NULL = new AutoPause() {
        @Override
        public long get() {
            AppLog.w(this, "NULL");
            return 0;
        }

        @Override
        public boolean update(GpxDeltaInterface delta) {
            return true;
        }
    };

    public AutoPause() {
        super(KEYS);
    }


    public abstract long get();
    public abstract boolean update(GpxDeltaInterface delta);


    @Override
    public boolean update(GpxPointNode p, boolean autoPause) {
        return update(p) == false;
    }


    @Override
    public String get(int key) {
        if (key == INDEX_AUTO_PAUSE) {
            return String.valueOf(get());

        }
        return NULL_VALUE;
    }




    @Override
    public long getAsLong(int key) {
        if (key == INDEX_AUTO_PAUSE) {
            return get();
        }
        return super.getAsLong(key);
    }



    private static class Samples extends AutoPause {

        private final float speed;
        private final Trigger trigger;

        private long pause = 0;

        private Samples(float minSpeed, int samples) {
            trigger = new Trigger(samples);
            speed = minSpeed;
        }

        public boolean update(GpxDeltaInterface delta) {
            if (delta.getSpeed() < speed) {
                trigger.down();
            } else {
                trigger.up();
            }

            if (trigger.isLow()) {
                pause += delta.getTimeDelta();
                return false;
            }
            return true;
        }

        public long get() {
            return pause;
        }

    }

    public static class Time extends AutoPause {

        private final float minSpeed;
        private final int time;


        private long add;

        private long pause;

        public Time(float minSpeed, int time) {
            this.minSpeed = minSpeed;
            this.time = time;
        }

        @Override
        public long get() {
            if (add < time) return pause;
            return pause + add;
        }

        @Override
        public boolean update(GpxDeltaInterface delta) {
            if (delta.getSpeed() < minSpeed) {
                 add += delta.getTimeDelta();

            } else {
                if (add > time) {
                    pause += add;

                }
                add = 0;
            }

            return add <= time; // not paused
        }
    }
}

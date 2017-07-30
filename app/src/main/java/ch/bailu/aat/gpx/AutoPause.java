package ch.bailu.aat.gpx;

import ch.bailu.aat.gpx.interfaces.GpxDeltaInterface;
import ch.bailu.aat.services.location.Trigger;
import ch.bailu.aat.util.ui.AppLog;

public abstract class AutoPause {
    public static final AutoPause NULL = new AutoPause() {
        @Override
        public long get() {
            AppLog.d(this, "NULL");
            return 0;
        }

        @Override
        public boolean update(GpxDeltaInterface delta) {
            return true;
        }
    };

    public abstract long get();
    public abstract boolean update(GpxDeltaInterface delta);


    public static class Samples extends AutoPause {

        private final float speed;
        private final Trigger trigger;

        private long pause = 0;

        public Samples(float minSpeed, int samples) {
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
}

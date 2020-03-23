package ch.bailu.aat.gpx.attributes;

import ch.bailu.aat.gpx.GpxPointNode;
import ch.bailu.aat.gpx.interfaces.GpxDeltaInterface;

public abstract class AutoPause extends GpxSubAttributes {

    private static final Keys KEYS = new Keys();

    public static final int INDEX_AUTO_PAUSE_TIME = KEYS.add("AutoPause");
    public static final int INDEX_AUTO_PAUSE_DISTANCE = KEYS.add("AutoPauseDistance");


    public static final AutoPause NULL = new AutoPause() {
        @Override
        public long getPauseTime() {return 0;}

        @Override
        public float getPauseDistance() {return 0f;}

        @Override
        public boolean update(GpxDeltaInterface delta) {
            return true;
        }
    };

    public AutoPause() {
        super(KEYS);
    }


    public abstract long getPauseTime();
    public abstract float getPauseDistance();

    public abstract boolean update(GpxDeltaInterface delta);


    @Override
    public boolean update(GpxPointNode p, boolean autoPause) {
        return update(p) == false;
    }


    @Override
    public String get(int key) {
        if (key == INDEX_AUTO_PAUSE_TIME) {
            return String.valueOf(getPauseTime());

        } else if (key == INDEX_AUTO_PAUSE_DISTANCE) {
            return String.valueOf(getPauseDistance());
        }
        return NULL_VALUE;
    }


    @Override
    public float getAsFloat(int key) {
        if (key == INDEX_AUTO_PAUSE_DISTANCE) {
            return getPauseDistance();
        }
        return super.getAsFloat(key);
    }


    @Override
    public long getAsLong(int key) {
        if (key == INDEX_AUTO_PAUSE_TIME) {
            return getPauseTime();
        }
        return super.getAsLong(key);
    }




    public static class Time extends AutoPause {

        private final float minSpeed;
        private final int minTime;

        private long addTime;
        private float addDistance;

        private long pauseTime;
        private float pauseDistance;

        public Time(float minSpeed, int time) {
            this.minSpeed = minSpeed;
            this.minTime = time;
        }


        @Override
        public float getPauseDistance() {
            if (addTime < minTime) return pauseDistance;
            return pauseDistance + addDistance;
        }

        @Override
        public long getPauseTime() {
            if (addTime < minTime) return pauseTime;
            return pauseTime + addTime;
        }

        @Override
        public boolean update(GpxDeltaInterface delta) {
            if (delta.getSpeed() < minSpeed) {
                addTime += delta.getTimeDelta();
                addDistance += delta.getDistance();

            } else {
                if (addTime > minTime) {
                    pauseTime += addTime;
                    pauseDistance += addDistance;

                }
                addTime = 0;
                addDistance = 0f;
            }

            return addTime <= minTime; // not paused
        }
    }
}

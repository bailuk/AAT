package ch.bailu.aat.services.bluetooth_le;

public class Revolution {
    private final static int MINUTE = 60 * 1024;

    private final Rollover time = new Rollover();
    private final Rollover revolution = new Rollover();


    public void addUINT32(int t, long r) {
        time.add(t);
        revolution.addUINT32(r);
    }

    public void add(int t, int r) {
        time.add(t);
        revolution.add(r);
    }


    public int rpm() {
        int rpm = 0;
        final int time_for_one_revolution = intervall();

        if (time_for_one_revolution > 0)
            rpm = MINUTE / time_for_one_revolution;

        return rpm;
    }


    public int intervall() {
        if (revolution.getDelta() > 0)
            return time.getDelta() / revolution.getDelta();

        return 0;
    }

    public float getSpeedSI(float wheel_size) {
        final float seconds = ((float)intervall()) / 1024f;
        final float meters = wheel_size;

        return meters / seconds;
    }

    public long getTotalRevolutions() {
        return revolution.getTotal();
    }
}

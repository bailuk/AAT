package ch.bailu.aat.services.sensor.bluetooth_le;

public class Revolution {

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
            rpm = ID.MINUTE / time_for_one_revolution;

        return rpm;
    }


    public int intervall() {
        if (revolution.getDelta() > 0)
            return time.getDelta() / revolution.getDelta();

        return 0;
    }

    public float getSpeedSI(float circumferenceSI) {
        if (circumferenceSI > 0) {
            final int time1024 = intervall();

            if (time1024 > 0) {
                return (circumferenceSI * 1024f) / time1024;
            }
        }

        return 0f;
    }

    public long getTotalRevolutions() {
        return revolution.getTotal();
    }

    public boolean isInitialized() {
        return time.isInitialized() && revolution.isInitialized();
    }
}

package ch.bailu.aat.services.bluetooth_le;

public class Rollover {
    private final static int MAX_UINT16 = 0xffff;

    private boolean first = true;

    private long previous = 0;
    private long current = 0;
    private int delta = 0;

    private long total = 0;



    public void add(long v) {
        if (first) {
            first = false;
            current = v;
        }
        previous = current;
        current = v;
        delta = difference(current, previous);
        total += delta;
    }


    public void addUINT32(long v) {
        // reset intead of rollover (no documentation found on rollover)

        if (v < current) {
            previous = v;
            current = v;
        }

        add(v);
    }


    private static int difference(long newer, long older) {
        if (newer > older)
            return (int) (newer - older);

        else if (older > newer)
            return (int) ((newer + MAX_UINT16) - older);

        return 0;
    }


    public int getDelta() {
        return delta;
    }

    public long getTotal() {
        return total;
    }

}

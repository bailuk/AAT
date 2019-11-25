package ch.bailu.aat.services.sensor;

public final class Averager {


    private final int[] values;
    private int size = 0;
    private int next = 0;
    private int average = 0;

    public Averager(int sapmles) {
        values = new int[sapmles];
    }

    public void add(int b) {
        values[next] = b;
        if (size < values.length) size++;

        next++;
        if (next >= values.length) next = 0;

        average = average();
    }

    private int average() {
        int r = 0;

        if (size > 0) {
            for (int i = 0; i < size; i++) {
                r = r + values[i];
            }
            r = r / size;
        }
        return r;
    }

    public int get() {
        return average;
    }
}

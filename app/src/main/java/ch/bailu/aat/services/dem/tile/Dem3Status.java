package ch.bailu.aat.services.dem.tile;

public class Dem3Status {

    public static final int LOADING =1;
    public static final int VALID=2;
    public static final int EMPTY=3;

    private int status = EMPTY;
    private long stamp = System.currentTimeMillis();


    public long getStamp() {
        return stamp;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int s) {
        status = s;
        if (s == LOADING) stamp = System.currentTimeMillis();
    }
}

package ch.bailu.aat.util;

public class Once {
    private boolean once=true;


    public void reset() {
        once = true;
    }


    public boolean once() {
        if (once) {
            once = false;
            return true;
        }
        return false;
    }
}

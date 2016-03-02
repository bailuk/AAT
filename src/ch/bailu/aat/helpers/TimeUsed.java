package ch.bailu.aat.helpers;

public class TimeUsed {
    private static long _start = System.currentTimeMillis();;
    
    public static void start() {
        _start=System.currentTimeMillis();
    }
        
    
    
    public static void show(Object o) {
        long stop=System.currentTimeMillis();
        long delta = stop-_start;
        AppLog.d(o, "T: " + delta);
    }
}

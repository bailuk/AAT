package ch.bailu.aat.services.tracker.location;

public class Trigger {
    private static final int LOW =-1;
    private static final int NEUTRAL = 0;
    private static final int HIGH = 1;
    
    private int htrigger;
    private int ltrigger;
    
    private int trigger=NEUTRAL;
    
    private int level=0;    
    
    public Trigger(int trigger) {
        htrigger=trigger;
        ltrigger=0-trigger;
    }
    
    public void up() {
        if (++level >= htrigger) {
            level = htrigger;
            trigger=HIGH;
        }
    }
    
    
    public void down() {
        if (--level <= ltrigger) {
            level = ltrigger;
            trigger=LOW;
        }
    }
    public boolean isHigh() {
        return trigger==HIGH;
    }  

    
    public boolean isNeutral() {
        return trigger==NEUTRAL;
    }  
    
    public boolean isLow() {
        return trigger==LOW;
    }  
}

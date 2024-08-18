package ch.bailu.aat_lib.service.location;

public final class Trigger {
    private static final int LOW =-1;
    private static final int NEUTRAL = 0;
    private static final int HIGH = 1;

    private final int htrigger;
    private final int ltrigger;

    private int trigger=NEUTRAL;

    private int level=0;

    public Trigger(int trigger) {
        htrigger=trigger;
        ltrigger=0-trigger;
    }

    public Trigger(int triggerLevel, Trigger old) {
        this(triggerLevel);
        trigger = old.trigger;
        level = old.level;
    }

    public void up() {
        level++;
        if (level >= htrigger) {
            level = htrigger;
            trigger=HIGH;
        }
    }


    public void down() {
        level--;
        if (level <= ltrigger) {
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

    public void log() {
        String  t="Neutral";
        if (trigger==HIGH) t="High";
        else if (trigger==LOW) t="Low";

    }
}

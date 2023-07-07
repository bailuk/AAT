package ch.bailu.aat_lib.gpx.attributes;


public final class StepCounterAttributes extends GpxAttributes {


    private static final Keys KEYS = new Keys();

    public static final int KEY_INDEX_STEPS_RATE = KEYS.add("StepRate");
    public static final int KEY_INDEX_STEPS_TOTAL = KEYS.add("TotalSteps");

    public int stepsRate, stepsTotal;


    @Override
    public String get(int keyIndex) {
        return String.valueOf(getAsInteger(keyIndex));
    }


    @Override
    public int getAsInteger(int keyIndex) {
        if (keyIndex == KEY_INDEX_STEPS_RATE) {
            return stepsRate;

        } else if (keyIndex == KEY_INDEX_STEPS_TOTAL) {
            return stepsTotal;

        }
        return 0;
    }


    @Override
    public boolean hasKey(int keyIndex) {
        return KEYS.hasKey(keyIndex);
    }

    @Override
    public int size() {
        return KEYS.size();
    }

    @Override
    public String getAt(int i) {
        return get(KEYS.getKeyIndex(i));
    }

    @Override
    public int getKeyAt(int i) {
        return KEYS.getKeyIndex(i);
    }
}

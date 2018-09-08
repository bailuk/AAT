package ch.bailu.aat.gpx.interfaces;

public enum GpxType {
    WAY,
    ROUTE,
    TRACK,
    NONE;



    @Override
    public String toString() {
        return name();
    }


    public static String[] toStrings() {
        String[] strings = new String[values().length-1];

        for (int i = 0; i < strings.length; i++)
            strings[i] = values()[i].toString();

        return strings;
    }


    public int toInteger() {
        return ordinal();
    }


    public static GpxType fromInteger(int id) {
        if (id < 0 || id >= values().length)
            id = values().length-1;
        return values()[id];
    }
}

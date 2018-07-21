package ch.bailu.aat.gpx.interfaces;

public enum GpxType {
    WAY(0), ROUTE(1), TRACK(2), NONE(-1);

    private final int id;

    GpxType(int _id) {
        id = _id;
    }

    public int toInteger() {
        return id;
    }

    public static GpxType fromInteger(int id) {
        if (id == WAY.id) return WAY;
        if (id == ROUTE.id) return ROUTE;
        if (id == TRACK.id) return TRACK;
        return NONE;
    }
}

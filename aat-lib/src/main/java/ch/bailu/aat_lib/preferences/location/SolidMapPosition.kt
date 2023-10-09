package ch.bailu.aat_lib.preferences.location;

import org.mapsforge.core.model.LatLong;

import ch.bailu.aat_lib.coordinates.LatLongE6;
import ch.bailu.aat_lib.coordinates.LatLongInterface;
import ch.bailu.aat_lib.preferences.StorageInterface;

public class SolidMapPosition {
    private static final String LONGITUDE_SUFFIX ="longitude";
    private static final String LATITUDE_SUFFIX ="latitude";
    private static final String ZOOM_SUFFIX ="zoom";

    public static LatLongE6 readPosition(StorageInterface storage, String key) {
        return new LatLongE6(
                storage.readInteger(key + LATITUDE_SUFFIX),
                storage.readInteger(key + LONGITUDE_SUFFIX));
    }

    public static int readZoomLevel(StorageInterface storage, String key) {
        return storage.readInteger(key + ZOOM_SUFFIX);
    }

    public static void writeZoomLevel(StorageInterface storage, String key, int zoom) {
        storage.writeInteger(key + ZOOM_SUFFIX, zoom);
    }

    public static void writePosition(StorageInterface storage, String key, LatLong latLong) {
        writePosition(storage, key, new LatLongE6(latLong));
    }

    public static void writePosition(StorageInterface storage, String key, LatLongInterface latLong) {
        storage.writeInteger(key + LATITUDE_SUFFIX, latLong.getLatitudeE6());
        storage.writeInteger(key + LONGITUDE_SUFFIX, latLong.getLongitudeE6());
    }
}

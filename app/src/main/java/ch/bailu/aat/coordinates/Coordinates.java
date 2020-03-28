package ch.bailu.aat.coordinates;

import androidx.annotation.NonNull;

import org.mapsforge.core.model.LatLong;

public abstract class Coordinates {


    @NonNull
    public abstract String toString();


    public abstract LatLong toLatLong();


    public static IllegalArgumentException getCodeNotValidException(String code) {
        throw new IllegalArgumentException(
                "The provided code '" + code + "' is not a valid.");
    }
}

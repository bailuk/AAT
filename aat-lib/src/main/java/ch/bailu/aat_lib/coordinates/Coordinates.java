package ch.bailu.aat_lib.coordinates;

import org.mapsforge.core.model.LatLong;

import javax.annotation.Nonnull;

public abstract class Coordinates {


    @Nonnull
    public abstract String toString();


    public abstract LatLong toLatLong();


    public static IllegalArgumentException getCodeNotValidException(String code) {
        throw new IllegalArgumentException(
                "The provided code '" + code + "' is not a valid.");
    }
}

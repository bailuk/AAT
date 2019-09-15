package ch.bailu.aat.coordinates;

import android.support.annotation.NonNull;

import org.mapsforge.core.model.LatLong;

import ch.bailu.aat.util.ui.AppLog;

public abstract class Coordinates {


    @NonNull
    public abstract String toString();


    public abstract LatLong toLatLong();


    public static IllegalArgumentException getCodeNotValidException(String code) {
        throw new IllegalArgumentException(
                "The provided code '" + code + "' is not a valid.");
    }
}

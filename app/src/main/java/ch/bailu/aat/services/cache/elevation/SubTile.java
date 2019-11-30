package ch.bailu.aat.services.cache.elevation;

import android.graphics.Rect;
import android.support.annotation.NonNull;

import ch.bailu.aat.coordinates.SrtmCoordinates;
import ch.bailu.aat.services.cache.Span;

public final class SubTile {
    public final Span laSpan;
    public final Span loSpan;
    public final SrtmCoordinates coordinates;

    public SubTile(Span laS, Span loS) {
        laSpan = laS;
        loSpan = loS;

        coordinates = new SrtmCoordinates((double)laS.deg(), (double)loS.deg());
    }

    public Rect toRect() {
        return Span.toRect(laSpan, loSpan);
    }


    public int pixelDim() {
        return loSpan.pixelCount();
    }

    @Override
    public int hashCode() {
        return coordinates.hashCode();
    }

    @NonNull
    @Override
    public String toString() {
        return coordinates.toString();
    }

}

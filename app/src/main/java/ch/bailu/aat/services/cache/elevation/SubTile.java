package ch.bailu.aat.services.cache.elevation;

import android.graphics.Rect;
import androidx.annotation.NonNull;

import ch.bailu.aat.coordinates.Dem3Coordinates;
import ch.bailu.aat.services.cache.Span;

public final class SubTile {
    public final Span laSpan;
    public final Span loSpan;
    public final Dem3Coordinates coordinates;

    public SubTile(Span laS, Span loS) {
        laSpan = laS;
        loSpan = loS;

        coordinates = new Dem3Coordinates((double)laS.deg(), (double)loS.deg());
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

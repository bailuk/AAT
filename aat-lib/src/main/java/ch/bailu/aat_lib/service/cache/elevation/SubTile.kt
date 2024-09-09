package ch.bailu.aat_lib.service.cache.elevation;


import ch.bailu.aat_lib.coordinates.Dem3Coordinates;
import ch.bailu.aat_lib.service.cache.Span;
import ch.bailu.aat_lib.util.Rect;

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
        return loSpan.pixelDim();
    }

    @Override
    public int hashCode() {
        return coordinates.hashCode();
    }

    @Override
    public String toString() {
        return coordinates.toString();
    }
}

package ch.bailu.aat.services.cache.elevation;

import android.graphics.Rect;

import ch.bailu.aat.coordinates.SrtmCoordinates;
import ch.bailu.aat.services.cache.Span;
import ch.bailu.aat.services.dem.tile.Dem3Tile;

public class SubTile {
    public final Span laSpan;
    public final Span loSpan;
    public final SrtmCoordinates coordinates;

    public SubTile(Span laS, Span loS) {
        laSpan = laS;
        loSpan = loS;

        coordinates = new SrtmCoordinates((double)laS.deg(), (double)loS.deg());
    }

    public SubTilePainter painterFactory(String i, Dem3Tile t) {
        return new SubTilePainter(i, this, t);
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

    @Override
    public String toString() {
        return coordinates.toString();
    }


}

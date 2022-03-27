package ch.bailu.aat_lib.map;


import org.mapsforge.core.graphics.Canvas;
import org.mapsforge.core.graphics.Paint;
import org.mapsforge.core.graphics.TileBitmap;

import javax.annotation.Nonnull;

import ch.bailu.aat_lib.map.tile.source.Source;
import ch.bailu.aat_lib.util.Rect;

public interface TilePainter {
    void paint(@Nonnull TileBitmap tileBitmap, @Nonnull Canvas canvas, @Nonnull Rect rect, @Nonnull Paint paint);

    Paint createPaint(@Nonnull Source source);
}

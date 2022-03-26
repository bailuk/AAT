package ch.bailu.aat_lib.map;


import org.mapsforge.core.graphics.Canvas;
import org.mapsforge.core.graphics.Paint;
import org.mapsforge.core.graphics.TileBitmap;

import ch.bailu.aat_lib.map.tile.source.Source;
import ch.bailu.aat_lib.util.Rect;

public interface TilePainter {
    void paint(TileBitmap tileBitmap, Canvas canvas, Rect rect, Paint paint);

    Paint createPaint(Source source);
}

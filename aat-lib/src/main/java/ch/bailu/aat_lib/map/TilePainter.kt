package ch.bailu.aat_lib.map

import ch.bailu.aat_lib.map.tile.source.Source
import ch.bailu.aat_lib.util.Rect
import org.mapsforge.core.graphics.Canvas
import org.mapsforge.core.graphics.Paint
import org.mapsforge.core.graphics.TileBitmap

interface TilePainter {
    fun paint(tileBitmap: TileBitmap, canvas: Canvas, rect: Rect, paint: Paint)
    fun createPaint(source: Source): Paint
}

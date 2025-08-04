package ch.bailu.aat_gtk.map

import ch.bailu.aat_lib.map.TilePainter
import ch.bailu.aat_lib.map.tile.source.Source
import ch.bailu.aat_lib.util.Rect
import org.mapsforge.core.graphics.Canvas
import org.mapsforge.core.graphics.Paint
import org.mapsforge.core.graphics.TileBitmap
import org.mapsforge.map.gtk.graphics.GtkGraphicFactory

class GtkTilePainter : TilePainter {

    private var alpha : Float = 0f


    override fun paint(tileBitmap: TileBitmap, canvas: Canvas, rect: Rect, paint: Paint) {
        canvas.drawBitmap(tileBitmap, rect.left, rect.top, rect.right, rect.bottom,
            rect.left, rect.top, rect.right, rect.bottom, alpha)
    }

    override fun createPaint(source: Source): Paint {
        alpha = source.alpha.toFloat() / 255f

        return GtkGraphicFactory.INSTANCE.createPaint()
    }
}

package ch.bailu.aat.map.tile

import ch.bailu.aat.map.To
import ch.bailu.aat_lib.map.TilePainter
import ch.bailu.aat_lib.map.tile.source.Source
import ch.bailu.aat_lib.util.Rect
import org.mapsforge.core.graphics.Canvas
import org.mapsforge.core.graphics.Paint
import org.mapsforge.core.graphics.TileBitmap
import org.mapsforge.map.android.graphics.AndroidGraphicFactory
import javax.annotation.Nonnull

class AndroidTilePainter : TilePainter {
    private val rectCache = android.graphics.Rect()

    override fun paint(tileBitmap: TileBitmap, canvas: Canvas, rect: Rect, paint: Paint
    ) {
        val bitmap = AndroidGraphicFactory.getBitmap(tileBitmap)
        if (bitmap != null) {
            To.androidCanvas(canvas).drawBitmap(bitmap, null, To.androidRect(rect, rectCache), To.androidPaint(paint))
        }
    }

    override fun createPaint(@Nonnull source: Source): Paint {
        val paint = AndroidGraphicFactory.INSTANCE.createPaint()
        val androidPaint = To.androidPaint(paint)

        androidPaint.alpha = source.alpha
        if (source.filterBitmap()) {
            androidPaint.flags = android.graphics.Paint.FILTER_BITMAP_FLAG
        }
        return paint
    }
}

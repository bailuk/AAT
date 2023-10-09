package ch.bailu.aat_lib.map

import ch.bailu.aat_lib.app.AppGraphicFactory.instance
import ch.bailu.aat_lib.map.MapColor.setAlpha
import ch.bailu.aat_lib.map.MapColor.toLightTransparent
import org.mapsforge.core.graphics.Paint
import org.mapsforge.core.graphics.Style

object MapPaint {
    const val EDGE_WIDTH_LINE = 2
    private const val TEXT_SIZE = 20f
    @JvmStatic
    fun createBackgroundPaint(color: Int): Paint {
        val p = instance().createPaint()
        p.color = toLightTransparent(color)
        p.setStyle(Style.FILL)
        return p
    }

    fun createBackgroundPaint(): Paint {
        val p = instance().createPaint()
        p.color = MapColor.LIGHT
        p.setStyle(Style.FILL)
        return p
    }

    fun createGridPaint(res: AppDensity): Paint {
        val p = instance().createPaint()
        p.color = MapColor.GRID
        p.setStyle(Style.FILL)
        p.strokeWidth = Math.max(1f, res.toPixelFloat(1f))
        return p
    }

    fun createStatusTextPaint(res: AppDensity): Paint {
        return createTextPaint(res, TEXT_SIZE)
    }

    fun createLegendTextPaint(res: AppDensity): Paint {
        val p = createTextPaint(res, TEXT_SIZE / 3 * 2)
        p.color = MapColor.TEXT
        return p
    }

    private fun createTextPaint(res: AppDensity, size: Float): Paint {
        val p = instance().createPaint()
        p.color = MapColor.TEXT
        p.setTextSize(res.toPixelScaledFloat(size))
        p.setStyle(Style.FILL)
        return p
    }

    @JvmStatic
    fun createEdgePaintLine(res: AppDensity): Paint {
        return instance().createPaint().apply {
            this.strokeWidth = Math.max(res.toPixelFloat(EDGE_WIDTH_LINE.toFloat()), 1f)
            this.color = MapColor.EDGE
            this.setStyle(Style.STROKE)
        }
    }

    @JvmStatic
    fun createEdgePaintBlur(draw: MapDraw, color: Int, zoom: Int): Paint {
        return instance().createPaint().apply {
            this.strokeWidth = draw.getNodeBitmap().width.toFloat()
            this.color = setAlpha(color, zoomToAlpha(zoom))
            this.setStyle(Style.STROKE)
        }
    }

    private fun zoomToAlpha(zoomLevel: Int): Int {
        var a = 20
        if (zoomLevel > 15) a = 100 else if (zoomLevel > 13) a = 90 else if (zoomLevel > 12) a = 80 else if (zoomLevel > 11) a =
            70 else if (zoomLevel > 10) a = 60 else if (zoomLevel > 9) a = 50 else if (zoomLevel > 8) a =
            40 else if (zoomLevel > 7) a = 30
        return a
    }
}

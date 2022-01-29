package ch.bailu.aat_gtk.ui.view.graph

import ch.bailu.aat_lib.util.Point
import ch.bailu.aat_lib.view.graph.GraphCanvas
import ch.bailu.gtk.cairo.Context
import ch.bailu.gtk.pango.Pango
import ch.bailu.gtk.pangocairo.Pangocairo
import ch.bailu.gtk.type.Str
import org.mapsforge.map.gtk.util.color.ARGB
import org.mapsforge.map.gtk.util.color.ColorInterface
import org.mapsforge.map.gtk.util.color.Conv255

class GtkCanvas (private val context: Context) : GraphCanvas {

    private val fontDescription = Pango.fontDescriptionFromString(Str("Arial Thin 9"))

    override fun drawLine(x1: Int, y1: Int, x2: Int, y2: Int) {
        drawLine(x1, y1, x2, y2, ColorInterface.DKGRAY)
    }

    override fun drawLine(pa: Point, pb: Point, color: Int) {
        drawLine(pa.x, pa.y, pb.x, pb.y, color)
    }

    private fun drawLine(x1: Int, y1: Int, x2: Int, y2: Int, color: Int) {
        context.save()

        setColor(color)
        context.setLineWidth(1.0);
        context.moveTo(x1.toDouble(), y1.toDouble())
        context.lineTo(x2.toDouble(), y2.toDouble())
        context.stroke()
        context.restore()
    }

    override fun drawBitmap(pa: Point?, color: Int) {
    }

    override fun getTextSize(): Int {
        return 12
    }

    private fun setColor(color: Int) {
        val argb = ARGB(color)
        context.setSourceRgba(
            Conv255.toDouble(argb.red()),
            Conv255.toDouble(argb.green()),
            Conv255.toDouble(argb.blue()),
            Conv255.toDouble(argb.alpha())
        )
    }

    override fun drawText(text: String, x: Int, y: Int) {
        val strText = Str(text)
        val layout = Pangocairo.createLayout(context)
        layout.setText(strText, strText.size - 1)
        layout.fontDescription = fontDescription

        context.save()
        context.setLineWidth(0.5)
        context.moveTo(x.toDouble(), y.toDouble())
        Pangocairo.layoutPath(context, layout)
        setColor(ColorInterface.DKGRAY)
        context.fillPreserve()
        context.stroke()
        context.restore()

        layout.unref()
        strText.destroy()
    }
}
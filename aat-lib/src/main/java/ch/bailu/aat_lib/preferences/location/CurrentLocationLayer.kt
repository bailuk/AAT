package ch.bailu.aat_lib.preferences.location

import ch.bailu.aat_lib.app.AppColor
import ch.bailu.aat_lib.dispatcher.DispatcherInterface
import ch.bailu.aat_lib.dispatcher.OnContentUpdatedInterface
import ch.bailu.aat_lib.gpx.GpxInformation
import ch.bailu.aat_lib.gpx.InfoID
import ch.bailu.aat_lib.lib.color.ARGB
import ch.bailu.aat_lib.map.MapContext
import ch.bailu.aat_lib.map.layer.MapLayerInterface
import ch.bailu.aat_lib.preferences.StorageInterface
import ch.bailu.aat_lib.util.Point
import org.mapsforge.core.graphics.Paint
import org.mapsforge.core.graphics.Style
import org.mapsforge.core.model.LatLong
import kotlin.math.abs
import kotlin.math.roundToInt

class CurrentLocationLayer(private val mcontext: MapContext, d: DispatcherInterface) :
    OnContentUpdatedInterface, MapLayerInterface {
    private var center = GpxInformation.NULL
    private val paint: Paint = mcontext.draw().createPaint()

    init {
        paint.setStyle(Style.STROKE)
        paint.strokeWidth = STROKE_WIDTH.toFloat()
        d.addTarget(this, InfoID.LOCATION)
    }

    private class Saturate(c: Int) {
        private val r = ShortArray(STEPS)
        private val g = ShortArray(STEPS)
        private val b = ShortArray(STEPS)
        private val rgb: ARGB

        init {
            rgb = ARGB(c)
            val max = Math.max(rgb.red(), Math.max(rgb.green(), rgb.blue()))
            fill(rgb.red(), max, r)
            fill(rgb.green(), max, g)
            fill(rgb.blue(), max, b)
        }

        private fun fill(base: Int, max: Int, t: ShortArray) {
            for (i in 0 until STEPS) {
                val step = (max - base) / STEPS.toFloat()
                t[i] = (base + (step * i).roundToInt()).toShort()
            }
        }

        fun colorFromTimeStamp(time: Long): Int {
            return color((System.currentTimeMillis() - time).toInt() / 1000)
        }

        fun color(index: Int): Int {
            var i = index
            i = Math.min(STEPS - 1, abs(i))
            return ARGB(rgb.alpha(), r[i].toInt(), g[i].toInt(), b[i].toInt()).toInt()
        }

        companion object {
            private const val STEPS = 60
        }
    }

    override fun onContentUpdated(iid: Int, info: GpxInformation) {
        center = info
        if (contains(center)) mcontext.getMapView().requestRedraw()
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {}
    override fun drawInside(mcontext: MapContext) {
        if (contains(center) && center.getAccuracy() > 0) {
            val pixel = mcontext.getMetrics().toPixel(center)
            val radius =
                Math.max(MIN_RADIUS, mcontext.getMetrics().distanceToPixel(center.getAccuracy()))
            paint.color = COLOR.colorFromTimeStamp(center.getTimeStamp())
            mcontext.draw().circle(pixel, radius, paint)
        }
    }

    private operator fun contains(center: GpxInformation): Boolean {
        return mcontext.getMetrics().getBoundingBox().contains(
            LatLong(center.getLatitude(), center.getLongitude())
        )
    }

    override fun onTap(tapPos: Point): Boolean {
        return false
    }

    override fun drawForeground(mcontext: MapContext) {}
    override fun onPreferencesChanged(storage: StorageInterface, key: String) {}
    override fun onAttached() {}
    override fun onDetached() {}

    companion object {
        private const val MIN_RADIUS = 7
        private const val STROKE_WIDTH = 2
        private val COLOR = Saturate(AppColor.HL_ORANGE)
    }
}

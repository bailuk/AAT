package ch.bailu.aat.views.layout

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout

open class PercentageLayout (
    context: Context,
    private val space: Int = DEFAULT_SPACE
) : ViewGroup(context) {
    private class Entry(val view: View, p: Int) {
        val percentage: Int

        init {
            percentage = Math.max(p, 5)
        }

        fun getSize(parent_size: Int, p100: Int): Int {
            return parent_size * percentage / p100
        }

        fun isVisible(): Boolean {
            return view.visibility != GONE
        }
    }

    private val list = ArrayList<Entry>(5)
    private var orientation = LinearLayout.VERTICAL
    fun setOrientation(o: Int) {
        orientation = o
    }

    fun add(v: View, p: Int): PercentageLayout {
        list.add(Entry(v, p))
        addView(v)
        return this
    }

    fun add(vararg views: View): PercentageLayout {
        if (views.isNotEmpty()) {
            val p = 100 / views.size
            for (v in views) add(v, p)
        }
        return this
    }

    override fun onMeasure(wSpec: Int, hSpec: Int) {
        if (orientation == LinearLayout.VERTICAL) vMeasure(wSpec, hSpec) else hMeasure(wSpec, hSpec)
    }

    private fun vMeasure(wSpec: Int, hSpec: Int) {
        var wSpec = wSpec
        var hSpec = hSpec
        val width = MeasureSpec.getSize(wSpec)
        val height = MeasureSpec.getSize(hSpec)
        val p100 = get100Percent()
        if (list.size > 0) {
            wSpec = MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY)
            for (e in list) {
                if (e.isVisible()) {
                    hSpec =
                        MeasureSpec.makeMeasureSpec(e.getSize(height, p100), MeasureSpec.EXACTLY)
                    e.view.measure(wSpec, hSpec)
                }
            }
        }
        setMeasuredDimension(width, height)
    }

    private fun hMeasure(wSpec: Int, hSpec: Int) {
        var wSpec = wSpec
        var hSpec = hSpec
        val width = MeasureSpec.getSize(wSpec)
        val height = MeasureSpec.getSize(hSpec)
        val p100 = get100Percent()
        if (list.size > 0) {
            hSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY)
            for (e in list) {
                if (e.isVisible()) {
                    wSpec = MeasureSpec.makeMeasureSpec(e.getSize(width, p100), MeasureSpec.EXACTLY)
                    e.view.measure(wSpec, hSpec)
                }
            }
        }
        setMeasuredDimension(width, height)
    }

    private fun get100Percent(): Int {
        var p = 0
        for (e in list) {
            if (e.isVisible()) {
                p += e.percentage
            }
        }
        return p
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        if (orientation == LinearLayout.VERTICAL) vLayout(l, t, r, b) else hLayout(l, t, r, b)
    }

    private fun vLayout(l: Int, t: Int, r: Int, b: Int) {
        var l = l
        var t = t
        var r = r
        var b = b
        val p100 = get100Percent()
        if (list.size > 0) {
            val parentHeight = b - t
            val parentWidth = r - l
            var s = 0
            r = parentWidth
            t = 0
            l = t
            for (e in list) {
                if (e.isVisible()) {
                    val viewHeight = e.getSize(parentHeight, p100)
                    b = t + viewHeight
                    e.view.layout(l, t + s, r, b)
                    t += viewHeight
                    s = space
                }
            }
        }
    }

    private fun hLayout(l: Int, t: Int, r: Int, b: Int) {
        var l = l
        var t = t
        var r = r
        var b = b
        val p100 = get100Percent()
        if (list.size > 0) {
            val parentHeight = b - t
            val parentWidth = r - l
            var s = 0
            l = 0
            t = l
            b = parentHeight
            for (e in list) {
                if (e.isVisible()) {
                    val viewWidth = e.getSize(parentWidth, p100)
                    r = l + viewWidth
                    e.view.layout(l + s, t, r, b)
                    l += viewWidth
                    s = space
                }
            }
        }
    }

    companion object {
        private const val DEFAULT_SPACE = 2
        fun add(c: Context, vararg views: View): PercentageLayout {
            return PercentageLayout(c).add(*views)
        }
    }
}

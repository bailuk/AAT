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
        val percentage: Int = Math.max(p, 5)

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
        var wSpecMeasured = wSpec
        var hSpecMeasured = hSpec
        val width = MeasureSpec.getSize(wSpecMeasured)
        val height = MeasureSpec.getSize(hSpecMeasured)
        val p100 = get100Percent()
        if (list.size > 0) {
            wSpecMeasured = MeasureSpec.makeMeasureSpec(width, MeasureSpec.EXACTLY)
            for (e in list) {
                if (e.isVisible()) {
                    hSpecMeasured =
                        MeasureSpec.makeMeasureSpec(e.getSize(height, p100), MeasureSpec.EXACTLY)
                    e.view.measure(wSpecMeasured, hSpecMeasured)
                }
            }
        }
        setMeasuredDimension(width, height)
    }

    private fun hMeasure(wSpec: Int, hSpec: Int) {
        var wSpecMeasured = wSpec
        var hSpecMeasured = hSpec
        val width = MeasureSpec.getSize(wSpecMeasured)
        val height = MeasureSpec.getSize(hSpecMeasured)
        val p100 = get100Percent()
        if (list.size > 0) {
            hSpecMeasured = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY)
            for (e in list) {
                if (e.isVisible()) {
                    wSpecMeasured = MeasureSpec.makeMeasureSpec(e.getSize(width, p100), MeasureSpec.EXACTLY)
                    e.view.measure(wSpecMeasured, hSpecMeasured)
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
        var left = l
        var top = t
        var right = r
        var bottom = b
        val p100 = get100Percent()
        if (list.size > 0) {
            val parentHeight = bottom - top
            val parentWidth = right - left
            var s = 0
            right = parentWidth
            top = 0
            left = top
            for (e in list) {
                if (e.isVisible()) {
                    val viewHeight = e.getSize(parentHeight, p100)
                    bottom = top + viewHeight
                    e.view.layout(left, top + s, right, bottom)
                    top += viewHeight
                    s = space
                }
            }
        }
    }

    private fun hLayout(l: Int, t: Int, r: Int, b: Int) {
        var left = l
        var top = t
        var right = r
        var bottom = b
        val p100 = get100Percent()
        if (list.size > 0) {
            val parentHeight = bottom - top
            val parentWidth = right - left
            var s = 0
            left = 0
            top = 0
            bottom = parentHeight
            for (e in list) {
                if (e.isVisible()) {
                    val viewWidth = e.getSize(parentWidth, p100)
                    right = left + viewWidth
                    e.view.layout(left + s, top, right, bottom)
                    left += viewWidth
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

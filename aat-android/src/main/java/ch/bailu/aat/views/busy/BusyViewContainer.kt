package ch.bailu.aat.views.busy

import android.content.Context
import android.view.ViewGroup
import android.widget.FrameLayout

class BusyViewContainer(context: Context) : FrameLayout(context) {
    private val busy: BusyView
    private var orientation = BOTTOM_RIGHT

    init {
        busy = BusyView(context)
        addView(busy, LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT))
    }

    fun setOrientation(o: Int) {
        orientation = o
    }

    fun setText(text: CharSequence) {
        busy.setText(text)
    }

    override fun onLayout(c: Boolean, l: Int, t: Int, r: Int, b: Int) {
        super.onLayout(c, l, t, r, b)
        if (orientation == TOP_LEFT) topLeft(l,t,r,b)
        else if (orientation == TOP_RIGHT) topRight(l,t,r,b)
        else if (orientation == BOTTOM_LEFT) bottomLeft(l,t,r,b)
        else if (orientation == BOTTOM_RIGHT) bottomRight(l, t, r, b)
        else center(l, t, r, b)
    }

    private fun center(l: Int, t: Int, r: Int, b: Int) {
        val bw = busy.width
        val bh = busy.height
        val w = r - l
        val h = b - t
        val nl = w / 2 - bw / 2
        val nt = h / 2 - bh / 2
        busy.layout(nl, nt, nl + bw, nt + bh)
    }

    private fun topLeft(l: Int, t: Int, r: Int, b: Int) {
        val bw = busy.width
        val bh = busy.height
        val nl = 0
        val nt = 0
        busy.layout(nl, nt, nl + bw, nt + bh)
    }

    private fun topRight(l: Int, t: Int, r: Int, b: Int) {
        val bw = busy.width
        val bh = busy.height
        val w = r - l
        val nl = w - bw
        val nt = 0
        busy.layout(nl, nt, nl + bw, nt + bh)
    }

    private fun bottomLeft(l: Int, t: Int, r: Int, b: Int) {
        val bw = busy.width
        val bh = busy.height
        val h = b - t
        val nl = 0
        val nt = h - bh
        busy.layout(nl, nt, nl + bw, nt + bh)
    }

    private fun bottomRight(l: Int, t: Int, r: Int, b: Int) {
        val bw = busy.width
        val bh = busy.height
        val w = r - l
        val h = b - t
        val nl = w - bw
        val nt = h - bh
        busy.layout(nl, nt, nl + bw, nt + bh)
    }

    companion object {
        const val TOP_LEFT = 1
        const val TOP_RIGHT = 2
        const val BOTTOM_LEFT = 3
        const val BOTTOM_RIGHT = 4
    }
}

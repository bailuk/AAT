package ch.bailu.aat.map.layer.control

import android.view.View
import android.widget.LinearLayout
import ch.bailu.aat.activities.AbsBackButton.OnBackPressedListener
import ch.bailu.aat.map.To.view
import ch.bailu.aat.util.ui.AppLayout
import ch.bailu.aat.views.bar.ControlBar
import ch.bailu.aat_lib.map.MapColor
import ch.bailu.aat_lib.map.MapContext
import ch.bailu.aat_lib.map.edge.Position
import ch.bailu.aat_lib.map.layer.MapLayerInterface
import ch.bailu.aat_lib.preferences.StorageInterface
import ch.bailu.aat_lib.util.Point
import javax.annotation.Nonnull

abstract class ControlBarLayer(mc: MapContext, val bar: ControlBar, private val placement: Position, color: Int) :
    MapLayerInterface, View.OnClickListener {

    private var w = 0
    private var h = 0

    init {
        val map = mc.mapView
        bar.setBackgroundColor(color)
        bar.setOnClickListener2(this)
        bar.visibility = View.GONE
        view(map)?.addView(bar)
        view(map)?.addView(object : OnBackPressedListener(bar.context) {
            override fun onBackPressed(): Boolean {
                if (view(map)?.visibility == VISIBLE && isBarVisible) {
                    hideBar()
                    return true
                }
                return false
            }
        })
    }

    constructor(c: MapContext, b: ControlBar, p: Position) : this(c, b, p, MapColor.MEDIUM)

    val isBarVisible: Boolean
        get() = bar.visibility == View.VISIBLE

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        if (changed) {
            w = r - l
            h = b - t
            val cs = bar.controlSize
            if (placement === Position.TOP) {
                bar.place(0, 0, w)
            } else if (placement === Position.LEFT) {
                bar.place(0, 0, h)
            } else if (placement === Position.BOTTOM) {
                bar.place(0, h - cs, w)
            } else if (placement === Position.RIGHT) {
                bar.place(w - cs, 0, h)
            }
        }
    }

    fun showBar() {
        if (!isBarVisible) {
            AppLayout.fadeIn(bar)
            onShowBar()
        }
    }

    open fun onShowBar() {}
    fun hideBar() {
        if (isBarVisible) {
            AppLayout.fadeOut(bar)
            onHideBar()
        }
    }

    open fun onHideBar() {}
    override fun onTap(tapXY: Point): Boolean {
        val size = bar.controlSize
        val y = tapXY.y
        val x = tapXY.x
        if (y < size) {
            topTap()
        } else if (y > h - size) {
            bottomTap()
        } else if (x < size) {
            leftTab()
        } else if (x > w - size) {
            rightTab()
        } else {
            middleTap()
        }
        return false
    }

    override fun onClick(v: View) {
        showBar()
    }

    private fun topTap() {
        showHideBar(Position.TOP)
    }

    private fun bottomTap() {
        showHideBar(Position.BOTTOM)
    }

    private fun middleTap() {
        hideBar()
    }

    private fun rightTab() {
        showHideBar(Position.RIGHT)
    }

    private fun leftTab() {
        showHideBar(Position.LEFT)
    }

    private fun showHideBar(p: Position) {
        if (p === placement) showBar() else hideBar()
    }

    override fun drawForeground(mcontext: MapContext) {}
    override fun onPreferencesChanged(@Nonnull s: StorageInterface, @Nonnull key: String) {}

    companion object {
        fun getOrientation(placement: Position): Int {
            return if (placement === Position.TOP || placement === Position.BOTTOM) {
                LinearLayout.HORIZONTAL
            } else LinearLayout.VERTICAL
        }
    }
}

package ch.bailu.aat_lib.map.edge

import ch.bailu.aat_lib.map.MapContext
import ch.bailu.aat_lib.map.layer.MapLayerInterface
import ch.bailu.aat_lib.preferences.StorageInterface
import ch.bailu.aat_lib.util.Point

class EdgeControlLayer(private val mcontext: MapContext, private val edgeSize: Int) : MapLayerInterface {

    private val controlBars = ArrayList<EdgeViewInterface>()

    private var width = 0
    private var height = 0

    override fun onAttached() {}

    override fun onDetached() {}

    override fun onPreferencesChanged(storage: StorageInterface, key: String) {}

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        width = r - l
        height = b - t
    }

    override fun drawInside(mcontext: MapContext?) {}

    override fun drawForeground(mcontext: MapContext?) {}

    override fun onTap(tapXY: Point): Boolean {
        var result = true

        val y = tapXY.y
        val x = tapXY.x

        if (y < edgeSize) {
            show(Position.TOP)
        } else if (y > height - edgeSize) {
            show(Position.BOTTOM)
        } else if (x < edgeSize) {
            show(Position.LEFT)
        } else if (x > width - edgeSize) {
            show(Position.RIGHT)
        } else {
            hide()
            result = false
        }
        mcontext.mapView.requestRedraw()
        return result
    }

    fun add(bar: EdgeViewInterface) {
        bar.hide()
        controlBars.add(bar)
    }

    private fun hide() {
        controlBars.forEach { it.hide() }
    }

    fun show(pos: Position) {
        controlBars.forEach {
            if (it.pos() != pos) {
                it.hide()
            }
        }
        controlBars.forEach {
            if (it.pos() == pos) {
                it.show()
            }
        }
    }
}

package ch.bailu.aat_gtk.ui.view.map.layer

import ch.bailu.aat_gtk.ui.view.map.control.Bar
import ch.bailu.aat_gtk.ui.view.map.control.MapBars
import ch.bailu.aat_lib.map.MapContext
import ch.bailu.aat_lib.map.layer.MapLayerInterface
import ch.bailu.aat_lib.preferences.StorageInterface
import ch.bailu.aat_lib.util.Point

class ControlBarLayer(private val barControl: MapBars) : MapLayerInterface {

    private var width = 0
    private var height = 0

    override fun onAttached() {

    }

    override fun onDetached() {

    }

    override fun onPreferencesChanged(storage: StorageInterface, key: String) {

    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        width = r - l
        height = b - t
    }

    override fun drawInside(mcontext: MapContext?) {

    }

    override fun drawForeground(mcontext: MapContext?) {

    }

    override fun onTap(tapXY: Point): Boolean {
        val size: Int = Bar.SIZE

        val y = tapXY.y
        val x = tapXY.x

        if (y < size) {
            barControl.show(MapBars.TOP)
            return true
        } else if (y > height - size) {
            barControl.show(MapBars.BOTTOM)
            return true
        } else if (x < size) {
            barControl.show(MapBars.LEFT)
            return true
        } else if (x > width - size) {
            barControl.show(MapBars.RIGHT)
            return true
        }
        barControl.hide()
        return false
    }
}
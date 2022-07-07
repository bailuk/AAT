package ch.bailu.aat_gtk.view.map.layer

import ch.bailu.aat_gtk.view.map.control.Bar
import ch.bailu.aat_lib.map.edge.EdgeControl
import ch.bailu.aat_lib.map.edge.Position
import ch.bailu.aat_lib.map.MapContext
import ch.bailu.aat_lib.map.layer.MapLayerInterface
import ch.bailu.aat_lib.preferences.StorageInterface
import ch.bailu.aat_lib.util.Point

class ControlBarLayer(private val mcontext: MapContext, private val barControl: EdgeControl) : MapLayerInterface {

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
        val size: Int = Bar.SIZE

        val y = tapXY.y
        val x = tapXY.x

        if (y < size) {
            barControl.show(Position.TOP)
        } else if (y > height - size) {
            barControl.show(Position.BOTTOM)
        } else if (x < size) {
            barControl.show(Position.LEFT)
        } else if (x > width - size) {
            barControl.show(Position.RIGHT)
        } else {
            barControl.hide()
            result = false
        }
        mcontext.mapView.requestRedraw()
        return result
    }
}

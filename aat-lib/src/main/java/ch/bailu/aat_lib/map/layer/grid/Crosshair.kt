package ch.bailu.aat_lib.map.layer.grid

import ch.bailu.aat_lib.map.MapContext
import ch.bailu.aat_lib.map.layer.MapLayerInterface
import ch.bailu.aat_lib.preferences.StorageInterface
import ch.bailu.aat_lib.util.Point

class Crosshair : MapLayerInterface {
    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {}
    override fun drawInside(mcontext: MapContext) {}
    override fun drawForeground(mcontext: MapContext) {
        drawGrid(mcontext)
    }

    private fun drawGrid(mcontext: MapContext) {
        val pixel = mcontext.getMetrics().getCenterPixel()
        mcontext.draw().vLine(pixel.x)
        mcontext.draw().hLine(pixel.y)
        mcontext.draw().point(pixel)
    }

    override fun onTap(tapPos: Point): Boolean {
        return false
    }

    override fun onPreferencesChanged(storage: StorageInterface, key: String) {}
    override fun onAttached() {}
    override fun onDetached() {}
}

package ch.bailu.aat_lib.map.layer.grid

import ch.bailu.aat_lib.coordinates.CH1903Coordinates
import ch.bailu.aat_lib.coordinates.CH1903Coordinates.Companion.inSwitzerland
import ch.bailu.aat_lib.coordinates.MeterCoordinates
import ch.bailu.aat_lib.map.MapContext
import ch.bailu.aat_lib.preferences.StorageInterface
import ch.bailu.aat_lib.util.Point
import org.mapsforge.core.model.LatLong

class CH1903GridLayer(storage: StorageInterface) : MeterGridLayer(storage) {
    override fun getCoordinates(p: LatLong): MeterCoordinates {
        return CH1903Coordinates(p)
    }

    override fun drawInside(mcontext: MapContext) {
        if (inSwitzerland(mcontext.getMetrics().boundingBox.centerPoint)) super.drawInside(mcontext)
    }

    override fun drawForeground(mcontext: MapContext) {
        if (inSwitzerland(mcontext.getMetrics().boundingBox.centerPoint)) super.drawForeground(mcontext)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {}
    override fun onTap(tapXY: Point): Boolean {
        return false
    }

    override fun onPreferencesChanged(storage: StorageInterface, key: String) {}
    override fun onAttached() {}
    override fun onDetached() {}
}

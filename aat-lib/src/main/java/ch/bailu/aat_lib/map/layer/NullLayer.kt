package ch.bailu.aat_lib.map.layer

import ch.bailu.aat_lib.map.MapContext
import ch.bailu.aat_lib.preferences.StorageInterface
import ch.bailu.aat_lib.util.Point

class NullLayer : MapLayerInterface {
    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {}
    override fun drawInside(mcontext: MapContext) {}
    override fun onTap(tapPos: Point): Boolean {
        return false
    }

    override fun drawForeground(mcontext: MapContext) {}
    override fun onAttached() {}
    override fun onDetached() {}
    override fun onPreferencesChanged(storage: StorageInterface, key: String) {}
}

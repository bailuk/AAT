package ch.bailu.aat_lib.map.layer

import ch.bailu.aat_lib.map.Attachable
import ch.bailu.aat_lib.map.MapContext
import ch.bailu.aat_lib.preferences.OnPreferencesChanged
import ch.bailu.aat_lib.util.Point

interface MapLayerInterface : Attachable, OnPreferencesChanged {
    fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int)
    fun drawInside(mcontext: MapContext)
    fun drawForeground(mcontext: MapContext)
    fun onTap(tapPos: Point): Boolean
}
